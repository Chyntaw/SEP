package com.gruppe_f.sep.entities.chat;

import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.BettingRound.BettingRoundRepository;
import com.gruppe_f.sep.entities.Message.Message;
import com.gruppe_f.sep.entities.Message.MessageRepository;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalTime;
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

        LocalTime time = LocalTime.now();


        for(String firstUserChatIDS : firstUserChatIDs){
            for(String secondUserChatIDS: secondUserChatIDs){
                if(firstUserChatIDS.equals(secondUserChatIDS)){
                    chat = chatRepository.findById(Long.parseLong(firstUserChatIDS)).get();
                    if(!chat.isGroupChat()){
                        List<Message> oldMessages = chat.getMessages();

                        String name = firstUser.getLastName() + ", " + firstUser.getFirstName();
                        Message newMessage = new Message(message, userID, time, name);
                        messageRepository.save(newMessage);

                        oldMessages.add(newMessage);
                        chat.setMessages(oldMessages);
                        chatRepository.save(chat);
                    }
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
                    if(!chatRepository.findById(Long.parseLong(firstUserChatID)).get().isGroupChat()){
                        chat = chatRepository.findById(Long.parseLong(firstUserChatID)).get();

                        return chat;
                    }
                }
            }
        }

        return null;
    }





    public void saveGroupMessage(Long userID, Long tipprundenID, String message){

        User firstUser = userRepository.findUserById(userID);
        BettingRound bettingRound = bettingRoundRepository.getById(tipprundenID);

        Chat chat;

        String[] firstUserChatIDs = firstUser.getMyChatIDs().split("-");

        LocalTime time = LocalTime.now();

        for(String firstUserChatIDS : firstUserChatIDs){
            if(firstUserChatIDS.equals(bettingRound.getMyChatID().toString())){
                chat = chatRepository.findById(Long.parseLong(firstUserChatIDS)).get();
                if(chat.isGroupChat()){
                    List<Message> oldMessages = chat.getMessages();

                    String name = firstUser.getLastName() + ", " + firstUser.getFirstName();
                    Message newMessage = new Message(message, userID, time, name);
                    messageRepository.save(newMessage);

                    oldMessages.add(newMessage);
                    chat.setMessages(oldMessages);
                    chatRepository.save(chat);
                }

            }
        }
    }

    public Chat getGroupMessage(Long userID, Long tipprundenID){
        User firstUser = userRepository.findUserById(userID);
        BettingRound bettingRound = bettingRoundRepository.getById(tipprundenID);

        Chat chat;

        String[] firstUserChatIDs = firstUser.getMyChatIDs().split("-");

        return chatRepository.findById(bettingRound.getMyChatID()).get();

        /*
        for(String firstUserChatIDS : firstUserChatIDs){
            if(firstUserChatIDS.equals(bettingRound.getMyChatID().toString())){
                if(chatRepository.findById(Long.parseLong(firstUserChatIDS)).get().isGroupChat()){
                    chat = chatRepository.findById(Long.parseLong(firstUserChatIDS)).get();

                    return chat;
                }
            }
        }
        return null;
         */

    }







}
