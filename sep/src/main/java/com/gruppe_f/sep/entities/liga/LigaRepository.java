package com.gruppe_f.sep.entities.liga;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigaRepository extends JpaRepository<Liga, Long> {

    Liga findLigaByid(long id);




}
