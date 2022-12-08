package com.gruppe_f.sep;

import com.gruppe_f.sep.entities.friends.Friend;
import com.gruppe_f.sep.entities.friends.FriendRepository;
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
        //Friend friend = new Friend(userRepository.findUserById(1L), userRepository.findUserById(2L), true);
        //friendRepository.save(friend);
        Assertions.assertNotNull(friendRepository.findByFirstUserAndSecondUser(userRepository.findUserById(1L), userRepository.findUserById(2L)));
    }
}