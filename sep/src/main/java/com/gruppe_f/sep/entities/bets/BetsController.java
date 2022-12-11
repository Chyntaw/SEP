package com.gruppe_f.sep.entities.bets;

import com.gruppe_f.sep.businesslogic.GenerellLogisch;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.BettingRound.BettingRoundRepository;
import com.gruppe_f.sep.entities.alias.Alias;
import com.gruppe_f.sep.entities.alias.AliasRepository;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.liga.LigaRepository;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/bets/")
public class BetsController {
    private BetsRepository betsrepo;
    private LigaRepository ligaRepository;
    private AliasRepository aliasRepository;
    private UserRepository userRepository;
    private DateRepository dateRepository;
    private BettingRoundRepository bettingRoundRepository;



    @Autowired
    public BetsController (BetsRepository betsrepo,
                           LigaRepository ligaRepository,
                           AliasRepository aliasRepository,
                           UserRepository userRepository,
                           DateRepository dateRepository,
                           BettingRoundRepository bettingRoundRepository){
        this.betsrepo = betsrepo;
        this.ligaRepository = ligaRepository;
        this.aliasRepository = aliasRepository;
        this.userRepository = userRepository;
        this.bettingRoundRepository = bettingRoundRepository;
        this.dateRepository = dateRepository;
    }

    @GetMapping("leagues")
    public ResponseEntity<?> getAllLeagues() {
        // Calculation of scores
        List<BettingRound> bettingRounds = bettingRoundRepository.findAll();

        for(BettingRound bettingRound : bettingRounds) {
            GenerellLogisch.calculateScore(
                    dateRepository.findAll().get(0).getLocalDate(), bettingRound);
        }

        // return all leagues
        return new ResponseEntity<>(ligaRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("topUser/{id}")
    public ResponseEntity<?> getTopUser(@PathVariable("id") Long id) {

        List<Bets> allBets = betsrepo.findAll();
        List<User> topUsers = calculateTopUsers(allBets, id);

        return new ResponseEntity<>(topUsers, HttpStatus.OK);
    }


    @GetMapping("topTeams/{id}")
    public ResponseEntity<?> getTopTeams(@PathVariable("id") Long id) {
        List<Bets> allBets = betsrepo.findAll();
        List<LeagueData> topTeams = calculateTopTeams(allBets, id);
        return new ResponseEntity<>(topTeams, HttpStatus.OK);
    }


    // input is list of all existent bets
    // output is calculated result
    private List<User> calculateTopUsers(List<Bets> allBets, Long ligaID) {
        Map<Long, Integer> result2 = new HashMap<>();

        List<BettingRound> bettingRounds = bettingRoundRepository.findAll();

        for(BettingRound bettingRound : bettingRounds) {
            // skip bettingRound if not from current league
            if(!bettingRound.getLigaID().equals(ligaID)) continue;
            Map<Long, Integer> temp2 = new HashMap<>();
            List<Bets> bets = bettingRound.getBetsList();
            for(Bets bet : bets) {
                // add score to map
                if(temp2.containsKey(bet.getUserID())) {

                    int help = temp2.get(bet.getUserID());
                    temp2.replace(bet.getUserID(), help+bet.getScore());
                }
                else {
                    temp2.put(bet.getUserID(), bet.getScore());
                }
            }

            // iterate over temp map and replace or add value to whole map
            for(Map.Entry<Long, Integer> entry : temp2.entrySet()) {
                if(result2.containsKey(entry.getKey())) {
                    // if value from current list is higher than last value, replace it
                    if(entry.getValue() > result2.get(entry.getKey())) {
                        result2.replace(entry.getKey(), entry.getValue());
                    }

                }
                else {
                    result2.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // now ready with calculation
        // order Map by Value and delete all under top 3
        // with help by user Brian Goetz at https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
        Stream<Map.Entry<Long,Integer>> sorted2 =
                result2.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        List<User> top3Users = new ArrayList<>();
        for(Object x : sorted2.toList()) {
            User user = new User();
            // set name
            List<Alias> aliases = aliasRepository.findAll();
            String userID = x.toString().split("=")[0];
            // if alias exists in aliases
            boolean aliasExists = false;
            for(Alias alias : aliases) {
                if(String.valueOf(alias.getUserID()).equals(userID)) {
                    user.setFirstName(alias.getAlias());
                    aliasExists = true;
                    break;
                }
            }
            // if alias does not exist set real name
            if(!aliasExists) {
                user.setFirstName(userRepository.findUserById(
                        Long.parseLong(userID)
                ).getFirstName());
            }
            // set value
            user.setCode(x.toString().split("=")[1]);
            top3Users.add(user);
            if(top3Users.size() >= 3) break;
        }

        return top3Users;
    }

    // input is league_data rep and list of all existent bets
    // output is calculated result

    private List<LeagueData> calculateTopTeams(List<Bets> allBets, Long ligaID) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        // go through all bets for overall calculation
        for(Bets bet : allBets) {

            // if bet is not from current liga, just skip
            if(bet.getLeagueData().getLigaID() != ligaID) continue;
            // points are accounted to winner or in draw to both
            int firstWins = GenerellLogisch.FirstIsWinner(bet.getLeagueData());
            switch(firstWins) {
                case 1:
                    result = newResult(result, bet.getLeagueData().getPlayer1(), bet.getScore());
                    break;
                case -1:
                    result = newResult(result, bet.getLeagueData().getPlayer2(), bet.getScore());
                    break;
                case 0:
                    result = newResult(result, bet.getLeagueData().getPlayer1(), bet.getScore());
                    result = newResult(result, bet.getLeagueData().getPlayer2(), bet.getScore());
                    break;
            }
        }


        // order Map by Value and delete all under top 3
        // with help by user Brian Goetz at https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
        Stream<Map.Entry<String,Integer>> sorted =
                result.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        List<LeagueData> finalList = new ArrayList<>();
        for(Object x : sorted.toList()) {
            LeagueData leagueData = new LeagueData();
            leagueData.setPlayer1(x.toString().split("=")[0]);
            leagueData.setResult(x.toString().split("=")[1]);
            finalList.add(leagueData);
            if(finalList.size() >= 3) break;
        }

        return finalList;
    }


    // input is Map
    // output is newMap
    private Map<String, Integer> newResult(Map<String, Integer> oldResult, String player, int score) {
        // correct old value if team is already in Map
        if(oldResult.containsKey(player)) {
            int temp = oldResult.get(player);
            oldResult.replace(player, temp+score);
        }
        // create new entry if team is not yet in Map
        else {
            oldResult.put(player, score);
        }

        return oldResult;
    }
}
