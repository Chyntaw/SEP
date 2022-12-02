package com.gruppe_f.sep.entities.BettingRound;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.alias.Alias;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.liga.LigaRepository;
import com.gruppe_f.sep.entities.scores.Score;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gruppe_f.sep.businesslogic.GenerellLogic.compareDates;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/bettingRound/")
public class BettingRoundController {
    private BettingRoundRepository repo;
    private UserRepository userRepo;
    private  LeagueDataRepository leagueDataRepo;
    private  DateRepository dateRepo;
    private LigaRepository ligaRepo;
    @Autowired
    public BettingRoundController (BettingRoundRepository repo, UserRepository userRepo, LigaRepository ligaRepo, DateRepository dateRepo, LeagueDataRepository leagueDataRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.leagueDataRepo = leagueDataRepo;
        this.dateRepo = dateRepo;
        this.ligaRepo = ligaRepo;
    }


    @PostMapping("create")
    public ResponseEntity<?> createBettingRound(@RequestParam("name")String name,
                                                @RequestParam("ownerID")Long ownerID,
                                                @RequestParam("ligaID")Long ligaID,
                                                @RequestParam("isPrivate")Boolean isPrivate,
                                                @RequestParam("corrScorePoints")int corrScorePoints,
                                                @RequestParam("corrGoalPoints")int corrGoalPoints,
                                                @RequestParam("corrWinnerPoints")int corrWinnerPoints,
                                                @RequestParam(value = "password", required = false)String password) {

        BettingRound bettingRound = new BettingRound(name, ownerID, ligaID, isPrivate, corrScorePoints, corrGoalPoints, corrWinnerPoints, password);
        repo.save(bettingRound);
        addParticipant(ownerID, bettingRound.getId());
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.CREATED);
    }

    @PostMapping("addParticipant")
    public ResponseEntity<?> addParticipant(@RequestParam("userid") Long userid, @RequestParam("bettingRoundid") Long bettingRoundid) {
        BettingRound bettingRound = repo.findById(bettingRoundid).get();
        List<User> participants = bettingRound.getParticipants();
        User newParticipant = userRepo.findById(userid).get();
        //Check if user already participating
        if(participants.contains(newParticipant)) return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);

        participants.add(newParticipant);
        bettingRound.getScoresList().add(new Score(0, newParticipant));
        //Create Alias for new participant, Alias init with firstname, can be changed calling the same method.
        changeAlias(userid, bettingRoundid, newParticipant.getFirstName());

        //ReturnType is Bettinground, not Userlist
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.OK);
    }

    @GetMapping("getRoundsbyUserID/{id}")
    public ResponseEntity<?> getRoundsbyUserid(@PathVariable("id")Long id) {
        List<BettingRound> list = repo.findAll();
        List<BettingRound> returnlist = new LinkedList<>();

        for(BettingRound round: list)
            for(User user: round.getParticipants())
                if(user.getId()==id) returnlist.add(round);

        return new ResponseEntity<>(returnlist, HttpStatus.OK);
    }

    @GetMapping("getAllPublicRounds")
    public ResponseEntity<?>getAllPublicRounds(){
        List<BettingRound> list = repo.findAll();
        List<BettingRound> returnlist = new LinkedList<>();

        for (BettingRound bettingRound : list) {
            if(!bettingRound.isIsprivate()){
                returnlist.add(bettingRound);
            }
        }
        return new ResponseEntity<>(returnlist, HttpStatus.OK);

    }


    @GetMapping("getBets")
    public ResponseEntity<?> getBets(@RequestParam("bettingRoundid")Long bettingRoundid, @RequestParam("userid")Long userid) {
        List<Bets> bets = repo.findById(bettingRoundid).get().getBetsList();
        List<Bets> retList = new ArrayList<>();
        for(Bets bet: bets)
            if(bet.getUserID() == userid) retList.add(bet);
        return new ResponseEntity<>(retList, HttpStatus.OK);
    }

    @PutMapping("getBetsByLeagueDataID")
    public ResponseEntity<?> BetsByLDI(@RequestParam("userID")Long userID, @RequestParam("leagueDataids")Integer[] leagueDataids, @RequestParam("bettingRoundID")Long bettingRoundID) {
        List<Bets> betsList = repo.findById(bettingRoundID).get().getBetsList();
        List<String> returnList = new ArrayList<>();

        for(int x = 0; x < leagueDataids.length; x++) {
            for(Bets bet: betsList) {
                if(bet.getUserID() == userID && bet.getLeagueData().getId() == leagueDataids[x]) {
                    returnList.add(bet.getBets());
                }
            }
            if(x == returnList.size()-1) continue;
            returnList.add("N/A");
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }


    @PostMapping("placeBet")
    public ResponseEntity<?> placeBet(@RequestParam("bettingRoundid")Long bettingRoundid,
                                      @RequestParam("userid")Long userid,
                                      @RequestParam("leagueDataid")int leagueDataid,
                                      @RequestParam("newBet")String newBet) {

        BettingRound bettingRound = repo.findById(bettingRoundid).get();
        List<Bets> betsList = bettingRound.getBetsList();
        for(Bets bet: betsList) {
            //Check if user already placed Bet on this Game
            if(bet.getUserID() == userid && bet.getLeagueData().getId() == leagueDataid) {
                if(newBet == null) betsList.remove(bet);
                else bet.setBets(newBet);
                //getLeagueDataByDate(bettingRound.getLigaID());
                return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.OK);
            }
        }
        if(newBet != null) {
            LeagueData data = leagueDataRepo.findByid(leagueDataid);
            betsList.add(new Bets(newBet, userid, data));
        }
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.CREATED);

    }


    //Langsam versteh ich warum man das eigentlich in Controller und Serviceklassen aufteilen sollte. NAJA MACHSTE NIX
    public List<LeagueData> getLeagueDataByDate(Long id) {

        //GEHT BESSER
        Liga liga = ligaRepo.findLigaByid(id);
        List<LeagueData> list = liga.getLeagueData();
        List<SystemDate> sysDate = dateRepo.findAll();
        List<LeagueData> returnList = new ArrayList<>();
        for (LeagueData data : list) {
            //Getting LeagueData by Date
            if (compareDates(data.getDate(), sysDate.get(0).getLocalDate()) <= 0) {
                returnList.add(data);
            }
        }
        return returnList;
    }

    @GetMapping("leaderboard/{bettingroundid}")
    public ResponseEntity<?> leaderboard(@PathVariable("bettingroundid")Long id) {
        BettingRound bettingRound = repo.findById(id).get();
        List<Bets> betsList = bettingRound.getBetsList();
        List<LeagueData> leagueDatalist = getLeagueDataByDate(bettingRound.getLigaID());
        System.out.println(leagueDatalist.size());

        for(Bets bet: betsList) {
            for(LeagueData data: leagueDatalist) {
                if(bet.getLeagueData().getId() == data.getId()) {
                    //If bet on score is correct, Strings of scores are identical
                    if(bet.getBets().equals(data.getResult())) {bet.setScore(bettingRound.getCorrScorePoints()); continue;}
                    //Get goals from LeagueData and from bet as int
                    int[] goals = Arrays.stream(data.getResult().split("-")).mapToInt(Integer::parseInt).toArray();
                    int[] betGoals = Arrays.stream(bet.getBets().split("-")).mapToInt(Integer::parseInt).toArray();
                    int goalDiff = goals[0]-goals[1];
                    int betDiff = betGoals[0]-betGoals[1];
                    //Correct Goal Difference
                    if(Math.abs(goalDiff) == Math.abs(betDiff)) {bet.setScore(bettingRound.getCorrGoalPoints()); continue;}
                    //Correct Winner
                    if((goalDiff < 0 && betDiff < 0) || (goalDiff >0 && betDiff >0)) bet.setScore(bettingRound.getCorrWinnerPoints());
                }
            }
        }
        List<Score> scoreList = bettingRound.getScoresList();
        for(Score score:scoreList) {
            for(Bets bet: betsList) {
                if(score.getUser().getId() == bet.getUserID()) score.setScores(score.getScores()+ bet.getScore());
            }
        }
        List<Score> list = scoreList.stream().sorted((x,y) -> x.getScores() - y.getScores()).collect(Collectors.toList());
        /*
        for(Score score:list) {
            for(Alias alias: bettingRound.getAliasList()) {
                if(score.getUser().getId() == alias.getUserID()) {
                    score.getUser().setFirstName(alias.getAlias());
                }
            }
        }
        */
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/getByMatchday/{userID}/{matchDayID}/{bettingroundID}")
    public ResponseEntity<?> getByMatchday(@PathVariable("userID")Long userID, @PathVariable("matchDayID")int matchday, @PathVariable("bettingroundID")Long bettingroundID) {

        BettingRound round = repo.findById(bettingroundID).get();
        List<LeagueData> leagueData = ligaRepo.findLigaByid(round.getLigaID()).getLeagueData();
        List<Bets> betsList = round.getBetsList();

        List<Bets> returnList = new ArrayList<>();
        for(LeagueData data : leagueData) {
            if(data.getMatchDay() == matchday) {
                //returnList.add(data);
            }
        }
        return new ResponseEntity<>(betsList, HttpStatus.OK);
    }

    @PutMapping("/changeAlias")
    public ResponseEntity<?> changeAlias(@RequestParam("userID")Long userID, @RequestParam("bettingroundID")Long bettingroundID, @RequestParam("alias")String alias ) {
        BettingRound betR = repo.findById(bettingroundID).get();
        List<Alias> aliasList = betR.getAliasList();
        for(Alias userAlias : aliasList) {
            if(userAlias.getUserID() == userID) {
                userAlias.setAlias(alias);
                repo.save(betR);
                return  new ResponseEntity<>(HttpStatus.OK);
            }
        }
        aliasList.add(new Alias(alias, userID));
        repo.save(betR);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
