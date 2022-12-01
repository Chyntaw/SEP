package com.gruppe_f.sep.entities.alias;

import com.gruppe_f.sep.entities.user.User;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;

@Entity

public class Alias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="aliasid")
    private Long id;
    @Column(name="alias")
    private String alias;

    private Long userID;

    public Alias(){}

    public Alias(String alias, Long userID) {
        this.alias = alias;
        this.userID = userID;
    }

    public Long getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
