package com.gruppe_f.sep.entities.friends;

import com.gruppe_f.sep.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean existsByFirstUserAndSecondUser(User first, User second);
    List<Friend> findByFirstUser(User user);
    List<Friend> findBySecondUser(User user);


    Friend findByFirstUserAndSecondUser(User first, User second);
}
