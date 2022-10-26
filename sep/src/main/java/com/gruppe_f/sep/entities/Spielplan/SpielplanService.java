package com.gruppe_f.sep.entities.Spielplan;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpielplanService {

    private final SpielplanRepository spielplanRepo;


    public SpielplanService(SpielplanRepository spielplanRepo) {
        this.spielplanRepo = spielplanRepo;
    }

    public Spielplan addSpielplan(Spielplan spielplan){
        return spielplanRepo.save(spielplan);
    }

    public List<Spielplan> findAllSpielplans(){
        return spielplanRepo.findAll();
    }

    public Spielplan updateSpielplan(Spielplan spielplan){
        return spielplanRepo.save(spielplan);
    }

    public void deleteSpielplan(int spielplanId){
        spielplanRepo.deleteById(spielplanId);
    }

}
