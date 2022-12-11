package com.gruppe_f.sep.entities.table;

import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableEntry {

    @Transient
    private final int siegPunkte = 3;
    @Transient
    private final int unentschiedenPunkte = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String team;

    private int anzahlSpiele;

    private int siege;

    private int unentschieden;

    private int verloren;

    private int toreGeschossen;

    private int toreKassiert;

    private int diff;

    private int punkte;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAnzahlSpiele() {
        return anzahlSpiele;
    }

    public void setAnzahlSpiele(int anzahlSpiele) {
        this.anzahlSpiele = anzahlSpiele;
    }

    public int getSiege() {
        return siege;
    }

    public void setSiege(int siege) {
        this.siege = siege;
    }

    public int getUnentschieden() {
        return unentschieden;
    }

    public void setUnentschieden(int unentschieden) {
        this.unentschieden = unentschieden;
    }

    public int getVerloren() {
        return verloren;
    }

    public void setVerloren(int verloren) {
        this.verloren = verloren;
    }

    public int getToreGeschossen() {
        return toreGeschossen;
    }

    public void setToreGeschossen(int toreGeschossen) {
        this.toreGeschossen = toreGeschossen;
    }

    public int getToreKassiert() {
        return toreKassiert;
    }

    public void setToreKassiert(int toreKassiert) {
        this.toreKassiert = toreKassiert;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public int getPunkte() {
        return punkte;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public int getSiegPunkte() {
        return siegPunkte;
    }

    public int getUnentschiedenPunkte() {
        return unentschiedenPunkte;
    }

    public TableEntry(){};

    public TableEntry(int anzahlSpiele, int siege, int unentschieden, int verloren, int toreGeschossen, int toreKassiert, int diff, int punkte) {
        this.anzahlSpiele = anzahlSpiele;
        this.siege = siege;
        this.unentschieden = unentschieden;
        this.verloren = verloren;
        this.toreGeschossen = toreGeschossen;
        this.toreKassiert = toreKassiert;
        this.diff = diff;
        this.punkte = punkte;
    }

    public TableEntry(String team, int anzahlSpiele, int toreGeschossen, int toreKassiert) {
        this.team = team;
        this.anzahlSpiele = anzahlSpiele;
        this.toreGeschossen = toreGeschossen;
        this.toreKassiert = toreKassiert;
    }
}
