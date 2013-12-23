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
}
