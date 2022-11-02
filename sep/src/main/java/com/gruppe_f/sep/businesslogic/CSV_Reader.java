package com.gruppe_f.sep.businesslogic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSV_Reader {

    // input path to csv-File
    // return element contains whole data of csv File
    //
    // to get whole data in return element it is possible to use:
    // -----
    // for (String[] con_line : whole_content) {
    //            for (String element : con_line) {
    //                System.out.println(element);
    //            }
    //        }
    // ----
    public static ArrayList<String[]> csv_read(String path) {
        // next line to read
        String line = "";

        ArrayList<String[]> whole_content = new ArrayList<>();

        try {
            // Object that reads the csv File
            BufferedReader br = new BufferedReader(new FileReader(path));

            // continue as long as there's a line with content
            while((line = br.readLine()) != null)   {
                // splitting comma seperated data into String array elements
                String[] content = line.split(",");
                // add line to ArrayList
                whole_content.add(content);
            }


            // File is not found
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return whole_content;
    }

}
