package com.gruppe_f.sep.businesslogic;

import com.gruppe_f.sep.entities.leagueData.LeagueData;

import java.util.Arrays;
import java.util.Date;

public class GenerellLogic {
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
