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
    //private User user;

    public Alias(){}
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

}
