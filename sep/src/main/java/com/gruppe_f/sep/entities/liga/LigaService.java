package com.gruppe_f.sep.entities.liga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/Liga")
public class LigaService {


    private final LigaRepository ligaRepo;
    @Autowired
    public LigaService(LigaRepository ligaRepo) {
        this.ligaRepo = ligaRepo;
    }

    public Liga addLiga(Liga liga){
        return ligaRepo.save(liga);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Liga>> findAllLiga(){
        return new ResponseEntity<>(ligaRepo.findAll(), HttpStatus.OK);
    }

    public Liga updateLiga(Liga restaurants){
        return ligaRepo.save(restaurants);
    }

    public void deleteLiga(Long ligaId){
        ligaRepo.deleteById(ligaId);
    }


}
