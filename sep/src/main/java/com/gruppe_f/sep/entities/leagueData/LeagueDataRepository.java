package com.gruppe_f.sep.entities.leagueData;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueDataRepository extends JpaRepository<LeagueData, Long> {

    List<LeagueData> findBymatchDay(int matchDay);

    List<LeagueData> findByLigaName(String name);

    LeagueData findByid(int id);

}
