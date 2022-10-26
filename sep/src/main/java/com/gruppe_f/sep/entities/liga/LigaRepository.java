package com.gruppe_f.sep.entities.liga;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LigaRepository extends JpaRepository<Liga, Integer> {

    Liga findLigaById(long id);

}
