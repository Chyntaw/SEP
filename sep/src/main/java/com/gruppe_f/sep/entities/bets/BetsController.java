package com.gruppe_f.sep.entities.bets;

import com.gruppe_f.sep.businesslogic.GenerellLogic;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    @Autowired
    public BetsController (BetsRepository betsrepo){
        this.betsrepo = betsrepo;
    }


    @GetMapping("topTeams")
    public ResponseEntity<?> getTopTeams() {
        List<Bets> allBets = betsrepo.findAll();
        List<LeagueData> topTeams = calculateTopTeams(allBets);

        return new ResponseEntity<>(topTeams, HttpStatus.OK);
    }

    // input is league_data rep and list of all existent bets
    // output is calculated result

    private List<LeagueData> calculateTopTeams(List<Bets> allBets) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        // go through all bets for overall calculation
        for(Bets bet : allBets) {

            int firstWins = GenerellLogic.FirstIsWinner(bet.getLeagueData());
            // points are accounted to winner or in draw to both
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

        System.out.println(result.toString());

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
