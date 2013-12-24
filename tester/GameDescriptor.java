package tester;

import battlecode.common.Team;

public class GameDescriptor {
    private String[] teams; // {A, B}
    private Team winner;
    private String map;
    private String rmsFile;
    
    public GameDescriptor(String teamA, String teamB, Team winner, String map, String rmsFile) {
        this.teams = new String[]{teamA, teamB};
        this.winner = winner;
        this.map = map;
        this.rmsFile = rmsFile;
    }
    
    public String getWinnerName() {
        return getTeamName(winner);
    }
    
    public String getTeamName(Team team) {
        return teams[team.ordinal()];
    }

    public String getMap() {
        return map;
    }
    
    public String getRmsFile() {
        return rmsFile;
    }
    
    public static String calculateRecord(String teamA, String teamB, GameDescriptor[] descriptors) {
        int aWins = 0, bWins = 0;
        for(GameDescriptor desc : descriptors) {
            assert desc.getTeamName(Team.A).equals(teamA) || desc.getTeamName(Team.A).equals(teamB);
            assert desc.getTeamName(Team.B).equals(teamA) || desc.getTeamName(Team.B).equals(teamB);

            if(desc.getWinnerName().equals(teamA)) aWins++;
            else bWins++;
        }
        return String.format("%d-%d", aWins, bWins);
    }
}
