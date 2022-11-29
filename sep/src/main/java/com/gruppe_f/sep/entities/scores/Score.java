package com.gruppe_f.sep.entities.scores;

import com.gruppe_f.sep.entities.user.User;

import javax.persistence.*;

@Entity

public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="scoressid")
    private Long id;


    private int score;

    private Long userid;

    @OneToOne
    private User user;

    public Score(){}

    public Score(int score, User user){this.score = score; this.user = user;}




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScores() {
        return score;
    }

    public void setScores(int score) {
        this.score = score;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
