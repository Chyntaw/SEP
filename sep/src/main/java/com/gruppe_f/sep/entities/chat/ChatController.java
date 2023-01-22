package com.gruppe_f.sep.entities.chat;

import com.gruppe_f.sep.entities.Message.Message;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/chat/postMessage/{userID}/{friendID}")
    public ResponseEntity<?> saveMessage(@PathVariable("userID") Long userID,
                                         @PathVariable("friendID") Long friendID,
                                         @RequestParam("message") String message){

        chatService.saveMessage(userID, friendID, message);



        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chat/getMessage/{userID}/{friendID}")
    public ResponseEntity<Chat> getMessages(@PathVariable("userID") Long userID,
                                                     @PathVariable("friendID") Long friendID){

        return new ResponseEntity<>(chatService.getMeessages(userID, friendID), HttpStatus.OK);
    }




    @PostMapping("/chat/postGroupMessage/{userID}/{tipprundenID}")
    public ResponseEntity<?> saveGroupMessage(@PathVariable("userID") Long userID,
                                              @PathVariable("tipprundenID") Long tipprundenID,
                                              @RequestParam("message") String message){

        chatService.saveGroupMessage(userID, tipprundenID, message);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chat/getGroupMessage/{userID}/{friendID}")
    public ResponseEntity<Chat> getGroupMessages(@PathVariable("userID") Long userID,
                                                          @PathVariable("tipprundenID") Long tipprundenID){

        return new ResponseEntity<>(chatService.getGroupMessage(userID, tipprundenID), HttpStatus.OK);
    }






}
