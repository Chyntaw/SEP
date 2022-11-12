package com.gruppe_f.sep.date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DateController {

    private final DateService service;

    @Autowired
    public DateController(DateService service) {
        this.service = service;
    }


    @PostMapping("/user/systemdatum")
    public ResponseEntity<?> datumAendern(@RequestBody Date date){
        Date newDate = service.changeDate(date);

        return new ResponseEntity<>(newDate, HttpStatus.OK);
    }
}
