package com.gruppe_f.sep.entities.leagueData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gruppe_f.sep.entities.liga.Liga;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class LeagueData {

    @Id
    @SequenceGenerator(name="LigaData",sequenceName = "ligadata_seq")
    @GeneratedValue(strategy = SEQUENCE, generator = "LigaData")
    @Column(name = "LeagueDataID")
    private int id;
    private int matchDay;
    private String player1;
    private String player2;
    private String result;
    private String date;

    private Long ligaID;

    public  LeagueData() {};


    //Optional: League without leaguePicture
    public LeagueData(int matchDay, String player1, String player2, String result) {
        this.matchDay = matchDay;
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
    }

    public LeagueData(int matchDay, String player1, String player2, String result, Long ligaID) {
        this.matchDay = matchDay;
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.ligaID = ligaID;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(int matchDay) {
        this.matchDay = matchDay;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getLigaID() {
        return ligaID;
    }

    public void setLigaID(Long ligaID) {
        this.ligaID = ligaID;
    }
}

