package com.gruppe_f.sep.entities.wetten;

import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.user.User;

import javax.persistence.*;

@Entity
@Table(name = "wetten")
public class Wetten {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;


    private int leagueDataID;


    private Long userID;

    @Column(name = "isResolved")
    private boolean isResolved;     //true = Wette in der Vergangeheit, false = Wette in der Zukunft


    public Wetten() {

    }
    public Wetten(int leagueDataID, Long userID, double qoute, double einsatz, boolean isResolved) {
        this.leagueDataID = leagueDataID;
        this.userID = userID;
        this.qoute = qoute;
        this.einsatz = einsatz;
        this.isResolved = isResolved;
    }

    private double qoute;

    private double einsatz;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLeagueDataID() {
        return leagueDataID;
    }

    public void setLeagueDataID(int leagueDataID) {
        this.leagueDataID = leagueDataID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public double getQoute() {
        return qoute;
    }

    public void setQoute(double qoute) {
        this.qoute = qoute;
    }

    public double getEinsatz() {
        return einsatz;
    }

    public void setEinsatz(double einsatz) {
        this.einsatz = einsatz;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }



}
