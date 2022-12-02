package com.gruppe_f.sep.entities.leagueData;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.liga.LigaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gruppe_f.sep.businesslogic.GenerellLogic.compareDates;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/leagueData")
public class LeagueDataController {

    private final LeagueDataRepository repo;
    private final DateRepository repo2;
    private final LigaRepository ligaRepo;

    @Autowired
    public LeagueDataController(LeagueDataRepository repo, DateRepository repo2, LigaRepository ligaRepo) {
        this.repo = repo;
        this.repo2 = repo2;
        this.ligaRepo = ligaRepo;
    }

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

        Liga liga = ligaRepo.findLigaByid(id);
        List<LeagueData> list = liga.getLeagueData();
        List<SystemDate> sysDate = repo2.findAll();
        List<LeagueData> returnList = new ArrayList<>();
        for (LeagueData data : list) {
                //Getting LeagueData by ID and setting result of Future games "0-0"
                if (compareDates(data.getDate(), sysDate.get(0).getLocalDate()) < 0) {
                    returnList.add(data);
                } else {
                    data.setResult("-");
                    returnList.add(data);
                }
            }
        //Sorting Resulting LeagueData List by Date
        List<LeagueData> result = returnList.stream().sorted((x, y) -> x.getDate().compareTo(y.getDate())).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueData> getData(@PathVariable("id") int id) {
        return new ResponseEntity<>(repo.findByid(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLeagueData(@PathVariable("id") int id, @RequestBody LeagueData data) {
        LeagueData newData = repo.findByid(id);
        newData.setResult(data.getResult());
        newData.setDate(data.getDate());
        newData.setPlayer1(data.getPlayer1());
        newData.setPlayer2(data.getPlayer2());
        newData.setMatchDay(data.getMatchDay());
        return new ResponseEntity<>(repo.save(newData), HttpStatus.OK);
    }
    @GetMapping("/getByMatchday/{LigaID}/{matchDayID}")
    public ResponseEntity<?> getByMatchday(@PathVariable("LigaID")Long LigaID, @PathVariable("matchDayID")int matchday) {

        Liga liga = ligaRepo.findLigaByid(LigaID);
        List<LeagueData> matchDayList = liga.getLeagueData();
        List<LeagueData> returnList = new ArrayList<>();
        for(LeagueData data : matchDayList) {
            if(data.getMatchDay() == matchday) {
                returnList.add(data);
            }
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/getMatchdays/{ligaID}")
    public ResponseEntity<?> getMatchdays(@PathVariable("ligaID")Long ligaID) {
        List<LeagueData> data = ligaRepo.findLigaByid(ligaID).getLeagueData();
        List<Integer> matchDaylist = new ArrayList<>();
        for(LeagueData game: data)
            if(!matchDaylist.contains(game.getMatchDay())) matchDaylist.add(game.getMatchDay());

        return  new ResponseEntity<>(matchDaylist, HttpStatus.OK);
    }
}
