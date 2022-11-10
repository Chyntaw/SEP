package com.gruppe_f.sep.entities.leagueData;

import com.gruppe_f.sep.entities.liga.Liga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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

    @PutMapping("/update")
    public ResponseEntity<?> updateLeagueData(@RequestBody LeagueData data) {
        return new ResponseEntity<>(repo.save(data), HttpStatus.OK);
    }


    @GetMapping(path = "/getAll/{id}")
    public ResponseEntity<List<LeagueData>> getLeagueData(@PathVariable("id") Long id) {

        List<LeagueData> list = repo.findAll();

        List<LeagueData> returnList = new ArrayList<>();
        for(LeagueData data:list) {
            if (data.getLiga().getId() == id) returnList.add(data);
            data.setLiga(null);
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }
}
