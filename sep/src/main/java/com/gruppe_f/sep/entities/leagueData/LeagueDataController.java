package com.gruppe_f.sep.entities.leagueData;

import com.gruppe_f.sep.businesslogic.QouteRechner;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.liga.LigaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gruppe_f.sep.businesslogic.GenerellLogisch.compareDates;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/leagueData")
public class LeagueDataController {



    private final LeagueDataRepository repo;
    private final DateRepository dateRepo;
    private final LigaRepository ligaRepo;


    @Autowired
    public LeagueDataController(LeagueDataRepository repo, DateRepository dateRepo, LigaRepository ligaRepo) {
        this.repo = repo;
        this.dateRepo = dateRepo;
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

        List<LeagueData> list = ligaRepo.findLigaByid(id).getLeagueData();
        String currDate =  dateRepo.findAll().get(0).getLocalDate();

        //Compare Date of each element of the List to System Date.
        //If Future - Game -> set result to "-"
        list.stream().forEach(x -> {
            if(!(compareDates(x.getDate(), currDate) < 0)) x.setResult("-");
        });
                                    //Return list sorted by Date
        return new ResponseEntity<>(list.stream().sorted((x, y) -> x.getDate().compareTo(y.getDate())).collect(Collectors.toList()), HttpStatus.OK);

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

        List<LeagueData> matchDayList = ligaRepo.findLigaByid(LigaID).getLeagueData().stream()
                .filter(x -> x.getMatchDay() == matchday).collect(Collectors.toList());

        return new ResponseEntity<>(matchDayList, HttpStatus.OK);
    }

    @GetMapping("/getMatchdays/{ligaID}")
    public ResponseEntity<?> getMatchdays(@PathVariable("ligaID")Long ligaID) {
        List<LeagueData> data = ligaRepo.findLigaByid(ligaID).getLeagueData();
        List<Integer> matchDaylist = new ArrayList<>();

        for(LeagueData game: data)
            if(!matchDaylist.contains(game.getMatchDay())) matchDaylist.add(game.getMatchDay());

        return  new ResponseEntity<>(matchDaylist, HttpStatus.OK);
    }

    @GetMapping("/findLigaByID/{ligaID}")
    public ResponseEntity<?> getData(@PathVariable("ligaID") Long ligaID) {
        Liga liga = ligaRepo.findLigaByid(ligaID);
        return new ResponseEntity<>(liga, HttpStatus.OK);
    }




    @GetMapping("/getOddsForLiga/{id}")
    public ResponseEntity<List<double[]>> getOddsForLiga(@PathVariable("id") Long ligaID){

        QouteRechner qouteRechner = new QouteRechner(dateRepo, repo);

        List<double[]> oddsList = new ArrayList<>();

        for(LeagueData leagueData: ligaRepo.findLigaByid(ligaID).getLeagueData()){
            oddsList.add(qouteRechner.qouteBerechnen(leagueData.getPlayer1(), leagueData.getPlayer2(), leagueData.getDate()));
        }
        return new ResponseEntity<>(oddsList, HttpStatus.OK);
    }

}
