package com.gruppe_f.sep.entities.chat;

import com.gruppe_f.sep.entities.Message.Message;
import com.gruppe_f.sep.entities.user.User;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany
    private List<Message> messages = new LinkedList<>();

    private boolean isGroupChat;


    public Chat() {
    }

    public Chat(boolean isGroupChat, List<Message> messages) {
        this.isGroupChat = isGroupChat;
        this.messages = messages;
    }


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
