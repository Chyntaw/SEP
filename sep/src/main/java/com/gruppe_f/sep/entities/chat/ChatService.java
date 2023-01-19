package com.gruppe_f.sep.entities.chat;

import com.gruppe_f.sep.entities.BettingRound.BettingRoundRepository;
import com.gruppe_f.sep.entities.Message.Message;
import com.gruppe_f.sep.entities.Message.MessageRepository;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    private BettingRoundRepository bettingRoundRepository;

    @Autowired
    public ChatService(UserRepository userRepository,
                       ChatRepository chatRepository,
                       MessageRepository messageRepository,
                       BettingRoundRepository bettingRoundRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.bettingRoundRepository = bettingRoundRepository;
    }


    public void saveMessage(Long userID, Long friendID, String message){


        Chat chat = chatRepository.findByFirstUserAndSecondUser(userRepository.findUserById(userID), userRepository.findUserById(friendID));

        List<Message> oldMessages = chat.getMessages();

        Message newMessage = new Message(message, userID);
        messageRepository.save(newMessage);

        oldMessages.add(newMessage);
        chat.setMessages(oldMessages);
        chatRepository.save(chat);

    }

    public List<Message> getMeessages(Long userID, Long friendID){
        Chat chat = chatRepository.findByFirstUserAndSecondUser(userRepository.findUserById(userID), userRepository.findUserById(friendID));

        return chat.getMessages();
    }



    public void saveGroupMessage(Long userID, Long tipprundenID, String message){

        Chat chat = chatRepository.findByUserAndTipprunde(userRepository.findUserById(userID), bettingRoundRepository.findById(tipprundenID).get());

        List<Message> oldMessages = chat.getMessages();

        Message newMessage = new Message(message, userID);
        messageRepository.save(newMessage);

        oldMessages.add(newMessage);
        chat.setMessages(oldMessages);
        chatRepository.save(chat);
    }

    public List<Message> getGroupMessage(Long userID, Long tipprundenID){
        Chat chat = chatRepository.findByUserAndTipprunde(userRepository.findUserById(userID), bettingRoundRepository.findById(tipprundenID).get());

        return chat.getMessages();
    }



}
