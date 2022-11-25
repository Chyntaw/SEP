package com.gruppe_f.sep.entities.BettingRound;

import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.scores.Scores;
import com.gruppe_f.sep.entities.user.User;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity


public class BettingRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bettingroundid")
    private Long id;

    private String name;

    private Long ownerID;

    private boolean isPrivate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bettingroundid")
    private List<Scores> scoresList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bettingroundid")
    private List<Bets> betsList = new LinkedList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "participants",
            joinColumns = @JoinColumn(name = "bettingRoundid", referencedColumnName = "bettingRoundid"),
            inverseJoinColumns = @JoinColumn(name = "userID",
                    referencedColumnName = "userID"))
    private List<User> participants = new LinkedList<>();


    private int corrScorePoints;
    private int corrGoalPoints;
    private int corrWinnerPoints;


    private Long ligaID;
    private String password;

    public BettingRound() {}

    public BettingRound(String name, Long ownerID, Long ligaID, boolean isprivate,
                        int corrScorePoints, int corrGoalPoints, int corrWinnerPoints, String password) {
        this.name = name;
        this.ownerID = ownerID;
        this.ligaID = ligaID;
        this.isPrivate = isprivate;
        this.corrScorePoints = corrScorePoints;
        this.corrGoalPoints = corrGoalPoints;
        this.corrWinnerPoints = corrWinnerPoints;
        this.password = password;
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

    public boolean isIsprivate() {
        return isPrivate;
    }

    public void setIsprivate(boolean isprivate) {
        this.isPrivate = isprivate;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public int getCorrScorePoints() {
        return corrScorePoints;
    }

    public void setCorrScorePoints(int corrScorePoints) {
        this.corrScorePoints = corrScorePoints;
    }

    public int getCorrGoalPoints() {
        return corrGoalPoints;
    }

    public void setCorrGoalPoints(int corrGoalPoints) {
        this.corrGoalPoints = corrGoalPoints;
    }

    public int getCorrWinnerPoints() {
        return corrWinnerPoints;
    }

    public void setCorrWinnerPoints(int corrWinnerPoints) {
        this.corrWinnerPoints = corrWinnerPoints;
    }

    public Long getLigaID() {
        return ligaID;
    }

    public void setLigaID(Long ligaID) {
        this.ligaID = ligaID;
    }

    public List<Scores> getScoresList() {
        return scoresList;
    }

    public void setScoresList(List<Scores> scoresList) {
        this.scoresList = scoresList;
    }

    public List<Bets> getBetsList() {
        return betsList;
    }

    public void setBetsList(List<Bets> betsList) {
        this.betsList = betsList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}
