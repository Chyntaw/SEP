package com.gruppe_f.sep.entities.table;

import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.leagueData.LeagueData;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class TableEntryService {

    //Method to calculate Results (Table) of Liga or User Bets
    //If param E not instanceof Bets or LeagueData -> IllegalArgumentException
    public static <E> List<TableEntry> calculateTable(List<E> list) {

        if(list.isEmpty()) throw new NullPointerException("List is Empty");

        List<TableEntry> table = new LinkedList<>();

        for(E element:list) {

            String player1;
            String player2;
            String result;
            List<TableEntry> newEntries;
            TableEntry player1Entry;
            TableEntry player2Entry;

            //Check if elements of List are instances of "Bets" and
            //cast element to Bets -> currBet
            if(element instanceof Bets currBet) {

                player1 = currBet.getLeagueData().getPlayer1();
                player2 = currBet.getLeagueData().getPlayer2();
                result = currBet.getBets();

                //Get 2 new TableEntries for this Game, since both Players
                //Points need to be updated
                newEntries = newTableEntry(player1, player2, result);

                player1Entry = newEntries.get(0);
                player2Entry = newEntries.get(1);

            //Check if elements of List are of Type LeagueData
            } else if(element instanceof LeagueData currData) {

                player1 = currData.getPlayer1();
                player2 = currData.getPlayer2();
                result = currData.getResult();

                newEntries = newTableEntry(player1, player2, result);

                player1Entry = newEntries.get(0);
                player2Entry = newEntries.get(1);

            //If not of Type Bet or LeagueData, throw Exception
            } else {
                throw new IllegalArgumentException("Type of List must be 'Bets' or 'LeagueData'");
            }

            //Update Table with new Entries
            if(!table.isEmpty()) {
                boolean p1changed = false;
                boolean p2changed = false;
                for (TableEntry entry : table) {
                    //Check if curr Entry is for Team 1
                    if (entry.getTeam().equals(player1Entry.getTeam())) {
                        //Update existing Table - Entry
                        combineEntries(entry, player1Entry);
                        p1changed = true;
                    }
                    if (entry.getTeam().equals(player2Entry.getTeam())) {

                        combineEntries(entry, player2Entry);
                        p2changed = true;

                    }
                }
                //If not changed before, Players not yet in List -> have to be added here
                if(!p1changed) table.add(player1Entry);
                if(!p2changed) table.add(player2Entry);
            //If Table empty, add Both new Entries
            } else{
                table.add(player1Entry);
                table.add(player2Entry);
            }
        }
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
