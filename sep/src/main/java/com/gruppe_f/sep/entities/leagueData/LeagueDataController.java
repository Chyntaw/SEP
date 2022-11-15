package com.gruppe_f.sep.entities.leagueData;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.liga.Liga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/leagueData")
public class LeagueDataController {

    private final LeagueDataRepository repo;
    private final DateRepository repo2;

    @Autowired
    public LeagueDataController(LeagueDataRepository repo, DateRepository repo2) {this.repo = repo; this.repo2= repo2;}
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
        List<SystemDate> sysDate = repo2.findAll();
        List<LeagueData> returnList = new ArrayList<>();
        for(LeagueData data:list) {
            if (data.getLiga().getId() == id) {
                //Getting LeagueData by ID and setting result of Future games "0-0"
                if(data.getDate().compareTo(sysDate.get(0).getLocalDate()) < 0) {
                    data.getLiga().setLeagueData(null);
                    returnList.add(data);
                } else {
                    data.setResult("0-0");
                    data.getLiga().setLeagueData(null);
                    returnList.add(data);
                }
            }
        }
        //Sorting Resulting LeagueData List by Date
        List<LeagueData> result = returnList.stream().sorted((x, y)-> x.getDate().compareTo(y.getDate())).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueData> getData(@PathVariable("id") int id) {
        LeagueData data = repo.findByid(id);
        data.getLiga().setLeagueData(null);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLeagueData(@PathVariable("id") int id, @RequestBody LeagueData data) {
        LeagueData newData = repo.findByid(id);
        newData.getLiga().setLeagueData(null);
        newData.setResult(data.getResult());
        newData.setDate(data.getDate());
        newData.setPlayer1(data.getPlayer1());
        newData.setPlayer2(data.getPlayer2());
        newData.setMatchDay(data.getMatchDay());
        return new ResponseEntity<>(repo.save(newData), HttpStatus.OK);
    }
}
