package com.gruppe_f.sep.entities.Message;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name="Message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String message;
    private Long userID;

    private LocalTime localTime;

    private String name;
    public Message(String message, Long userID, LocalTime localTime, String name) {
        this.message = message;
        this.userID = userID;
        this.localTime = localTime;
        this.name = name;
    }

    public Message() {

    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
