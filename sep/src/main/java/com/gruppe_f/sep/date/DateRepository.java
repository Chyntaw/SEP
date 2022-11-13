package com.gruppe_f.sep.date;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DateRepository extends JpaRepository<SystemDate, Long> {


    SystemDate findByid(Long id);

}