package com.gruppe_f.sep.businesslogic;

import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.leagueData.LeagueData;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GenerellLogisch {
    /* Works similar to compareTo, returns
    *       -1 if left < right,
    *       1 if right > left,
    *       0 if identical
    *
    * requested param1, param2: Date-String in format yyyy-mm-dd
    */
    public static int compareDates(String date1, String date2) {

        if(date1 == null || date2 == null) throw new NullPointerException();

        //Get Dates as Integer
        int[] dateleft = Arrays.stream(date1.split("-")).mapToInt(Integer::parseInt).toArray();
        int[] dateright = Arrays.stream(date2.split("-")).mapToInt(Integer::parseInt).toArray();

        Date leftDate = new Date(dateleft[0], dateleft[1], dateleft[2]);
        Date rightDate = new Date(dateright[0], dateright[1], dateright[2]);

        return leftDate.compareTo(rightDate);
    }

    public static String rightPadtext(String text, int length) {
        for(int currlength = text.length(); currlength < length; currlength++) {
            text += " ";
        }
        return text;
    }

    //Helper to calculate the Score of a Bet.
    //return    Bet with score
    public static BettingRound calculateScore(String systemDate, BettingRound round) {

        if(round == null || systemDate == null) throw new NullPointerException();

        List<Bets> betsList = round.getBetsList();

        for(Bets bet: betsList) {

            //Get Date of current Bet
            String betDate = bet.getLeagueData().getDate();
            //Compare Date of Bet to systemDate
            //If Future-game -> set score = null
            if(compareDates(betDate, systemDate) >= 0) {
                bet.setScore(-1);
                continue;
            };

            String currentBet = bet.getBets();
            String actualResult = bet.getLeagueData().getResult();

            //If Strings identical, Bet is perfect
            if(currentBet.equals(actualResult)) {
                bet.setScore(round.getCorrScorePoints());
                continue;
            }
            //Get goals from LeagueData and from bet as Integer Array
            int[] goals = Arrays.stream(bet.getLeagueData().getResult().split("-")).mapToInt(Integer::parseInt).toArray();
            int[] betGoals = Arrays.stream(bet.getBets().split("-")).mapToInt(Integer::parseInt).toArray();

            //Get Goaldifference for calculation
            int goalDiff = goals[0]-goals[1];
            int betDiff = betGoals[0]-betGoals[1];
            //Correct Goal Difference
            if(Math.abs(goalDiff) == Math.abs(betDiff)) {
                bet.setScore(round.getCorrGoalPoints());
                continue;
            }
            //Correct Winner
            if((goalDiff < 0 && betDiff < 0) || (goalDiff >0 && betDiff >0)) {
                bet.setScore(round.getCorrWinnerPoints());
                continue;
            }
        }
        return round;
    }


    // checks who is winner of match
    // return    1 for player1
    //          -1 for player2
    //           0 for draw
    public static int FirstIsWinner(LeagueData leagueData) {
        String result = leagueData.getResult();
        int result1 = Integer.parseInt(result.split("-")[0]);
        int result2 = Integer.parseInt(result.split("-")[1]);
        if(result1 > result2) {
            return 1;
        }
        else {
            if(result2 > result1) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}
