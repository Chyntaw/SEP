package com.gruppe_f.sep.entities.BettingRound;

import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.scores.Scores;
import com.gruppe_f.sep.entities.user.User;

import javax.persistence.*;
import java.util.List;

@Entity


public class BettingRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bettingroundid")
    private Long id;

    @Column(name="name")
    private String name;

    @OneToOne
    private User user;

    private boolean isprivate;


     //private List<Scores> scoresList;
     private float correctscore;
     private float correctgoaldifferent;
     private float correctwinner;

     @OneToOne
     private Liga liga;

     public BettingRound(){

     }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isIsprivate() {
        return isprivate;
    }

    public void setIsprivate(boolean isprivate) {
        this.isprivate = isprivate;
    }

    public float getCorrectscore() {
        return correctscore;
    }

    public void setCorrectscore(float correctscore) {
        this.correctscore = correctscore;
    }

    public float getCorrectgoaldifferent() {
        return correctgoaldifferent;
    }

    public void setCorrectgoaldifferent(float correctgoaldifferent) {
        this.correctgoaldifferent = correctgoaldifferent;
    }

    public float getCorrectwinner() {
        return correctwinner;
    }

    public void setCorrectwinner(float correctwinner) {
        this.correctwinner = correctwinner;
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }
}
