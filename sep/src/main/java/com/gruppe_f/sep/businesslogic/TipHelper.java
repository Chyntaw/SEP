package com.gruppe_f.sep.businesslogic;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class TipHelper {

    private  DateRepository dateRepository;
    private LeagueDataRepository leagueDataRepository;

    @Autowired
    public TipHelper(DateRepository dateRepository, LeagueDataRepository leagueDataRepository) {
        this.dateRepository = dateRepository;
        this.leagueDataRepository = leagueDataRepository;
    }

    // inputs are the two current players
    // return element is possible result
    public String tipHelp(String player1, String player2, Long id) {

        // get all data from database
        SystemDate systemDate = dateRepository.findAll().get(0);
        List<LeagueData> all_league_data = leagueDataRepository.findAll();

        // create lists for calculation
        List<LeagueData> toCalculateList1 = new ArrayList<>();
        List<LeagueData> toCalculateList2 = new ArrayList<>();

        // sort list by date for correct tiphelp
        // help by harshtuna at https://stackoverflow.com/questions/28543602/how-to-sort-an-array-by-a-particular-value
        all_league_data.stream().sorted(Comparator.comparing(LeagueData::getId));


        // look for ligaid in all_league_data
        Long ligaid = 0L;
        for(LeagueData x : all_league_data) {
            if( x.getId() == id) {
                ligaid = x.getLigaID();
                break;
            }
        }
        List<LeagueData> spec_league_data = new ArrayList<>();
        // delete data that is not in the league
        for(int i=0; i<all_league_data.size(); i++) {
            if(all_league_data.get(i).getLigaID() == ligaid) {
                spec_league_data.add(all_league_data.get(i));
            }
        }


        // go through loop with 2 conditions in this order
        // 1. in all_league_data needs to be an entry
        // 2. as long as the systemdate is later than the last matchdays
        while(  !spec_league_data.isEmpty() &&
                GenerellLogisch.compareDates(systemDate.getLocalDate(), spec_league_data.get(0).getDate()) > 0) {
            System.out.println(spec_league_data.get(0).getDate());
            System.out.println(systemDate.getLocalDate());
            // add value to list for player1 if player1 is in current value
            if(     player1.equals(spec_league_data.get(0).getPlayer1()) ||
                    player1.equals(spec_league_data.get(0).getPlayer2()) ) {
                toCalculateList1.add(spec_league_data.get(0));

                // remove the latest value if more than 3 are in it
                if(toCalculateList1.size() > 3) {
                    toCalculateList1.remove(0);
                }
            }

            // add value to list for player2 if player2 is in current value
            if(    player2.equals(spec_league_data.get(0).getPlayer1()) ||
                   player2.equals(spec_league_data.get(0).getPlayer2()) ) {
                toCalculateList2.add(spec_league_data.get(0));

                // remove the latest value if more than 3 are in it
                if(toCalculateList2.size() > 3) {
                    toCalculateList2.remove(0);
                }
            }

            // remove the read element
            spec_league_data.remove(0);
        }

        // if there is no data: send no real help to frontend
        if(toCalculateList1.size() < 1 || toCalculateList2.size() < 1) {
            return "N/A";
        }

        // results of the two teams
        int[] result1 = sumOfMatches(toCalculateList1, player1);
        int[] result2 = sumOfMatches(toCalculateList1, player2);

        // final result
        int[] result = {0, 0, result1[2] - result2[2]};

        String tip;

        switch(result[2]) {
            // team 2 has more matches
            case -1:
                result1 = differenceDeleter(result1);
                break;
            // teams have same number of matches
            case 0:
                break;
            // team 1 has more matches
            case 1:
                result2 = differenceDeleter(result2);
                break;
        }

        result[0] = Math.max(0, result1[0] - result2[1]);
        result[1] = Math.max(0, result1[1] - result2[0]);

        return Integer.toString(result[0]) + "-" + Integer.toString(result[1]);
    }

    // input is list for calculation and the team to look for
    // returns sum of matches in array with 3 values:
    //  first value: goals this team made
    //  second value: enemy goals
    //  third value: number of matches
    private int[] sumOfMatches(List<LeagueData> toCalculateList, String player) {
        int[] result = {0, 0, toCalculateList.size()};

        // go through list for data
        for(LeagueData x : toCalculateList) {
            // check if first player is given player
            if(player.equals(x.getPlayer1())) {
                // get first number from result string
                // parse to int from string
                // add to made goals
                result[0] += Integer.parseInt(x.getResult().split("-")[0]);

                // get second number from result string
                // parse to int from string
                // add to enemy goals
                result[1] += Integer.parseInt(x.getResult().split("-")[1]);
            }
            // if first player is not given player it must be second player
            else {
                // get second number from result string
                // parse to int from string
                // add to made goals
                result[0] += Integer.parseInt(x.getResult().split("-")[1]);

                // get first number from result string
                // parse to int from string
                // add to enemy goals
                result[1] += Integer.parseInt(x.getResult().split("-")[0]);
            }
        }
        return result;
    }

    // inputs are result array and the toCalculateList with fewer entries
    private int[] differenceDeleter(int[] result) {

        // just double values if only one match exists
        if(result[2] == 1) {
            result[0] *= 2;
            result[1] *= 2;
        }

        // multiply with 1.5 but make sure an int is saved for the two values
        if(result[2] == 2) {
            result[0] = (int) ((float)result[0] * 1.5);
            result[1] = (int) ((float)result[1] * 1.5);
        }

        return result;
    }

}
