package com.gruppe_f.sep.entities.BettingRound;


import com.gruppe_f.sep.entities.alias.AliasRepository;
import com.gruppe_f.sep.entities.bets.Bets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BettingRoundRepository extends JpaRepository<BettingRound,Long> {


}
