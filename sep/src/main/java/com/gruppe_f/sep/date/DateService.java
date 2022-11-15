package com.gruppe_f.sep.date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DateService {

    private final DateRepository repository;

    @Autowired
    public DateService(DateRepository repository) {
        this.repository = repository;
    }

    public SystemDate changeDate(SystemDate systemDate){
/*
        SystemDate newSystemDate = new SystemDate(); //= repository.findByid(systemDate.getId());
        newSystemDate.setLocalDate(systemDate.getLocalDate());
        System.out.println(newSystemDate.toString());
        System.out.println(systemDate.toString());
        repository.deleteAll();
 */


        repository.deleteAll();
        return repository.save(systemDate);
    }

    public List<SystemDate> getAll() {
        return repository.findAll();
    }

}