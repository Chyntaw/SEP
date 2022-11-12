package com.gruppe_f.sep.date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DateService {

    private final DateRepository repository;

    @Autowired
    public DateService(DateRepository repository) {
        this.repository = repository;
    }

    public Date changeDate(Date date){
        return repository.save(date);
    }

}