package com.gruppe_f.sep.entities.chat;

import com.gruppe_f.sep.entities.BettingRound.BettingRoundRepository;
import com.gruppe_f.sep.entities.Message.Message;
import com.gruppe_f.sep.entities.Message.MessageRepository;
import com.gruppe_f.sep.entities.user.User;
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

        User firstUser = userRepository.findUserById(userID);
        User secondUser = userRepository.findUserById(friendID);

        Chat chat;

        String[] firstUserChatIDs = firstUser.getMyChatIDs().split("-");
        String[] secondUserChatIDs = secondUser.getMyChatIDs().split("-");

        for(String firstUserChatIDS : firstUserChatIDs){
            for(String secondUserChatIDS: secondUserChatIDs){
                if(firstUserChatIDS.equals(secondUserChatIDS)){
                    chat = chatRepository.findById(Long.parseLong(firstUserChatIDS)).get();
                    List<Message> oldMessages = chat.getMessages();

                    Message newMessage = new Message(message, userID);
                    messageRepository.save(newMessage);

                    oldMessages.add(newMessage);
                    chat.setMessages(oldMessages);
                    chatRepository.save(chat);
                }
            }
        }
    }


    public Chat getMeessages(Long userID, Long friendID){

        User firstUser = userRepository.findUserById(userID);
        User secondUser = userRepository.findUserById(friendID);

        Chat chat;

        String[] firstUserChatIDs = firstUser.getMyChatIDs().split("-");
        String[] secondUserChatIDs = secondUser.getMyChatIDs().split("-");

        for(String firstUserChatID : firstUserChatIDs){
            for(String secondUserChatID: secondUserChatIDs){
                if(firstUserChatID.equals(secondUserChatID)){
                    System.out.println(firstUserChatID + " " + secondUserChatID);
                    chat = chatRepository.findById(Long.parseLong(firstUserChatID)).get();

                    return chat;
                }
            }
        }

        return null;
    }




/*
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



 */



}
