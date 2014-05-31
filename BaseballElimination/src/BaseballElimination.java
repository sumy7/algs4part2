import java.util.ArrayList;
import java.util.HashMap;

/*************************************************************************
 * Compilation: javac BaseballElimination.java <br>
 * Execution: IllegalArgumentException<br>
 * Dependencies: <br>
 * 
 * @author sumy
 * 
 *************************************************************************/
public class BaseballElimination {
    private int teamNum;
    private ArrayList<String> teamName;
    private HashMap<String, Integer> teamIndex;
    private int[] win;
    private int[] loss;
    private int[] remaining;
    private int[][] gameleft;

    private boolean[] teamEliminated;
    private HashMap<String, ArrayList<String>> cOE;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamNum = in.readInt();
        teamName = new ArrayList<String>();
        teamIndex = new HashMap<String, Integer>();
        win = new int[teamNum];
        loss = new int[teamNum];
        remaining = new int[teamNum];
        gameleft = new int[teamNum][teamNum];

        teamEliminated = new boolean[teamNum];
        cOE = new HashMap<String, ArrayList<String>>();

        for (int i = 0; i < teamNum; i++) {
            String name = in.readString();
            teamName.add(name);
            teamIndex.put(name, i);
            win[i] = in.readInt();
            loss[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < teamNum; j++) {
                gameleft[i][j] = in.readInt();
            }
        }

        for (int i = 0; i < teamNum; i++) {
            workFlow(i);
        }
    }

    private void workFlow(int x) {
        ArrayList<String> temp = null;
        for (int i = 0; i < teamNum; i++) {
            if (i == x) {
                continue;
            }
            if (win[x] + remaining[x] < win[i]) {
                teamEliminated[x] = true;
                if (temp == null) {
                    temp = new ArrayList<String>();
                }
                temp.add(teamName.get(i));
            }
        }
        if (temp != null) {
            cOE.put(teamName.get(x), temp);
            return;
        }

        double totalflow = 0;
        int nodenumber = 2 + teamNum + (teamNum - 1) * teamNum / 2;
        FlowNetwork flowNetwork = new FlowNetwork(nodenumber);
        for (int i = 0; i < teamNum; i++) {
            FlowEdge edge;
            for (int j = 0; j < i; j++) {
                edge = new FlowEdge(0, i * (i - 1) / 2 + j + 1, gameleft[i][j]);
                flowNetwork.addEdge(edge);
                totalflow += gameleft[i][j];
                edge = new FlowEdge(i * (i - 1) / 2 + j + 1, teamNum
                        * (teamNum - 1) / 2 + 1 + i, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(edge);
                edge = new FlowEdge(i * (i - 1) / 2 + j + 1, teamNum
                        * (teamNum - 1) / 2 + 1 + j, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(edge);
            }
            edge = new FlowEdge(teamNum * (teamNum - 1) / 2 + 1 + i, 1
                    + teamNum + (teamNum - 1) * teamNum / 2, win[x]
                    + remaining[x] - win[i]);
            flowNetwork.addEdge(edge);
        }

        FordFulkerson flow = new FordFulkerson(flowNetwork, 0, nodenumber - 1);

        double eps = 1e-5;
        if (Math.abs(totalflow - flow.value()) < eps) {
            teamEliminated[x] = false;
            cOE.put(teamName.get(x), null);
        } else {
            teamEliminated[x] = true;
            temp = new ArrayList<String>();
            for (int i = 0; i < teamNum; i++) {
                if (i == x) {
                    continue;
                }
                if (flow.inCut(teamNum * (teamNum - 1) / 2 + 1 + i)) {
                    temp.add(teamName.get(i));
                }
            }
            cOE.put(teamName.get(x), temp);
        }
    }

    public int numberOfTeams() {
        return teamNum;
    }

    public Iterable<String> teams() {
        return teamName;
    }

    public int wins(String team) {
        if (!teamIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        int teami = teamIndex.get(team);
        return win[teami];
    }

    public int losses(String team) {
        if (!teamIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        int teami = teamIndex.get(team);
        return loss[teami];
    }

    public int remaining(String team) {
        if (!teamIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        int teami = teamIndex.get(team);
        return remaining[teami];
    }

    public int against(String team1, String team2) {
        if (!teamIndex.containsKey(team1) || !teamIndex.containsKey(team2)) {
            throw new java.lang.IllegalArgumentException();
        }
        int teami = teamIndex.get(team1);
        int teamj = teamIndex.get(team2);
        return gameleft[teami][teamj];
    }

    public boolean isEliminated(String team) {
        if (!teamIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        int teami = teamIndex.get(team);
        return teamEliminated[teami];
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teamIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        return cOE.get(team);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
