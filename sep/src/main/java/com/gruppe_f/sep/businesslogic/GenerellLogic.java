package com.gruppe_f.sep.businesslogic;

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

}
