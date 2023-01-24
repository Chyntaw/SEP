package com.gruppe_f.sep;

import com.gruppe_f.sep.businesslogic.QouteRechner;
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

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class QouteRechnerTest {

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
    void gibMirDieQoute(){
        // save league
        Liga liga = new Liga("TestLiga", null);
        ligaRepository.save(liga);

        System.out.println(ligaRepository.findLigaByid(1L).getName());

        SystemDate systemDate = new SystemDate("4000-01-01");
        dateRepository.save(systemDate);

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

        //4
        LeagueData leagueData4 = new LeagueData();
        leagueData4.setPlayer1("x");
        leagueData4.setPlayer2("y");
        leagueData4.setResult("3-2");
        leagueData4.setDate("1000-01-11");
        leagueData4.setLigaID(1L);
        leagueDataRepository.save(leagueData4);

        //5
        LeagueData leagueData5 = new LeagueData();
        leagueData5.setPlayer1("y");
        leagueData5.setPlayer2("x");
        leagueData5.setResult("0-0");
        leagueData5.setDate("1000-01-11");
        leagueData5.setLigaID(1L);
        leagueDataRepository.save(leagueData5);

        // 6
        LeagueData leagueData6 = new LeagueData();
        leagueData6.setPlayer1("y");
        leagueData6.setPlayer2("c");
        leagueData6.setResult("3-2");
        leagueData6.setDate("1000-01-11");
        leagueData6.setLigaID(1L);
        leagueDataRepository.save(leagueData6);

        //7
        LeagueData leagueData7 = new LeagueData();
        leagueData7.setPlayer1("r");
        leagueData7.setPlayer2("c");
        leagueData7.setResult("2-5");
        leagueData7.setDate("1000-01-11");
        leagueData7.setLigaID(1L);
        leagueDataRepository.save(leagueData7);

        //8
        LeagueData leagueData8 = new LeagueData();
        leagueData8.setPlayer1("c");
        leagueData8.setPlayer2("y");
        leagueData8.setResult("0-0");
        leagueData8.setDate("1000-01-11");
        leagueData8.setLigaID(1L);
        leagueDataRepository.save(leagueData8);



        //test wenn spiel zwischen anderen Spielen stattfindet

        LeagueData leagueData9 = new LeagueData();
        leagueData9.setPlayer1("x");
        leagueData9.setPlayer2("c");
        leagueData9.setResult("0-0");
        leagueData9.setDate("2000-01-11");
        leagueData9.setLigaID(1L);
        leagueDataRepository.save(leagueData9);

        QouteRechner qouteRechner = new QouteRechner(dateRepository, leagueDataRepository);

        double[] ergebnis = {2.0, 4.0, 3.0};



        Assertions.assertArrayEquals(ergebnis,
                qouteRechner.qouteBerechnen(leagueData9.getPlayer1(), leagueData9.getPlayer2(), leagueData9.getDate(), leagueData9.getId()));



        /*
        Spieler 1 Qoute: 2.0/4.0/2.0
        Spieler 2 Qoute: 4.0/2.0/4.0

        QouteSpiel 2.0/4.0/3.0
        2.0 Sieg Spieler 1
        4.0 Unetschieden
        3.0 Sieg Spieler 2
        Warum:
        Spieler 1 4 Spiele:
        1-1 5-2 3-2 0-0
        -> 2 Siege/ 0 Niederlagen / 2 Uentschieden
        -> 4/2 (da 4 Spiele gespielt) = 2er Qoute
        usw.

        Spieler 2 gleiches Prinzip

        SpielQoute
        -> Spieler mit der niedrigeren Siegesqoute bekommt die kleinere Qoute
        -> Spieler mit der höheren Niederlageqoute bekommt die höhere Qoute
        -> Uentschieden = Beide UentschiedenQoute addieren / 2 teilen

         */
    }
}
