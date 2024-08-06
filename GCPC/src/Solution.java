import java.util.*;

class Solution {

    // TODO: Include your data structures here
    private static class Team implements Comparable<Team> {
        int teamID;
        long totalSolvedProblems;
        long totalPenalty;

        public Team(long numSolved, long totalPenalty, int id) {
            this.totalSolvedProblems = numSolved;
            this.totalPenalty = totalPenalty;
            this.teamID = id;
        }


        @Override
        public int compareTo(Team other) {
            Long numSolved = this.totalSolvedProblems;
            Long totalPenalty = this.totalPenalty;
            if (numSolved == other.totalSolvedProblems) {
                // The more penalty the worse
                int penaltyDifferentor = -(totalPenalty.compareTo(other.totalPenalty));
                return penaltyDifferentor;
            }
            return numSolved.compareTo(other.totalSolvedProblems);
        }
        public void updateIndividualTeam(int newPenalty) {
            this.totalSolvedProblems += 1;
            this.totalPenalty += newPenalty;
        }
    }

    public Team[] allTeams;
    public HashSet<Integer> numOfbetterTeams;


    public Solution(int numTeams) {
        // TODO: Construct/Initialise your data structures here
        this.numOfbetterTeams = new HashSet<Integer>();
        this.allTeams = new Team[numTeams];

        // Initialisation of all teams in the teamsarray which will store
        // all the information of the teams - their total penalty, number of solved
        // problems with the array index + 1 representing the team number
        for(int i = 0; i < numTeams; i++)
            this.allTeams[i] = new Team(0, 0, i + 1);
    }

    public int update(int team, int newPenalty) {

        // TODO: Implement your update function here
        allTeams[team - 1].updateIndividualTeam(newPenalty);

        // For team 1 case, consider all other teams's points and penalties
        // relative to team 1 when team 1 gets penalty and score.
        if (team - 1 == 0) {
            HashSet<Integer> teamsWhoAreWorse = new HashSet<Integer>();

            for (int i : numOfbetterTeams)
                if (allTeams[0].compareTo(allTeams[i]) >= 0) teamsWhoAreWorse.add(i);

            for (int j : teamsWhoAreWorse)
                numOfbetterTeams.remove(j);
        }

        // Else consider only the team with respect to the only team you're interested in - team 1
        // If better add to hashSet which is the number of better teams.
        // Rank of a team = numofbetterteams + 1
        if (allTeams[team - 1].compareTo(allTeams[0]) > 0)
            numOfbetterTeams.add(team - 1);

        return numOfbetterTeams.size() + 1;
    }

}