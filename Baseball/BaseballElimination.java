/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 25/08/19
 *  Description: An immutable data type that represents a sports division and
 *  determines which teams are mathematically eliminated
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("WOC_WRITE_ONLY_COLLECTION_FIELD")
public class BaseballElimination {

    private final int numberOfTeams;
    private final int[] wins, losses, remaining;
    private int[][] games;
    private final HashMap<String, Integer> teams;
    private boolean[] isEliminated;
    private final HashMap<Integer, ArrayList<Integer>> certificates;

    // creates a baseball division game from given filename
    public BaseballElimination(String filename) {
        In in = new In(filename);

        // storing the team name, number of wins, losses and remaining games for each team
        numberOfTeams = in.readInt();
        teams = new HashMap<>();
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        games = new int[numberOfTeams][numberOfTeams];
        isEliminated = new boolean[numberOfTeams];
        certificates = new HashMap<>();

        int i = 0;
        while (in.hasNextLine()) {
            teams.put(in.readString(), i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int g = 0; g < numberOfTeams; g++)
                games[i][g] = in.readInt();

            in.readLine();
            i++;
        }

        eliminateTrivially();
        eliminateNonTrivially();
    }

    // returns the number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        Queue<String> teamNames = new Queue<>();
        for (String teamName : teams.keySet())
            teamNames.enqueue(teamName);

        return teamNames;
    }

    // number of wins for a given team
    public int wins(String team) {
        validate(team);
        int i = teams.get(team);
        return wins[i];
    }

    // number of losses for a given team
    public int losses(String team) {
        validate(team);
        int i = teams.get(team);
        return losses[i];
    }

    // remaining matches for a team
    public int remaining(String team) {
        validate(team);

        int i = teams.get(team);
        return remaining[i];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);

        int i = teams.get(team1);
        int j = teams.get(team2);
        return games[i][j];
    }

    // is this team eliminated?
    public boolean isEliminated(String team) {
        validate(team);
        return isEliminated[teams.get(team)];
    }

    // subset R of teams that eliminates given team
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        ArrayList<Integer> teamIndexes = certificates.get(teams.get(team));
        Stack<String> teamNames = new Stack<>();
        if (teamIndexes == null) return null;
        for (int i : teamIndexes) {
            for (Map.Entry<String, Integer> entry : teams.entrySet()) {
                if (entry.getValue() == i) {
                    teamNames.push(entry.getKey());
                    break;
                }
            }
        }

        return teamNames;
    }

    private void validate(String name) {
        if (name == null || !teams.containsKey(name))
            throw new IllegalArgumentException("argument passed can't be nil");
    }

    // prints the schedule
    private void printSchedule() {
        StdOut.println("Team | w | l | rem |  schedule");
        for (int i = 0; i < numberOfTeams; i++) {
            StdOut.println(i + "     " + wins[i] + "  " + losses[i] + "   " + remaining[i] +
                                   "   " + Arrays.toString(games[i]));
        }
    }

    // eliminates the team which are eliminated trivially
    private void eliminateTrivially() {
        // find team with most wins
        int max = wins[0];
        int maxWinTeam = 0;
        for (int i = 0; i < numberOfTeams; i++) {
            if (max < wins[i]) {
                max = wins[i];
                maxWinTeam = i;
            }
        }

        // now eliminate any team which cannot reach the max amount of wins
        for (int i = 0; i < numberOfTeams; i++) {
            if (wins[i] + remaining[i] < max) {
                isEliminated[i] = true;
                certificates.put(i, new ArrayList<>());
                certificates.get(i).add(maxWinTeam);
            }
        }
    }

    // eliminate teams by computing maxflow
    private void eliminateNonTrivially() {
        for (int i = 0; i < numberOfTeams; i++) {
            if (isEliminated[i]) continue;
            makeFlowNetwork(i);
        }
    }

    private void makeFlowNetwork(int teamIndex) {
        int vertices;
        if (numberOfTeams == 1) vertices = 10;
        else vertices = numberOfTeams * numberOfTeams;

        FlowNetwork network = new FlowNetwork(vertices);
        int vertex = numberOfTeams;
        int source = vertices - 2;
        int sink = vertices - 1;
        Stack<FlowEdge> fromSources = new Stack<>();
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == teamIndex) continue;
            for (int j = i; j < numberOfTeams; j++) {
                if (i == j || j == teamIndex) continue;
                FlowEdge fromSource = new FlowEdge(source, vertex, games[i][j]);
                fromSources.push(fromSource);
                network.addEdge(fromSource);

                network.addEdge(new FlowEdge(vertex, i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(vertex, j, Double.POSITIVE_INFINITY));
                vertex++;
            }
            network.addEdge(
                    new FlowEdge(i, sink, wins[teamIndex] + remaining[teamIndex] - wins[i]));
        }

        FordFulkerson ff = new FordFulkerson(network, source, sink);


        for (FlowEdge fe : fromSources)
            if (fe.capacity() != fe.flow()) {
                isEliminated[teamIndex] = true;
            }

        if (isEliminated[teamIndex]) {
            if (certificates.get(teamIndex) == null) {
                certificates.put(teamIndex, new ArrayList<>());
            }
            for (int i = 0; i < numberOfTeams; i++)
                if (ff.inCut(i))
                    certificates.get(teamIndex).add(i);
        }
    }


    public static void main(String[] args) {

        BaseballElimination division = new BaseballElimination(args[0]);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}
