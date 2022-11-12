package com.gruppe_f.sep.date;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DateRepository extends JpaRepository<Date, LocalDate> {

}