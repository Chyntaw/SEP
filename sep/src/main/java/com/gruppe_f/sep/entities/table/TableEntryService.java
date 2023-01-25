package com.gruppe_f.sep.entities.table;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import java.util.*;


public class TableEntryService {

    //Method to calculate Results (Table) of Liga or User Bets
    //If param E not instanceof Bets or LeagueData -> IllegalArgumentException
    public static <E> List<TableEntry> calculateTable(List<E> list) {

        Map<String, TableEntry> tableEntries = new HashMap<>();

        for(E element:list) {

            String team1;
            String team2;
            String result;
            List<TableEntry> newEntries;

            //Check if elements of List are instances of "Bets" and
            //cast element to Bets -> currBet
            if(element instanceof Bets currBet) {

                team1 = currBet.getLeagueData().getPlayer1();
                team2 = currBet.getLeagueData().getPlayer2();
                result = currBet.getBets();

                //Get 2 new TableEntries for this Game, since both Teams points need to be updated
                newEntries = newTableEntry(team1, team2, result);

            //Check if elements of List are of Type LeagueData, otherwise identical to above
            } else if(element instanceof LeagueData currData) {

                team1 = currData.getPlayer1();
                team2 = currData.getPlayer2();
                result = currData.getResult();

                newEntries = newTableEntry(team1, team2, result);

            //If not of Type Bet or LeagueData, throw Exception
            } else {
                throw new IllegalArgumentException("List must contain instances of 'Bets' or 'LeagueData'");
            }

            //If Team not in Table create entry; otherwise update existing entries in Map using newEntries from above.
            tableEntries.put(team1, combineEntries(tableEntries.get(team1), newEntries.get(0)));
            tableEntries.put(team2, combineEntries(tableEntries.get(team2), newEntries.get(1)));
        }

        //Convert Map.values to List, to sort and return
        List<TableEntry> table = new LinkedList<>(tableEntries.values());

        //Comparators to sort by Points first and GoalDifference second
        //.reversed(), since Table need to be sorted max -> min
        Comparator<TableEntry> punkteComparator = Comparator.comparing(TableEntry::getPunkte).reversed();
        Comparator<TableEntry> torDiffComparator = Comparator.comparing(TableEntry::getDiff).reversed();
        Comparator<TableEntry> doppelComparator = punkteComparator.thenComparing(torDiffComparator);

        Collections.sort(table, doppelComparator);

        return table;
    }

    //Generates a List of 2 TableEntries for 1 Game.
    public static List<TableEntry> newTableEntry (String team1, String team2, String result) {

        int goalsPlayer1 = Integer.parseInt(result.split("-")[0]);
        int goalsPlayer2 = Integer.parseInt(result.split("-")[1]);

        TableEntry entryTeam1 = new TableEntry(team1, 1, goalsPlayer1, goalsPlayer2);
        TableEntry entryTeam2 = new TableEntry(team2, 1, goalsPlayer2, goalsPlayer1);

        //Check for Unentschieden
        if(goalsPlayer1 == goalsPlayer2) {

            entryTeam1.setUnentschieden(1);
            entryTeam1.setPunkte(entryTeam1.getUnentschiedenPunkte());

            entryTeam2.setUnentschieden(1);
            entryTeam2.setPunkte(entryTeam2.getUnentschiedenPunkte());
        //Check if Team1 is Winner
        } else if (goalsPlayer1 > goalsPlayer2) {
            entryTeam1.setSiege(1);
            entryTeam1.setPunkte(entryTeam1.getSiegPunkte());

            entryTeam2.setVerloren(1);
        //If not Unentschieden, and Team1 is not Winner, Team2 must be winner
        } else {
            entryTeam2.setSiege(1);
            entryTeam2.setPunkte(entryTeam2.getSiegPunkte());

            entryTeam1.setVerloren(1);
        }
        //Return both new entries with Team1 at index 0, Team2 at index 1
        List<TableEntry> returnList = new LinkedList<>();
        returnList.add(entryTeam1);
        returnList.add(entryTeam2);

        return returnList;
    }

    //Combine Entries, so that Reference to param1 is not lost.
    public static TableEntry combineEntries(TableEntry entry, TableEntry consumedEntry) {

        if(entry == null) return consumedEntry;

        entry.setToreGeschossen(entry.getToreGeschossen() + consumedEntry.getToreGeschossen());
        entry.setToreKassiert(entry.getToreKassiert() + consumedEntry.getToreKassiert());
        entry.setPunkte(entry.getPunkte() + consumedEntry.getPunkte());
        entry.setAnzahlSpiele(entry.getAnzahlSpiele() + consumedEntry.getAnzahlSpiele());
        entry.setSiege(entry.getSiege() + consumedEntry.getSiege());
        entry.setUnentschieden(entry.getUnentschieden() + consumedEntry.getUnentschieden());
        entry.setVerloren(entry.getVerloren() + consumedEntry.getVerloren());

        entry.setDiff(entry.getToreGeschossen() - entry.getToreKassiert());

        return entry;
    }
}
