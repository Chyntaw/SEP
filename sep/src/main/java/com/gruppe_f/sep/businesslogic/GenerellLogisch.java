package com.gruppe_f.sep.businesslogic;

import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.table.TableEntry;

import javax.persistence.Table;
import java.util.*;
import java.util.stream.Collectors;

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

    //Generic Method to calculate Results of Liga or User Bets
    //If param E not instanceof Bets or LeagueData -> Empty List returned
    public static <E> List<TableEntry> calculateTable(List<E> list) {

        if(list.isEmpty()) throw new NullPointerException("List is Empty");

        List<TableEntry> table = new LinkedList<>();

        for(E element:list) {
            //Check if elements of List are instances of "Bets"
            if(element instanceof Bets) {
                //Cast generic E to Bets
                Bets currBet = (Bets)element;

                String player1 = currBet.getLeagueData().getPlayer1();
                String player2 = currBet.getLeagueData().getPlayer2();
                String bet = currBet.getBets();

                List<TableEntry> newEntries = newTableEntry(player1, player2, bet);

                TableEntry player1Entry = newEntries.get(0);
                TableEntry player2Entry = newEntries.get(1);

                if(!table.isEmpty()) {
                    boolean p1changed = false;
                    boolean p2changed = false;
                    for (TableEntry entry : table) {

                        if (entry.getTeam().equals(player1Entry.getTeam())) {

                            combineEntries(entry, player1Entry);
                            p1changed = true;
                        }
                        if (entry.getTeam().equals(player2Entry.getTeam())) {

                            combineEntries(entry, player2Entry);
                            p2changed = true;

                        }
                    }
                    if(!p1changed) table.add(player1Entry);
                    if(!p2changed) table.add(player2Entry);
                } else{
                    table.add(player1Entry);
                    table.add(player2Entry);
                }

            //Check if elements of List are of Type LeagueData
            } else if(element instanceof LeagueData) {

                LeagueData currData = (LeagueData)element;

                String player1 = currData.getPlayer1();
                String player2 = currData.getPlayer2();
                String bet = currData.getResult();

                List<TableEntry> newEntries = newTableEntry(player1, player2, bet);

                TableEntry player1Entry = newEntries.get(0);
                TableEntry player2Entry = newEntries.get(1);

                if(!table.isEmpty()) {
                    boolean p1changed = false;
                    boolean p2changed = false;
                    for (TableEntry entry : table) {
                        //Check if Player1 is already present in List, if yes use Method
                        //combineEntries to get new Score of Player1
                        if (entry.getTeam().equals(player1Entry.getTeam())) {
                            combineEntries(entry, player1Entry);
                            p1changed = true;
                        }
                        if (entry.getTeam().equals(player2Entry.getTeam())) {
                            combineEntries(entry, player2Entry);
                            p2changed = true;
                        }
                    }
                    //If Elements arent changed earlier, they can be added to List here.
                    if(!p1changed) table.add(player1Entry);
                    if(!p2changed) table.add(player2Entry);
                }
                //If Table is Empty, just add both Elements
                else {
                    table.add(player1Entry);
                    table.add(player2Entry);
                }
            }
        }

                /*

                int goalsPlayer1 = Integer.parseInt(currBet.getBets().split("-")[0]);
                int goalsPlayer2 = Integer.parseInt(currBet.getBets().split("-")[1]);

                if(!table.isEmpty()) {
                if(player1Entry == null) {
                    for(TableEntry x : table) if(x.getTeam().equals(player1)) player1Entry = x;
                }
                if(player2Entry == null) {
                    for(TableEntry x : table) if(x.getTeam().equals(player2)) player2Entry = x;
                }
                }

                //Check if Player1 already exists in Table
                if(player1Entry != null) {

                    player1Entry.setAnzahlSpiele(player1Entry.getAnzahlSpiele()+1);

                    player1Entry.setToreGeschossen(player1Entry.getToreGeschossen() + goalsPlayer1);
                    player1Entry.setToreKassiert(player1Entry.getToreKassiert() + goalsPlayer2);
                    //Check who is winner and add Score
                    if(goalsPlayer1 == goalsPlayer2) {
                        player1Entry.setUnentschieden(player1Entry.getUnentschieden() + 1);
                        player1Entry.setPunkte(player1Entry.getPunkte()+ player1Entry.getUnentschiedenPunkte());

                    } else if (goalsPlayer1 > goalsPlayer2) {
                        player1Entry.setSiege(player1Entry.getSiege() + 1);
                        player1Entry.setPunkte(player1Entry.getPunkte()+ player1Entry.getSiegPunkte());
                    } else {
                        player1Entry.setVerloren(player1Entry.getVerloren() + 1);
                    }
                //If Player1 doesnt exist in Table already, create new Entry
                } else {
                    TableEntry newEntry = new TableEntry();
                    newEntry.setTeam(player1);
                    newEntry.setAnzahlSpiele(1);
                    newEntry.setToreGeschossen(goalsPlayer1);
                    newEntry.setToreKassiert(goalsPlayer2);

                    if(goalsPlayer1 == goalsPlayer2) {
                        newEntry.setUnentschieden(1);
                        newEntry.setPunkte(newEntry.getUnentschiedenPunkte());
                    } else if (goalsPlayer1 > goalsPlayer2) {
                        newEntry.setSiege(1);
                        newEntry.setPunkte(newEntry.getSiegPunkte());
                    } else {
                        newEntry.setVerloren(1);
                    }
                    table.add(newEntry);
                }

                //Same as above for player2
                if(player2Entry != null) {
                    //Get Tableentry for Player2
                    player2Entry.setAnzahlSpiele(player2Entry.getAnzahlSpiele()+1);

                    player2Entry.setToreGeschossen(player2Entry.getToreGeschossen() + goalsPlayer2);
                    player2Entry.setToreKassiert(player2Entry.getToreKassiert() + goalsPlayer1);
                    //Check who is winner and add Score
                    if(goalsPlayer1 == goalsPlayer2) {
                        player2Entry.setUnentschieden(player2Entry.getUnentschieden() + 1);
                        player2Entry.setPunkte(player2Entry.getPunkte()+ player2Entry.getUnentschiedenPunkte());

                    } else if (goalsPlayer2 > goalsPlayer1) {
                        player2Entry.setSiege(player2Entry.getSiege() + 1);
                        player2Entry.setPunkte(player2Entry.getPunkte()+ player2Entry.getSiegPunkte());
                    } else {
                        player2Entry.setVerloren(player2Entry.getVerloren() + 1);
                    }
                    //If Player1 doesnt exist in Table already, create new Entry
                } else {
                    TableEntry newEntry = new TableEntry();
                    newEntry.setTeam(player2);
                    newEntry.setAnzahlSpiele(1);
                    newEntry.setToreGeschossen(goalsPlayer2);
                    newEntry.setToreKassiert(goalsPlayer1);

                    if(goalsPlayer1 == goalsPlayer2) {
                        newEntry.setUnentschieden(1);
                        newEntry.setPunkte(newEntry.getUnentschiedenPunkte());
                    } else if (goalsPlayer2 > goalsPlayer1) {
                        newEntry.setSiege(1);
                        newEntry.setPunkte(newEntry.getSiegPunkte());
                    } else {
                        newEntry.setVerloren(1);
                    }
                    table.add(newEntry);
                }
            }
        }
        */

        //Calculate TorDifference
        for(TableEntry entry : table) entry.setDiff(entry.getToreGeschossen()- entry.getToreKassiert());

        //Comparators, so that list can be Sorted by Points first, after that sorted by Goal Difference
        Comparator<TableEntry> punkteComparator = Comparator.comparing(TableEntry::getPunkte).reversed();
        Comparator<TableEntry> torDiffComparator = Comparator.comparing(TableEntry::getDiff).reversed();
        Comparator<TableEntry> doppelComparator = punkteComparator.thenComparing(torDiffComparator);

        Collections.sort(table, doppelComparator);

        return table;

    }

    //Generates a List of TableEntries for 1 Game.
    //Requires Player1, Player2 and Result of game.
    public static List<TableEntry> newTableEntry (String player1, String player2, String result) {

        int goalsPlayer1 = Integer.parseInt(result.split("-")[0]);
        int goalsPlayer2 = Integer.parseInt(result.split("-")[1]);

        TableEntry entryPlayer1 = new TableEntry(player1, 1, goalsPlayer1, goalsPlayer2);
        TableEntry entryPlayer2 = new TableEntry(player2, 1, goalsPlayer2, goalsPlayer1);

        //Check for Unentschieden
        if(goalsPlayer1 == goalsPlayer2) {

            entryPlayer1.setUnentschieden(1);
            entryPlayer1.setPunkte(entryPlayer1.getUnentschiedenPunkte());

            entryPlayer2.setUnentschieden(1);
            entryPlayer2.setPunkte(entryPlayer2.getUnentschiedenPunkte());
        //Check if Player1 is Winner
        } else if (goalsPlayer1 > goalsPlayer2) {
            entryPlayer1.setSiege(1);
            entryPlayer1.setPunkte(entryPlayer1.getSiegPunkte());

            entryPlayer2.setVerloren(1);
        //If not Unentschieden, and Player1 is not Winner, Player2 must be winner
        } else {
            entryPlayer2.setSiege(1);
            entryPlayer2.setPunkte(entryPlayer2.getSiegPunkte());

            entryPlayer1.setVerloren(1);
        }
        //Return both new Entries
        List<TableEntry> returnList = new LinkedList<>();
        returnList.add(entryPlayer1);
        returnList.add(entryPlayer2);

        return returnList;
    }

    //Combine Entries, so that Reference to param1 is not lost.
    //param2 is consumed and lost.
    public static TableEntry combineEntries(TableEntry entry, TableEntry consumedEntry) {

        entry.setToreGeschossen(entry.getToreGeschossen() + consumedEntry.getToreGeschossen());
        entry.setToreKassiert(entry.getToreKassiert() + consumedEntry.getToreKassiert());
        entry.setPunkte(entry.getPunkte() + consumedEntry.getPunkte());
        entry.setAnzahlSpiele(entry.getAnzahlSpiele() + consumedEntry.getAnzahlSpiele());
        entry.setSiege(entry.getSiege() + consumedEntry.getSiege());
        entry.setUnentschieden(entry.getUnentschieden() + consumedEntry.getUnentschieden());
        entry.setVerloren(entry.getVerloren() + consumedEntry.getVerloren());

        return entry;
    }

}
