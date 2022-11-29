package com.gruppe_f.sep.entities.bets;

import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.leagueData.LeagueData;

import javax.persistence.*;

@Entity

public class Bets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "betsid")
    private Long id;

    @Column(name = "bet")
    private String bet;

    private Long userID;

    @OneToOne
    private LeagueData leagueData;

    private int score;


    public Bets(){}

    public Bets(String bet, Long userID, LeagueData data){
        this.bet = bet;
        this.userID = userID;
        this.leagueData = data;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBets() {
        return bet;
    }

    public void setBets(String bets) {
        this.bet = bets;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LeagueData getLeagueData() {
        return leagueData;
    }

    public void setLeagueData(LeagueData leagueData) {
        this.leagueData = leagueData;
    }
}