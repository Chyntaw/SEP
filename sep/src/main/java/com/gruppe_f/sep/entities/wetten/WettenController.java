package com.gruppe_f.sep.entities.wetten;

import com.gruppe_f.sep.businesslogic.GenerellLogisch;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class WettenController {


    private final WettenService wettenService;
    private final UserRepository userRepository;
    private final LeagueDataRepository leagueDataRepository;
    private final DateRepository dateRepository;

    @Autowired
    public WettenController(WettenService wettenService,
                            UserRepository userRepository,
                            LeagueDataRepository leagueDataRepository,
                            DateRepository dateRepository) {
        this.wettenService = wettenService;
        this.userRepository = userRepository;
        this.leagueDataRepository = leagueDataRepository;
        this.dateRepository = dateRepository;
    }


                //setzeWette/"+leagueDataIDzumWetten+"/"+eMail+"/"+qoute+"/"+wettEinsatz
    @GetMapping("/wetten/setzeWette/{leagueDataIDzumWetten}/{eMail}/{qoute}/{wettEinsatz}/{tipp}")
    public ResponseEntity<?> setzeWette(@PathVariable("leagueDataIDzumWetten") int id,
                                        @PathVariable("eMail") String userEMail,
                                        @PathVariable("qoute") double qoute,
                                        @PathVariable("wettEinsatz") double wettEinsatz,
                                        @PathVariable("tipp") int tipp){

        if(GenerellLogisch.compareDates(dateRepository.findAll().get(0).getLocalDate(), leagueDataRepository.findByid(id).getDate()) < 0){
            LeagueData leagueData = leagueDataRepository.findByid(id);
            User user = userRepository.findUserByeMail(userEMail);
            wettenService.setWette(leagueData, user, qoute, wettEinsatz, tipp);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/wetten/platzierteWetten/{eMail}")
    public ResponseEntity<List<String[]>> platzierteWetten(@PathVariable("eMail") String userEmail){
        return new ResponseEntity<>(wettenService.getFutureWetten(userRepository.findUserByeMail(userEmail)), HttpStatus.OK);
    }

    @GetMapping("/wetten/vergangeneWetten/{eMail}")
    public ResponseEntity<List<String[]>> vergangeneWetten(@PathVariable("eMail") String userEmail){
        return new ResponseEntity<>(wettenService.getPastWetten(userRepository.findUserByeMail(userEmail)), HttpStatus.OK);
    }


}
