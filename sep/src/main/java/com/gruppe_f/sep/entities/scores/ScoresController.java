package com.gruppe_f.sep.entities.scores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class ScoresController {
    private ScoresRepository scoresrepo;

    @Autowired
    public ScoresController(ScoresRepository scoresrepo) {this.scoresrepo = scoresrepo;
    }
}
