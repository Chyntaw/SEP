package com.gruppe_f.sep;

import com.gruppe_f.sep.entities.friends.Friend;
import com.gruppe_f.sep.entities.friends.FriendRepository;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FriendServiceTest {


    @Test
    void contextLoads(){
    }

    @Autowired
    FriendRepository friendRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("Test should pass, if FriendRequest is sended")
    void shouldSendFriendReqeust(){

        User user1 = new User("Vorname1","Nachname1","02.02.2002", "test@gmx.de", "1", "BASIC");
        User user2 = new User("Vorname2","Nachname2","02.02.2002", "test2@gmx.de", "1", "BASIC");

        userRepository.save(user1);
        userRepository.save(user2);

        Friend friend = new Friend(user1, user2, true, user1);

        friendRepository.save(friend);
        Assertions.assertNotNull(friendRepository.findByFirstUserAndSecondUser(userRepository.findUserByeMail("test@gmx.de"), userRepository.findUserByeMail("test2@gmx.de")));
    }
}