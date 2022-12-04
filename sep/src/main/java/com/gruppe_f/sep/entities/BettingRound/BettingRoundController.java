package com.gruppe_f.sep.entities.BettingRound;

import com.gruppe_f.sep.businesslogic.TipHelper;
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
import com.gruppe_f.sep.mail.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gruppe_f.sep.businesslogic.GenerellLogic.compareDates;
import static com.gruppe_f.sep.businesslogic.GenerellLogic.rightPadtext;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/bettingRound/")
public class BettingRoundController {
    private BettingRoundRepository repo;
    private UserRepository userRepo;
    private  LeagueDataRepository leagueDataRepo;
    private  DateRepository dateRepo;
    private LigaRepository ligaRepo;
    private MailSenderService mailService;
    @Autowired
    public BettingRoundController (BettingRoundRepository repo, UserRepository userRepo, LigaRepository ligaRepo, DateRepository dateRepo, LeagueDataRepository leagueDataRepo, MailSenderService mailService) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.leagueDataRepo = leagueDataRepo;
        this.dateRepo = dateRepo;
        this.ligaRepo = ligaRepo;
        this.mailService = mailService;
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



    @PostMapping("getTipHelp")
    public ResponseEntity<?> getTipHelpByTeams(@RequestParam("player1") String player1,
                                               @RequestParam("player2") String player2,
                                               @RequestParam("id") Long id) {

        TipHelper tipHelper = new TipHelper(dateRepo, leagueDataRepo);

        LeagueData leagueData = new LeagueData();
        leagueData.setResult(tipHelper.tipHelp(player1, player2, id));

        return new ResponseEntity<>(leagueData, HttpStatus.OK);
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

    @GetMapping("/shareBets/{userID}/{bettingroundID}/{friendID}")
    public ResponseEntity<?> shareBets(@PathVariable("userID")Long userID, @PathVariable("bettingroundID")Long bettingroundID, @PathVariable("friendID")Long friendID) {
        BettingRound bettingRound = repo.findById(bettingroundID).get();
        List<Bets> betsList = bettingRound.getBetsList();
        User user = userRepo.findById(userID).get();
        User mailRecipient = userRepo.findById(friendID).get();

        if(!betsList.isEmpty()) {

            //Eig unnötig, hab ich nur hinzugefügt, damit die Mail n bisschen schöner formatiert ist.
            int longestPlayer1 = 0;
            int longestPlayer2 = 0;
            for(Bets tempBet:betsList) {
                if(tempBet.getLeagueData().getPlayer1().length() > longestPlayer1) longestPlayer1 = tempBet.getLeagueData().getPlayer1().length();
                if(tempBet.getLeagueData().getPlayer2().length() > longestPlayer2) longestPlayer2 = tempBet.getLeagueData().getPlayer2().length();
            }
            //Beggining of Mail Message
            String mailBody ="Moin " +mailRecipient.getFirstName()+ ",\nfolgende Tipps hab ich abgegeben.\n\nGaLiGrü \nDein "+user.getFirstName()+" "+user.getLastName()+"\n\n\n"+rightPadtext("Player 1", longestPlayer1)+"\t\t"+rightPadtext("Player 2", longestPlayer2)+"\t\tMein Tipp\n";
            for(Bets bet: betsList) {
                if(bet.getUserID() == userID) {
                String player1 = rightPadtext(bet.getLeagueData().getPlayer1(), longestPlayer1);
                String player2 = rightPadtext(bet.getLeagueData().getPlayer2(), longestPlayer2);
                String myBet = bet.getBets();
                mailBody += player1 +"\t\t" + player2+"\t\t"+ myBet+"\n";
                }
            }
            mailBody += "\n\nWenn du auch so geile Tipps abgeben willst, KOMM IN DIE GRUPPE";

            mailService.sendEmail(mailRecipient.geteMail(),
                    "Meine Tipps in Tipprunde: "+bettingRound.getName(),
                    mailBody);      //schick Email hoffentlich

            //After Mail was sent, HttpStatus.OK
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //Forbidden, if User has no Tipps in this Tippround.
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/getTipprundeByLigaID/{userID}/{bettingroundID}")
    public ResponseEntity<?> getTipprundeByLigaID(@PathVariable("userID")Long userID, @PathVariable("bettingroundID")Long bettingroundID) {
        List<BettingRound> allRounds = repo.findAll();
        List<BettingRound> returnList = new ArrayList<>();
        User currUser = userRepo.findById(userID).get();
        Long currLigaID = repo.findById(bettingroundID).get().getLigaID();
        for(BettingRound round: allRounds) {
            if(round.getParticipants().contains(currUser) && (round.getLigaID() == currLigaID) && (round.getId() != bettingroundID)) returnList.add(round);
            }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("transferBets/{fromTipprundenID}/{toTipprundenID}/{userID}")
    public ResponseEntity<?> transferBets(@PathVariable("fromTipprundenID")Long fromID, @PathVariable("toTipprundenID")Long toID, @PathVariable("userID")Long userID) {
        BettingRound fromRound = repo.findById(fromID).get();
        BettingRound toRound = repo.findById(toID).get();

        List<Bets> existingBets = fromRound.getBetsList();
        //If no Bets present, End here
        if(existingBets.isEmpty()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        List<Bets> newBets = toRound.getBetsList();
        for(Bets oldBet: existingBets) {
            boolean changed = false;
            if(!newBets.isEmpty()) {
                for(Bets newBet: newBets) {
                    //Check if there already exists a bet for this game by the user
                    if((oldBet.getUserID() == userID) && (newBet.getUserID() == userID) && (oldBet.getLeagueData().getId() == newBet.getLeagueData().getId())) {
                        newBet.setBets(oldBet.getBets());
                        changed = true;
                    }
                }
            }
            //If nothing was changed before
            if(!changed) {
                newBets.add(new Bets(oldBet.getBets(), oldBet.getUserID(), oldBet.getLeagueData()));
            }
        }
        repo.save(toRound);
        repo.save(fromRound);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
