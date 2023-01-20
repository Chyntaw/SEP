package com.gruppe_f.sep.entities.Message;

import javax.persistence.*;

@Entity
@Table(name="Message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String message;
    private Long userID;


    public Message(String message, Long userID) {
        this.message = message;
        this.userID = userID;
    }

    public Message() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
