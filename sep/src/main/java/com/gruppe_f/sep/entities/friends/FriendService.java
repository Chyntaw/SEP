package com.gruppe_f.sep.entities.friends;

import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FriendService {


    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public void sendFriendRequest(String currentUserEmail, String friendUserEmail, boolean pending){             //user = eingeloggter User -- id = freund zum adden

        User currentUser = userRepository.findUserByeMail(currentUserEmail);
        User friendUser = userRepository.findUserByeMail(friendUserEmail);

        Friend friend = new Friend();

        User firstUser = currentUser;
        User secondUser = friendUser;

        if(firstUser.getId() != secondUser.getId()){
            if(currentUser.getId() > friendUser.getId()){
                firstUser = friendUser;
                secondUser = currentUser;
            }
            if(!friendRepository.existsByFirstUserAndSecondUser(firstUser, secondUser)){
                friend.setFirstUser(firstUser);
                friend.setSecondUser(secondUser);
                friend.setPending(pending);
                friend.setSendedFrom(userRepository.findUserByeMail(currentUserEmail));
                friendRepository.save(friend);
            }
        }
    }

    public boolean isPending(String currentUserEmail, String freindUserEmail){

        User currentUser = userRepository.findUserByeMail(currentUserEmail);
        User friendUser = userRepository.findUserByeMail(freindUserEmail);

        Friend friend;

        User firstUser = currentUser;
        User secondUser = friendUser;

        if(firstUser.getId() != secondUser.getId()){
            if(currentUser.getId() > friendUser.getId()){
                firstUser = friendUser;
                secondUser = currentUser;
            }
            if(friendRepository.existsByFirstUserAndSecondUser(firstUser, secondUser)){

                friend = friendRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
                 return friend.isPending();
            }
        }
        return false;
    }
    public void acceptFriend(String currentUserEmail, String friendUserEmail){
        User currentUser = userRepository.findUserByeMail(currentUserEmail);
        User friendUser = userRepository.findUserByeMail(friendUserEmail);

        Friend friend;

        User firstUser = currentUser;
        User secondUser = friendUser;

        if(firstUser.getId() != secondUser.getId()){
            if(currentUser.getId() > friendUser.getId()){
                firstUser = friendUser;
                secondUser = currentUser;
            }
            if(friendRepository.existsByFirstUserAndSecondUser(firstUser, secondUser)){

                friend = friendRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
                if(friend.getSendedFrom() != currentUser){
                    friend.setPending(false);
                    friendRepository.save(friend);
                }
            }
        }
    }
    public void declineFriend(String currentUserEmail, String friendUserEmail){

        User currentUser = userRepository.findUserByeMail(currentUserEmail);
        User friendUser = userRepository.findUserByeMail(friendUserEmail);

        Friend friend;

        User firstUser = currentUser;
        User secondUser = friendUser;

        if(firstUser.getId() != secondUser.getId()){
            if(currentUser.getId() > friendUser.getId()){
                firstUser = friendUser;
                secondUser = currentUser;
            }
            if(friendRepository.existsByFirstUserAndSecondUser(firstUser, secondUser)){
                friend = friendRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
                friendRepository.delete(friend);
            }
        }
    }


    public List<User> getPendingFriends(User currentUser){         //get pending friends

        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            if(friend.isPending()){
                if(friend.getSendedFrom() != currentUser){
                    friendUsers.add(userRepository.findUserByeMail(friend.getSecondUser().geteMail()));
                }
            }
        }
        for (Friend friend : friendsBySecondUser) {
            if(friend.isPending()){
                if(friend.getSendedFrom() != currentUser){
                    friendUsers.add(userRepository.findUserByeMail(friend.getFirstUser().geteMail()));
                }
            }
        }
        return friendUsers;
    }
    public List<User> getPendingRequestedFriends(User currentUser){         //get pending friends

        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            if(friend.isPending()){
                if(friend.getSendedFrom() == currentUser){
                    friendUsers.add(userRepository.findUserByeMail(friend.getSecondUser().geteMail()));
                }
            }
        }
        for (Friend friend : friendsBySecondUser) {
            if(friend.isPending()){
                if(friend.getSendedFrom() == currentUser){
                    friendUsers.add(userRepository.findUserByeMail(friend.getFirstUser().geteMail()));
                }
            }
        }
        return friendUsers;
    }
    public List<User> getFriends(User currentUser){         //get accepted friends

        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            if(!friend.isPending()){
                friendUsers.add(userRepository.findUserByeMail(friend.getSecondUser().geteMail()));
            }
        }
        for (Friend friend : friendsBySecondUser) {
            if(!friend.isPending()){
                friendUsers.add(userRepository.findUserByeMail(friend.getFirstUser().geteMail()));
            }
        }
        return friendUsers;
    }

    public List<User> getFriendsByUserID(Long userID){         //get accepted friends

        User user = userRepository.findById(userID).get();

        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(user);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(user);
        List<User> friendUsers = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            if(!friend.isPending()){
                friendUsers.add(userRepository.findUserByeMail(friend.getSecondUser().geteMail()));
            }
        }
        for (Friend friend : friendsBySecondUser) {
            if(!friend.isPending()){
                friendUsers.add(userRepository.findUserByeMail(friend.getFirstUser().geteMail()));
            }
        }
        return friendUsers;
    }



    public void unfriend(String currentUserEmail, String friendUserEmail) {
        User currentUser = userRepository.findUserByeMail(currentUserEmail);
        User friendUser = userRepository.findUserByeMail(friendUserEmail);

        Friend friend;

        User firstUser = currentUser;
        User secondUser = friendUser;

        if(firstUser.getId() != secondUser.getId()){
            if(currentUser.getId() > friendUser.getId()){
                firstUser = friendUser;
                secondUser = currentUser;
            }
            if(friendRepository.existsByFirstUserAndSecondUser(firstUser, secondUser)){
                friend = friendRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
                friendRepository.delete(friend);
            }
        }
    }



}
