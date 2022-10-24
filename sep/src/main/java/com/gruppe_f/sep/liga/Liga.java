package com.gruppe_f.sep.liga;

import com.gruppe_f.sep.Spielplan.Spielplan;

import javax.persistence.*;
import java.io.File;

@Entity
public class Liga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;


//    private Spielplan spielplan;

    private File ligaPicture;

    public Liga(){

    }

    public Liga(long id, String name, Spielplan spielplan){
        this.id = id;
        this.name = name;
    }











    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
