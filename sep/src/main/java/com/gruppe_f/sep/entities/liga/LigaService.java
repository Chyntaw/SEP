package com.gruppe_f.sep.entities.liga;

import java.util.List;

public class LigaService {

    private final LigaRepository ligaRepo;

    public LigaService(LigaRepository ligaRepo) {
        this.ligaRepo = ligaRepo;
    }

    public Liga addLiga(Liga liga){
        return ligaRepo.save(liga);
    }

    public List<Liga> findAllLiga(){
        return ligaRepo.findAll();
    }

    public Liga updateLiga(Liga restaurants){
        return ligaRepo.save(restaurants);
    }

    public void deleteLiga(Long ligaId){
        ligaRepo.deleteById(ligaId);
    }


}
