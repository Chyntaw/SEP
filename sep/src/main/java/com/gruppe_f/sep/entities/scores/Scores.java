package com.gruppe_f.sep.entities.scores;

import javax.persistence.*;

@Entity

public class Scores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="scoressid")
    private Long id;

    @Column(name="scores")
    private String scores;
    //private User user;

    public Scores(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }
}
