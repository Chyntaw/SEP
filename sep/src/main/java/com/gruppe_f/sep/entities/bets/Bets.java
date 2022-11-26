package com.gruppe_f.sep.entities.bets;

import com.gruppe_f.sep.entities.BettingRound.BettingRound;

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
    private Long leagueDataid;

    private int score;


    public Bets(){}

    public Bets(String bet, Long userID, Long leagueDataid){
        this.bet = bet;
        this.userID = userID;
        this.leagueDataid = leagueDataid;
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

    public Long getLeagueDataid() {
        return leagueDataid;
    }

    public void setLeagueDataid(Long leagueDataid) {
        this.leagueDataid = leagueDataid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}