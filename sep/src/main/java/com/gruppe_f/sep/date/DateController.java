package com.gruppe_f.sep.date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/systemdatum")
public class DateController {

    private final DateService service;

    @Autowired
    public DateController(DateService service) {
        this.service = service;
    }


    @PostMapping("/update")
    public ResponseEntity<?> datumAendern(@RequestParam("date") String systemDate){
        SystemDate newSystemDate = new SystemDate(systemDate);
        newSystemDate = service.changeDate(newSystemDate);
        return new ResponseEntity<>(newSystemDate,  HttpStatus.CREATED);
    }

    @GetMapping("/getDate")
    public ResponseEntity<?> getDate() {
        List<SystemDate> allDates = service.getAll();
        return new ResponseEntity<>(allDates.get(0), HttpStatus.OK);
    }
}
