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


    public void setWette(LeagueData leagueData, User user, double qoute, double einsatz, int tipp){

        Wetten wetten = new Wetten(leagueData.getId(), user.getId(), qoute, einsatz, false, tipp);

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


    public List<String[]> getFutureWetten(User user){
        List<String[]> futureBets = new ArrayList<>();

        for(Wetten wetten: wettenRepository.findAll()){
            if(!wetten.isResolved()){
                if(wetten.getUserID().equals(user.getId())){
                    if(GenerellLogisch.compareDates(dateRepository.findAll().get(0).getLocalDate(),
                            leagueDataRepository.findByid(wetten.getLeagueDataID()).getDate()) < 0){

                        String[] bet = {leagueDataRepository.findByid(wetten.getLeagueDataID()).getPlayer1(),
                                leagueDataRepository.findByid(wetten.getLeagueDataID()).getPlayer2(),
                                Integer.toString(wetten.getTipp()),
                                Double.toString(wetten.getQoute()),
                                Double.toString(wetten.getEinsatz())};

                        futureBets.add(bet);

                    }
                }
            }
        }
        return futureBets;
    }



    public List<String[]> getPastWetten(User user){
        List<String[]> pastBets = new ArrayList<>();
        List<Wetten> wettenList = new ArrayList<>();

        for(Wetten wetten: wettenRepository.findAll()){
            if(wetten.getUserID().equals(user.getId())){
                if((GenerellLogisch.compareDates(dateRepository.findAll().get(0).getLocalDate(),
                        leagueDataRepository.findByid(wetten.getLeagueDataID()).getDate()) > 0) || wetten.isResolved()){
                    String[] bet = {leagueDataRepository.findByid(wetten.getLeagueDataID()).getPlayer1(),
                            leagueDataRepository.findByid(wetten.getLeagueDataID()).getPlayer2(),
                            Integer.toString(wetten.getTipp()),
                            Double.toString(wetten.getQoute()),
                            Double.toString(wetten.getEinsatz())};

                    wettenList.add(wetten);
                    pastBets.add(bet);
                }
            }
        }

        calculateOldBets(user, wettenList);

        return pastBets;
    }

    public void calculateOldBets(User user, List<Wetten> wettenList){

        for(LeagueData leagueData: leagueDataRepository.findAll()){
            for(Wetten wetten: wettenList){
                if(!wetten.isResolved()){
                    if(leagueData.getId() == wetten.getLeagueDataID()){

                        if(Integer.parseInt(leagueData.getResult().split("-")[0]) > Integer.parseInt(leagueData.getResult().split("-")[1])){    //Mannschaft 1 hat gewonnen
                            if(wetten.getTipp() == 0){

                                user.setGuthaben(user.getGuthaben() + (wetten.getEinsatz() * wetten.getQoute()));

                                wetten.setResolved(true);
                                wettenRepository.save(wetten);
                            }
                            else{
                                wetten.setResolved(true);
                                wettenRepository.save(wetten);
                            }
                        }
                        else if(Integer.parseInt(leagueData.getResult().split("-")[0]) < Integer.parseInt(leagueData.getResult().split("-")[1])){   //Mannschaft 2 hat gewonnen
                            if(wetten.getTipp() == 2){

                                user.setGuthaben(user.getGuthaben() + (wetten.getEinsatz() * wetten.getQoute()));

                                wetten.setResolved(true);
                                wettenRepository.save(wetten);
                            }
                            else{
                                wetten.setResolved(true);
                                wettenRepository.save(wetten);
                            }
                        }
                        else{           //Unentschieden
                            if(wetten.getTipp() == 1){

                                user.setGuthaben(user.getGuthaben() + (wetten.getEinsatz() * wetten.getQoute()));

                                wetten.setResolved(true);
                                wettenRepository.save(wetten);
                            }
                            else{
                                wetten.setResolved(true);
                                wettenRepository.save(wetten);
                            }
                        }



                    }
                }
            }

        }



    }

}
