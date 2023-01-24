package com.gruppe_f.sep;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.BettingRound.BettingRoundController;
import com.gruppe_f.sep.entities.BettingRound.BettingRoundRepository;
import com.gruppe_f.sep.entities.alias.AliasRepository;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.bets.BetsController;
import com.gruppe_f.sep.entities.bets.BetsRepository;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.liga.LigaRepository;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminBettingRoundResetterTest {

    @Test
    void contextloads() {

    }
    @Autowired
    private DateRepository dateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AliasRepository aliasRepository;
    @Autowired
    private BetsRepository betsRepository;
    @Autowired
    private LigaRepository ligaRepository;
    @Autowired
    private BettingRoundRepository bettingRoundRepository;
    @Autowired
    private LeagueDataRepository leagueDataRepository;

    @Test
    void shouldResetAllBettingRounds() {

        // save 2 leagues
        Liga liga1 = new Liga("TestLiga1", null);
        ligaRepository.save(liga1);

        Liga liga2 = new Liga("TestLiga2", null);
        ligaRepository.save(liga2);

        List<Liga> ligas = ligaRepository.findAll();

        Long liga1ID = 0L;
        Long liga2ID = 0L;

        for(Liga liga : ligas) {
            if("TestLiga1" == liga.getName()) {
                liga1ID = liga.getId();
            }
            else {
                liga2ID = liga.getId();
            }
        }

        LeagueData leagueData1 = new LeagueData();
        leagueData1.setPlayer1("x");
        leagueData1.setPlayer2("a");
        leagueData1.setResult("1-1");
        leagueData1.setDate("1000-01-01");
        leagueData1.setLigaID(2L);
        leagueDataRepository.save(leagueData1);

        // save 1 bettinground for each league
        BettingRound bettingRound1 = new BettingRound(
                "BettingRound1",    // name
                1L,                  // userID
                liga1ID,            // leagueID
                false,              // isprivate
                0,                  // corrScorePoints
                0,                  // corrGoalPoints
                0,                  // corrWinnerPoints
                "password",         // password
                false,          // important here: isResetted
                1L                   //mychatID
        );
        bettingRoundRepository.save(bettingRound1);

        BettingRound bettingRound2 = new BettingRound(
                "BettingRound2",    // name
                1L,                 // userID
                liga2ID,            // leagueID
                false,              // isprivate
                0,                  // corrScorePoints
                0,                  // corrGoalPoints
                0,                  // corrWinnerPoints
                "password",         // password
                false,          // important here: isResetted
                2L                   //mychatID
        );
        bettingRoundRepository.save(bettingRound2);


        // now execute function
        BetsController betsController = new BetsController(
                betsRepository,
                ligaRepository,
                aliasRepository,
                userRepository,
                dateRepository,
                bettingRoundRepository
        );

        betsController.putByBettingRound(liga1ID);

        // get BettingRounds

        BettingRound trueBR = new BettingRound();
        BettingRound falseBR = new BettingRound();

        List<BettingRound> bettingRoundList = bettingRoundRepository.findAll();

        for(BettingRound bettingRound : bettingRoundList) {
            if(bettingRound.getLigaID() == liga1ID) {
                trueBR = bettingRound;
            }
            else {
                falseBR = bettingRound;
            }
        }

        Assertions.assertEquals(true, trueBR.getIsResetted());
        Assertions.assertEquals(false, falseBR.getIsResetted());

    }


}
