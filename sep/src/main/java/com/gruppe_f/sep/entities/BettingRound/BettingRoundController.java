package com.gruppe_f.sep.entities.BettingRound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class BettingRoundController {
    private BettingRoundRepository repo;
    @Autowired
    public BettingRoundController (BettingRoundRepository repo ){
        this.repo = repo;
    }
}
