package com.gruppe_f.sep.entities.liga;



import com.gruppe_f.sep.entities.leagueData.LeagueData;
import org.hibernate.boot.model.relational.Sequence;

import javax.persistence.*;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class Liga {
    @Id
    @SequenceGenerator(name="Liga",sequenceName = "liga_seq")
    @GeneratedValue(strategy = SEQUENCE, generator = "Liga")
    @Column(name = "LigaID", nullable = false)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "liga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeagueData> leagueData = new ArrayList<>();

//    private Spielplan spielplan;

    private File ligaPicture;

    protected Liga() {}

    public Liga(String name){
        this.name = name;
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

    public List<LeagueData> getLeagueData() {
        return leagueData;
    }

    public void setLeagueData(List<LeagueData> leagueData) {
        this.leagueData.addAll(leagueData);
    }
}
