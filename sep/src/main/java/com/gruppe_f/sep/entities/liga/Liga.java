package com.gruppe_f.sep.entities.liga;



import com.gruppe_f.sep.entities.leagueData.LeagueData;

import javax.persistence.*;
import java.io.File;
import java.util.List;

@Entity
public class Liga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LigaID", nullable = false)
    private Long id;
    private String name;


//    private Spielplan spielplan;

    private File ligaPicture;

    public Liga(){

    }

    public Liga(String name){
        this.name = name;
    }











    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
