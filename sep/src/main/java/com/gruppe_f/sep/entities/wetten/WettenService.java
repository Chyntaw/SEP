package com.gruppe_f.sep.entities.wetten;

import com.gruppe_f.sep.businesslogic.GenerellLogisch;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WettenService {

    private final WettenRepository wettenRepository;
    private final DateRepository dateRepository;

    private final UserRepository userRepository;
    private final LeagueDataRepository leagueDataRepository;

    @Autowired
    public WettenService(WettenRepository wettenRepository, DateRepository dateRepository, UserRepository userRepository, LeagueDataRepository leagueDataRepository) {
        this.wettenRepository = wettenRepository;
        this.dateRepository = dateRepository;
        this.userRepository = userRepository;
        this.leagueDataRepository = leagueDataRepository;
    }


    public void setWette(LeagueData leagueData, User user, double qoute, double einsatz){

        Wetten wetten = new Wetten(leagueData.getId(), user.getId(), qoute, einsatz, false);

        User updateUser = user;
        updateUser.setGuthaben(updateUser.getGuthaben() - einsatz);
        userRepository.save(updateUser);
        wettenRepository.save(wetten);
    }


    public void resolveWette(Wetten wetten){
        Wetten wette = wetten;
        wette.setResolved(true);

        User updateUser = userRepository.findUserById(wette.getUserID());
        updateUser.setGuthaben(updateUser.getGuthaben() + (wette.getEinsatz() * wette.getQoute()));

        userRepository.save(updateUser);
        wettenRepository.save(wette);
    }




    public List<Wetten> getFutureWetten(User user){
        List<Wetten> userWetten = new ArrayList<>();

        for(Wetten wetten: wettenRepository.findAll()){
            if(wetten.getId().equals(user.getId())){
                if(GenerellLogisch.compareDates(dateRepository.findAll().get(0).getLocalDate(),
                        leagueDataRepository.findByid(wetten.getLeagueDataID()).getDate()) >0){
                    userWetten.add(wetten);
                }
            }
        }
        return userWetten;
    }



    public List<Wetten> getPastWetten(User user){
        List<Wetten> userWetten = new ArrayList<>();

        for(Wetten wetten: wettenRepository.findAll()){
            if(wetten.getId().equals(user.getId())){
                if(GenerellLogisch.compareDates(dateRepository.findAll().get(0).getLocalDate(),
                        leagueDataRepository.findByid(wetten.getLeagueDataID()).getDate()) < 0){
                    userWetten.add(wetten);
                }
            }
        }
        return userWetten;
    }


}
