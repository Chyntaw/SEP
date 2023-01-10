package com.gruppe_f.sep.businesslogic;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class QouteRechner {

    private DateRepository dateRepository;
    private LeagueDataRepository leagueDataRepository;

    @Autowired
    public QouteRechner(DateRepository dateRepository, LeagueDataRepository leagueDataRepository) {
        this.dateRepository = dateRepository;
        this.leagueDataRepository = leagueDataRepository;
    }

    /*
        alle spiele zusammen zählen (egal ob gleiche Liga/Saision)
        siege/unentschieden/niederlage zusammen zählen
        durch alle spiele

        Beispiel:
        Bayern                      BVB
        50 spiele                   40 Spiele
        40 siege                    20 siege
        8 niederlagen               5 niederlagen
        2 unentschieden             15 unentschieden

        qoute:
        sieg 50/40=1,25             sieg 40/20 = 2
        niederlage 50/8=6,25        niederlage 40/5 = 8
        unentschieden 50/2=25       unentschieden 40/15 = 2,667

        Sieg kleinre Qoute
        Niederlage Höhere Qoute
        Unentschieden Qouten addieren / 2

        bayern		X			BVB
        1,25		25 + 2,667			6,25
                    27,67
                    /2 = 13,82
     */



    public double[] qouteBerechnen(String player1, String player2, String date){

        // get all data from database
        SystemDate systemDate = dateRepository.findAll().get(0);
        List<LeagueData> all_league_data = leagueDataRepository.findAll();


        double[] oddsPlayer1;
        double[] oddsPlayer2;

        double[] oddsGame = {0, 0, 0};;

        List<LeagueData> allMatchesPlayer1 = new ArrayList<>();
        List<LeagueData> allMatchesPlayer2 = new ArrayList<>();


        //ALLE Spiele von Mannschaft 1
        for(LeagueData leagueData: leagueDataRepository.findAll()){
            if(             /* (GenerellLogisch.compareDates(systemDate.getLocalDate(), leagueData.getDate()) > 0) && */
                    (leagueData.getPlayer1().equals(player1) || leagueData.getPlayer2().equals(player1)) &&
                    (GenerellLogisch.compareDates(date, leagueData.getDate()) > 0)){
                allMatchesPlayer1.add(leagueData);
            }
        }

        //ALLE Spiele von Mannschaft 2
        for(LeagueData leagueData: leagueDataRepository.findAll()){
            if(                 /* (GenerellLogisch.compareDates(systemDate.getLocalDate(), leagueData.getDate()) > 0) && */

                    (leagueData.getPlayer1().equals(player2) || leagueData.getPlayer2().equals(player2)) &&
                    (GenerellLogisch.compareDates(date, leagueData.getDate()) > 0)){
                allMatchesPlayer2.add(leagueData);
            }
        }

        if(allMatchesPlayer1.size() > 3  && allMatchesPlayer2.size() > 3){
            oddsPlayer1 = countMatchInfos(allMatchesPlayer1, player1);
            oddsPlayer2 = countMatchInfos(allMatchesPlayer2, player2);

            if(oddsPlayer1[0] < oddsPlayer2[0]){
                oddsGame[0] = oddsPlayer1[0];
            }
            else{
                oddsGame[0] = oddsPlayer2[0];
            }
            if(oddsPlayer1[1] > oddsPlayer2[1]){
                oddsGame[1] = oddsPlayer1[1];
            }
            else{
                oddsGame[1] = oddsPlayer2[1];
            }
            oddsGame[2] = (oddsPlayer1[2] + oddsPlayer2[2]) / 2;

        }
        else{
            oddsGame = null;
        }
        return oddsGame;
    }


    /*  input is list for calculation and the team to look for
        returns sum of matches in array with 3 values:
        first value: odds to win
        second value: odds to loose
        third value: odds to draw */
    private double[] countMatchInfos(List<LeagueData> toCalculateList, String player) {
        double[] result = {0, 0, 0};

        int wins = 0;
        int looses = 0;
        int draws = 0;
        int numberOfGames = 0;

        // go through list for data
        for(LeagueData x : toCalculateList) {
            // check if first player is given player
            if(player.equals(x.getPlayer1())) {

                if(Integer.parseInt(x.getResult().split("-")[0]) > Integer.parseInt(x.getResult().split("-")[1])){
                    wins++;
                    numberOfGames++;
                }
                else if(Integer.parseInt(x.getResult().split("-")[0]) < Integer.parseInt(x.getResult().split("-")[1])){
                    looses++;
                    numberOfGames++;
                }else{
                    draws++;
                    numberOfGames++;
                }
            }
            // if first player is not given player it must be second player
            else {
                if(Integer.parseInt(x.getResult().split("-")[1]) > Integer.parseInt(x.getResult().split("-")[0])){
                    wins++;
                    numberOfGames++;
                }
                else if(Integer.parseInt(x.getResult().split("-")[1]) < Integer.parseInt(x.getResult().split("-")[0])){
                    looses++;
                    numberOfGames++;
                }else{
                    draws++;
                    numberOfGames++;
                }
            }
        }


        if(wins != 0){
            if(numberOfGames/wins == 1){
                result[0] = 1.2;
            }
            else{
                result[0] = numberOfGames/wins;
            }
        }
        else{
            if(numberOfGames == 1){
                result[0] = 1.2;
            }
            else{
                result[0] = numberOfGames;
            }
        }
        if(looses != 0){
            if(numberOfGames/looses == 1){
                result[1] = 1.2;
            }
            else{
                result[1] = numberOfGames/looses;
            }

        }else{
            if(numberOfGames == 1){
                result[1] = 1.2;
            }
            else{
                result[1] = numberOfGames;
            }

        }
        if(draws != 0){
            if(numberOfGames/draws == 1){
                result[2] = 1.2;
            }
            else{
                result[2] = numberOfGames/draws;
            }
        }else{
            if(numberOfGames == 1){
                result[2] = 1.2;
            }
            else{
                result[2] = numberOfGames;
            }

        }
        return result;

    }
}
