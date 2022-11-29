package com.gruppe_f.sep.entities.BettingRound;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.scores.Score;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/bettingRound/")
public class BettingRoundController {
    private BettingRoundRepository repo;
    private UserRepository userRepo;
    private  LeagueDataRepository leagueDataRepo;
    private  DateRepository dateRepo;
    @Autowired
    public BettingRoundController (BettingRoundRepository repo, UserRepository userRepo, LeagueDataRepository leagueDataRepo, DateRepository dateRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.leagueDataRepo = leagueDataRepo;
        this.dateRepo = dateRepo;
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
        if(participants.contains(userRepo.findById(userid).get())) return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        participants.add(userRepo.findById(userid).get());
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.OK);
    }

    @GetMapping("getRoundsbyUserID/{id}")
    public ResponseEntity<?> getRoundsbyUserid(@PathVariable("id")Long id) {
        List<BettingRound> list = repo.findAll();
        List<BettingRound> returnlist = new LinkedList<>();
        for(BettingRound round: list) {
            for(User user: round.getParticipants()) {
                if(user.getId()==id) returnlist.add(round);
            }
        }
        return new ResponseEntity<>(returnlist, HttpStatus.OK);
    }

    @GetMapping("getAllPublicRounds")
    public ResponseEntity<?>getAllPublicRounds(){
        List<BettingRound> list = repo.findAll();
        List<BettingRound> returnlist = new LinkedList<>();

        for (BettingRound bettingRound : list) {
            if(bettingRound.isIsprivate()==false){
                returnlist.add(bettingRound);

            }
        }
        return new ResponseEntity<>(returnlist, HttpStatus.OK);


    }




    @PostMapping("placeBet")
    public ResponseEntity<?> placeBet(@RequestParam("bettingRoundid")Long bettingRoundid,
                                      @RequestParam("userid")Long userid,
                                      @RequestParam("leagueDataid")Long leagueDataid,
                                      @RequestParam("bet")String newBet) {

        BettingRound bettingRound = repo.findById(bettingRoundid).get();
        List<Bets> betsList = bettingRound.getBetsList();
        for(Bets bet: betsList) {
            //Check if user already placed Bet on this Game
            if(bet.getUserID() == userid && bet.getLeagueDataid() == leagueDataid) {
                bet.setBets(newBet);
                getLeagueDataByDate(bettingRound.getLigaID());
                return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.OK);
            }
        }
        betsList.add(new Bets(newBet, userid, leagueDataid));
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.CREATED);

    }


    //Langsam versteh ich warum man das eigentlich in Controller und Serviceklassen aufteilen sollte. NAJA MACHSTE NIX
    public List<LeagueData> getLeagueDataByDate(Long id) {

        List<LeagueData> list = leagueDataRepo.findAll();
        List<SystemDate> sysDate = dateRepo.findAll();
        List<LeagueData> returnList = new ArrayList<>();
        for (LeagueData data : list) {
            if (data.getLiga().getId() == id) {
                //Getting LeagueData by ID and setting result of Future games "0-0"
                if (data.getDate().compareTo(sysDate.get(0).getLocalDate()) < 0) {
                    data.getLiga().setLeagueData(null);
                    returnList.add(data);
                }
            }
        }
        return returnList;
    }
}
