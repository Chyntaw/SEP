package com.gruppe_f.sep.entities.bets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class BetsController {
    private BetsRepository betsrepo;
    @Autowired
    public BetsController (BetsRepository betsrepo){
        this.betsrepo = betsrepo;
    }
}
