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

    @GetMapping("topUser")
    public ResponseEntity<?> getTopUser() {
        List<BettingRound> bettingRounds = bettingRoundRepository.findAll();
        for(BettingRound bettingRound : bettingRounds) {
            GenerellLogisch.calculateScore(
                    dateRepository.findAll().get(0).getLocalDate(), bettingRound);
        }
        List<Bets> allBets = betsrepo.findAll();
        List<Liga> allLeagues = ligaRepository.findAll();
        Map<String, List<User>> topUsers = new TreeMap<>();

        for(Liga liga : allLeagues) {
            List<User> topUser = calculateTopUsers(allBets, liga.getId());
            topUsers.put(liga.getName(), topUser);
        }

        return new ResponseEntity<>(topUsers, HttpStatus.OK);
    }


    @GetMapping("topTeams")
    public ResponseEntity<?> getTopTeams() {
        List<BettingRound> bettingRounds = bettingRoundRepository.findAll();
        for(BettingRound bettingRound : bettingRounds) {
            GenerellLogisch.calculateScore(
                    dateRepository.findAll().get(0).getLocalDate(), bettingRound);
        }
        List<Bets> allBets = betsrepo.findAll();
        List<Liga> allLeagues = ligaRepository.findAll();
        // A List of Key Value pairs
        // Key is League name
        // Value is List of Points of each team
        Map<String, List<LeagueData>>  fullMap = new TreeMap<>();
        for(Liga liga : allLeagues) {
            List<LeagueData> topTeams = calculateTopTeams(allBets, liga.getId());
            fullMap.put(liga.getName(), topTeams);

        }
        return new ResponseEntity<>(fullMap, HttpStatus.OK);
    }


    // input is list of all existent bets
    // output is calculated result
    private List<User> calculateTopUsers(List<Bets> allBets, Long ligaID) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        // -----CALCULATION-----
        Map<Long, Integer> temp = new HashMap<>();
        // go through all bets for overall calculation
        for(Bets bet : allBets) {
            // if bet is not from current liga, just skip
            if(bet.getLeagueData().getLigaID() != ligaID) continue;

            if(temp.containsKey(bet.getUserID())) {
                int help = temp.get(bet.getUserID());
                temp.replace(bet.getUserID(), help+bet.getScore());
            }
            else {
                temp.put(bet.getUserID(), bet.getScore());
            }
        }

        // -----ORDERING-----
        // order Map by Value and delete all under top 3
        // with help by user Brian Goetz at https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
        Stream<Map.Entry<Long,Integer>> sorted =
                temp.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));


        List<User> finalList = new ArrayList<>();
        for(Object x : sorted.toList()) {
            User user = new User();
            // get firstname of user
            user.setFirstName(userRepository.findUserById(
                    Long.parseLong(x.toString().split("=")[0])).getFirstName());
            user.setCode(x.toString().split("=")[1]);

            List<Alias> aliases = aliasRepository.findAll();
            // get alias TODO: not working
            /*
            for(Alias alias : aliases) {
                if(alias.getUserID().toString().equals(user.getCode())){
                    user.setFirstName(alias.getAlias());
                }
            }
            */
            finalList.add(user);
            if(finalList.size() >= 3) break;
        }


        return finalList;
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
