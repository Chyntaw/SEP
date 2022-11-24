package com.gruppe_f.sep.entities.bets;

import javax.persistence.*;

@Entity

public class Bets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "betsid")
    private Long id;

    @Column(name = "bets")
    private String bets;
    //private User user;

    public Bets(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBets() {
        return bets;
    }

    public void setBets(String bets) {
        this.bets = bets;
    }
}