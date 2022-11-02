package com.gruppe_f.sep.entities.liga;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LigaRepository extends JpaRepository<Liga, Long> {

    Liga findLigaById(long id);

}
