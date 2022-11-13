package com.gruppe_f.sep.date;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "date")
public class SystemDate implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "localDate")
    private String localSystemDate;

    public SystemDate(String localDate) {
        this.localSystemDate = localDate;
    }

    public SystemDate() {

    }

    @Override
    public String toString() {
        return "SystemDate{" +
                "id=" + id +
                ", localSystemDate=" + localSystemDate.toString() +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLocalDate(String localDate) {
        this.localSystemDate = localDate;
    }

    public String getLocalDate() {
        return localSystemDate;
    }
}