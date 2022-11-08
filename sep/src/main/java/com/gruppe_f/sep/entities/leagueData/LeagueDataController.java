package com.gruppe_f.sep.entities.leagueData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/leagueData")
public class LeagueDataController {

    private final LeagueDataRepository repo;

    @Autowired
    public LeagueDataController(LeagueDataRepository repo) {this.repo = repo;}

    @PostMapping("/add")
    public ResponseEntity<LeagueData> addLeagueData(@RequestBody LeagueData data) {
        LeagueData newData = repo.save(data);
        return new ResponseEntity<>(newData, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LeagueData>> getLeagueData() {
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
}
