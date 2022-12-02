package com.gruppe_f.sep.businesslogic;

import java.util.Arrays;
import java.util.Date;

public class GenerellLogic {
    /* Works similar to compareTo, returns negative int if
    *   right bigger left.
    *       Hab jz kein bock mehr das zu kommentieren, schau in compareTo methode.
    * */
    public static int compareDates(String date1, String date2) {

        int[] dateleft = Arrays.stream(date1.split("-")).mapToInt(Integer::parseInt).toArray();
        int[] dateright = Arrays.stream(date2.split("-")).mapToInt(Integer::parseInt).toArray();

        Date leftDate = new Date(dateleft[0], dateleft[1], dateleft[2]);
        Date rightDate = new Date(dateright[0], dateright[1], dateright[2]);

        return leftDate.compareTo(rightDate);
    }

}
