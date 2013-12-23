package tester;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.engine.signal.Signal;
import battlecode.serial.ExtensibleMetadata;
import battlecode.serial.GameStats;
import battlecode.serial.MatchFooter;
import battlecode.serial.MatchHeader;
import battlecode.serial.RoundDelta;
import battlecode.serial.RoundStats;
import battlecode.server.proxy.XStreamProxy;
import battlecode.world.signal.DeathSignal;
import battlecode.world.signal.SpawnSignal;

public class GameAnalyzer {
    public static GameDescriptor analyze(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream input = XStreamProxy.getXStream().createObjectInputStream(new GZIPInputStream(new FileInputStream(filename)));

        @SuppressWarnings("unused")
        MatchHeader header = (MatchHeader) input.readObject();
        // GameMap map = (GameMap) header.getMap();
        
        ExtensibleMetadata metadata = (ExtensibleMetadata) input.readObject();
        String teamA = (String) metadata.get("team-a", "?");
        String teamB = (String) metadata.get("team-b", "?");
        String[] maps = (String[]) metadata.get("maps", new String[] {});
        assert maps.length == 1;
        String map = maps[0];

        int[][] botCount = new int[2][RobotType.values().length];
        HashMap<Integer, Team> teamById = new HashMap<Integer, Team>();
        HashMap<Integer, RobotType> typeById = new HashMap<Integer, RobotType>();

        // Loop over rounds of the game
        Object o = input.readObject();
        while (o instanceof RoundDelta) {
            RoundDelta round = (RoundDelta) o;

            for (Signal s : round.getSignals()) {
                if (s instanceof SpawnSignal) {
                    SpawnSignal spawn = (SpawnSignal) s;
                    int robotId = spawn.getRobotID();
                    Team robotTeam = spawn.getTeam();
                    RobotType robotType = spawn.getType();

                    teamById.put(robotId, robotTeam);
                    typeById.put(robotId, robotType);
                    botCount[robotTeam.ordinal()][robotType.ordinal()]++;
                } else if (s instanceof DeathSignal) {
                    int id = ((DeathSignal) s).getObjectID();

                    botCount[teamById.get(id).ordinal()][typeById.get(id).ordinal()]--;
                    teamById.remove(id);
                }
            }

            for (Team t : new Team[] { Team.A, Team.B }) {
                int i = t.ordinal();
                //System.out.format("Team %s: %d soldiers, %d suppliers, %d generators\n", t.toString(), botCount[i][RobotType.SOLDIER.ordinal()],
                //        botCount[i][RobotType.SUPPLIER.ordinal()], botCount[i][RobotType.GENERATOR.ordinal()]);
            }

            @SuppressWarnings("unused")
            RoundStats stats = (RoundStats) input.readObject();
            
            o = input.readObject(); // RoundDelta, probably
        }

        GameStats gameStats = (GameStats) o;
        //System.out.println("timeToFirstKill = " + gameStats.getTimeToFirstKill());

        MatchFooter footer = (MatchFooter) input.readObject();
        Team winner = footer.getWinner();

        return new GameDescriptor(teamA, teamB, winner, map, filename);
    }
}
