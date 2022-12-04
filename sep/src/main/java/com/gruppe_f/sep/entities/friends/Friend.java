package com.gruppe_f.sep.entities.friends;

import com.gruppe_f.sep.entities.user.User;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "first_user_id", referencedColumnName = "userID")
    User firstUser;

    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "second_user_id", referencedColumnName = "userID")
    User secondUser;

    @Column(name = "pending")
    boolean pending;

    @OneToOne
    @JoinColumn(name = "sendedFrom", referencedColumnName = "userID")
    User sendedFrom;

    public Friend() {
    }

    public Friend(User user, User user2, boolean b) {
        this.firstUser = user;
        this.secondUser = user2;
        this.pending = b;
    }
    public User getSendedFrom() {
        return sendedFrom;
    }

    public void setSendedFrom(User sendedFrom) {
        this.sendedFrom = sendedFrom;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }


    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
