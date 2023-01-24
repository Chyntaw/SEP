package com.gruppe_f.sep;

import com.gruppe_f.sep.businesslogic.TipHelper;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.liga.LigaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TipHelperTest {

    @Test
    void contextloads() {

    }

    @Autowired
    private DateRepository dateRepository;

    @Autowired
    private LigaRepository ligaRepository;

    @Autowired
    private LeagueDataRepository leagueDataRepository;



    @Test
    void shouldDeleteDifference() {

        // save league
        Liga liga = new Liga("TestLiga", null);
        ligaRepository.save(liga);

        // save 3 matchday data
        // two teams to work with: x and y
        // 1
        LeagueData leagueData1 = new LeagueData();
        leagueData1.setPlayer1("x");
        leagueData1.setPlayer2("a");
        leagueData1.setResult("1-1");
        leagueData1.setDate("1000-01-01");
        leagueData1.setLigaID(1L);
        leagueDataRepository.save(leagueData1);

        // 2
        LeagueData leagueData2 = new LeagueData();
        leagueData2.setPlayer1("x");
        leagueData2.setPlayer2("b");
        leagueData2.setResult("5-2");
        leagueData2.setDate("1000-01-07");
        leagueData2.setLigaID(1L);
        leagueDataRepository.save(leagueData2);

        // 3
        LeagueData leagueData3 = new LeagueData();
        leagueData3.setPlayer1("y");
        leagueData3.setPlayer2("c");
        leagueData3.setResult("3-2");
        leagueData3.setDate("1000-01-11");
        leagueData3.setLigaID(1L);
        leagueDataRepository.save(leagueData3);

        // league data object to calculate result for
        LeagueData leagueDatatoCalculate = new LeagueData();
        leagueDatatoCalculate.setPlayer1("x");
        leagueDatatoCalculate.setPlayer2("y");
        leagueDatatoCalculate.setDate("3000-03-01");
        leagueDatatoCalculate.setLigaID(1L);
        leagueDataRepository.save(leagueDatatoCalculate);

        // save league again
        List<LeagueData> leagueDataList = new ArrayList<>();
        leagueDataList.add(leagueData1);
        leagueDataList.add(leagueData2);
        leagueDataList.add(leagueData3);
        leagueDataList.add(leagueDatatoCalculate);
        ligaRepository.save(liga);

        List<LeagueData> leagueDatas = leagueDataRepository.findAll();
        int id = 0;
        for(LeagueData  leagueData : leagueDatas) {
            if(leagueData.getPlayer1().equals("x") && leagueData.getPlayer2().equals("y")) {
                id = leagueData.getId();
            }
        }

        TipHelper tipHelper = new TipHelper(dateRepository, leagueDataRepository);
        Assertions.assertEquals("0-0", tipHelper.tipHelp(id));
        // Warum 0-0?
        // x spielt 1-1 und 5-2                 -> 6 eigene Tore, 3 gegnerische Tore
        // y spielt 4-2, dies wird dupliziert   -> 8 eigene Tore, 4 gegnerische Tore
        // max(6-8, 0)-max(3-4, 0) = 0-0

    }
}
