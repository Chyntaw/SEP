package com.gruppe_f.sep.entities.chat;

import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final UserRepository userRepository;
    private final ChatService chatService;

    @Autowired
    public ChatController(UserRepository userRepository,
                          ChatService chatService){
        this.userRepository = userRepository;
        this.chatService = chatService;
    }

}
