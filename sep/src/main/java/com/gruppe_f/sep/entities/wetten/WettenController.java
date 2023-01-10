package com.gruppe_f.sep.entities.wetten;

import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class WettenController {


    private final WettenService wettenService;
    private final UserRepository userRepository;
    private final LeagueDataRepository leagueDataRepository;

    @Autowired
    public WettenController(WettenService wettenService,
                            UserRepository userRepository,
                            LeagueDataRepository leagueDataRepository) {
        this.wettenService = wettenService;
        this.userRepository = userRepository;
        this.leagueDataRepository = leagueDataRepository;
    }


                //setzeWette/"+leagueDataIDzumWetten+"/"+eMail+"/"+qoute+"/"+wettEinsatz
    @GetMapping("/wetten/setzeWette/{leagueDataIDzumWetten}/{eMail}/{qoute}/{wettEinsatz}")
    public ResponseEntity<?> setzeWette(@PathVariable("leagueDataIDzumWetten") int id,
                                     @PathVariable("eMail") String userEMail,
                                     @PathVariable("qoute") double qoute,
                                     @PathVariable("wettEinsatz") double wettEinsatz){

        LeagueData leagueData = leagueDataRepository.findByid(id);
        User user = userRepository.findUserByeMail(userEMail);

        wettenService.setWette(leagueData, user, qoute, wettEinsatz);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
