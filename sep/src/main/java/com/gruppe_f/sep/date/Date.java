package com.gruppe_f.sep.date;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "date")
public class Date implements Serializable {
    @Id
    @Column(name = "localDate", nullable = false)
    private LocalDate localDate;

    public Date(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Date() {

    }
}