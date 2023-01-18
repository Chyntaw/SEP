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

import java.io.IOException;
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
    private List<BettingRound> bettingRoundList = new ArrayList<>();
    private List<Bets> betsList = new ArrayList<>();



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
        this.bettingRoundList = bettingRoundRepository.findAll();
        this.betsList = new ArrayList<>();
        for(BettingRound bettingRound : this.bettingRoundList) {
             bettingRound = GenerellLogisch.calculateScore(
                    dateRepository.findAll().get(0).getLocalDate(), bettingRound);
             this.betsList = Stream.concat(this.betsList.stream(), bettingRound.getBetsList().stream()).toList();
        }
        // return all leagues
        return new ResponseEntity<>(ligaRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("topUser/{id}")
    public ResponseEntity<?> getTopUser(@PathVariable("id") Long id) {

        List<User> topUsers = calculateTopUsers(id);

        return new ResponseEntity<>(topUsers, HttpStatus.OK);
    }


    @GetMapping("topTeams/{id}")
    public ResponseEntity<?> getTopTeams(@PathVariable("id") Long id) {
        List<LeagueData> topTeams = calculateTopTeams(this.betsList, id);
        return new ResponseEntity<>(topTeams, HttpStatus.OK);
    }

    @GetMapping("LeaguesBettingRound")
    public ResponseEntity<?> getLeaguesByBettingRound() {

        // get data
        List<BettingRound> bettingRounds = bettingRoundRepository.findAll();
        List<Liga> ligas = ligaRepository.findAll();

        // delete counted data in past
        List<BettingRound> tempBR = new ArrayList<>();
        for(BettingRound bettingRound : bettingRounds) {
            if(!bettingRound.getIsResetted()) {
                tempBR.add(bettingRound);
            }
        }
        bettingRounds = tempBR;

        // work with data
        // create HashMap of leagues
        HashMap<Long, Integer> leagueCounter = new HashMap<>();
        for(Liga liga : ligas) {
            leagueCounter.put(liga.getId(), 0);
        }

        // count Occurrences
        for(BettingRound bettingRound : bettingRounds) {
            Integer currentValue = leagueCounter.get(bettingRound.getLigaID());
            leagueCounter.replace(
                    bettingRound.getLigaID(),
                    currentValue + 1 // get old value and add 1
                    );
        }

        // create final list with complete liga object
        HashMap<List<String>, Integer> finalMap = new HashMap<>();

        for(Long key : leagueCounter.keySet()) {
            List<String> newEntry = new ArrayList<>();
            newEntry.add(key.toString());
            newEntry.add(ligaRepository.findLigaByid(key).getName());

            finalMap.put(newEntry, leagueCounter.get(key));
        }

        return new ResponseEntity<>(finalMap, HttpStatus.OK);
    }

    @GetMapping("LeaguesUsers")
    public ResponseEntity<?> getLeaguesByUsers() {

        // get data
        List<Bets> bets = betsrepo.findAll();
        List<Liga> ligas = ligaRepository.findAll();

        List<Bets> tempB = new ArrayList<>();
        for(Bets bet : bets) {
            if(!bet.getIsResetted()) {
                tempB.add(bet);
            }
        }
        bets = tempB;

        HashMap<Long, Integer> leagueCounter = new HashMap<>();

        // work with data
        for(Liga liga : ligas) {

            Integer count = 0;
            List<Long> userIDs = new ArrayList<>();
            for(Bets bet : bets) {
                // just skip if liga ids not equal
                if(bet.getLeagueData().getLigaID() != liga.getId()) continue;

                // add userID to list and increase counter by 1
                if(!userIDs.contains(bet.getUserID())) {
                    userIDs.add(bet.getUserID());
                    count++;
                }
            }

            leagueCounter.put(liga.getId(), count);

        }

        // create final list with complete liga object
        HashMap<List<String>, Integer> finalMap = new HashMap<>();

        for(Long key : leagueCounter.keySet()) {

            List<String> newEntry = new ArrayList<>();
            newEntry.add(key.toString());
            newEntry.add(ligaRepository.findLigaByid(key).getName());

            finalMap.put(newEntry, leagueCounter.get(key));

        }

        return new ResponseEntity<>(finalMap, HttpStatus.OK);
    }


    @GetMapping("PutByTippingRound/{id}")
    public ResponseEntity<?> putByBettingRound(@PathVariable("id") Long ligaID) {

        List<BettingRound> allBettingRounds = bettingRoundRepository.findAll();

        // set flag
        for(BettingRound bettingRound : allBettingRounds) {
            if(bettingRound.getLigaID() == ligaID) {
                bettingRound.setIsResetted(true);
                bettingRoundRepository.save(bettingRound);
            }
        }



        return new ResponseEntity<>("BR-Flags gesetzt", HttpStatus.OK);
    }

    @GetMapping("PutByBet/{id}")
    public ResponseEntity<?> putByBet(@PathVariable("id") Long ligaID) {

        List<Bets> allBets = betsrepo.findAll();

        // set flag
        for(Bets bet : allBets) {
            if(bet.getLeagueData().getLigaID() == ligaID) {
                bet.setIsResetted(true);
                betsrepo.save(bet);
            }
        }


        return new ResponseEntity<>("B-Flags gesetzt", HttpStatus.OK);
    }

    // input is list of all existent bets
    // output is calculated result
    private List<User> calculateTopUsers(Long ligaID) {
        Map<List<Long>, Integer> result2 = new HashMap<>();


        for(BettingRound bettingRound : this.bettingRoundList) {
            // skip bettingRound if not from current league
            if(!bettingRound.getLigaID().equals(ligaID)) continue;
            Map<List<Long>, Integer> temp2 = new HashMap<>();
            List<Bets> bets = bettingRound.getBetsList();
            for(Bets bet : bets) {
                // add score to map

                List<Long> keyList = new ArrayList<>();
                keyList.add(bet.getUserID());
                keyList.add(bettingRound.getId());

                if(temp2.containsKey(keyList)) {
                    int help = temp2.get(keyList);
                    temp2.replace(keyList, help+bet.getScore());
                }
                else {
                    temp2.put(keyList, bet.getScore());
                }
            }

            // iterate over temp map and replace or add value to whole map
            for(Map.Entry<List<Long>, Integer> entry : temp2.entrySet()) {
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
        Stream<Map.Entry<List<Long>,Integer>> sorted2 =
                result2.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        List<User> top3Users = new ArrayList<>();
        for(Map.Entry<List<Long>, Integer> x : sorted2.toList()) {
            Long[] keys = x.getKey().toArray(new Long[2]);
            User user = new User();

            // set name
            List<BettingRound> bettingRounds = bettingRoundRepository.findAll();
            for(BettingRound bettingRound : bettingRounds) {
                if(!bettingRound.getId().equals(keys[1])) continue;

                List<Alias> aliases = bettingRound.getAliasList();
                boolean aliasExists = false;
                for(Alias alias : aliases) {
                    if(String.valueOf(alias.getUserID()).equals(keys[0])) {
                        user.setFirstName(alias.getAlias());
                        aliasExists = true;
                        break;
                    }
                }
                if(!aliasExists) {
                    user.setFirstName(userRepository.findUserById(keys[0]).getFirstName());
                }
            }

            // set score
            user.setCode(String.valueOf(x.getValue()));
            top3Users.add(user);
            if(top3Users.size() >= 3) break;
        }
        /*
        for(Object x : sorted2.toList()) {
            System.out.println(x.toString());

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
*/
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
