package com.gruppe_f.sep.entities.BettingRound;

import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/bettingRound/")
public class BettingRoundController {
    private BettingRoundRepository repo;
    private UserRepository userRepo;
    @Autowired
    public BettingRoundController (BettingRoundRepository repo, UserRepository userRepo) {this.repo = repo; this.userRepo = userRepo;}


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
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.CREATED);
    }

    @PutMapping("addParticipant")
    public ResponseEntity<?> addParticipant(@RequestParam("userid") Long userid, @RequestParam("bettingRoundid") Long bettingRoundid) {
        BettingRound bettingRound = repo.findById(bettingRoundid).get();
        bettingRound.getParticipants().add(userRepo.findById(userid).get());
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

}
