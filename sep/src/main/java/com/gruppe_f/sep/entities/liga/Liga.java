package com.gruppe_f.sep.entities.liga;



import com.gruppe_f.sep.entities.leagueData.LeagueData;

import javax.persistence.*;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Liga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LigaID", nullable = false)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "liga",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<LeagueData> leagueData = new ArrayList<>();

//    private Spielplan spielplan;

    private File ligaPicture;

    public Liga(){

    }

    public Liga(String name, List leagueData){
        this.name = name;
        this.leagueData = leagueData;
    }











    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
