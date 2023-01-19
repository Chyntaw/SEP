package com.gruppe_f.sep.entities.chat;

import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findByFirstUserAndSecondUser(User first, User second);
    Chat findByUserAndTipprunde(User first, BettingRound bettingRound);

}
