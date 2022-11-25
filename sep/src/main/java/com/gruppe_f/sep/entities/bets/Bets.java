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


    //private User user;


    public Bets(){}

    public Bets(String bet){
        this.bet = bet;
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


}