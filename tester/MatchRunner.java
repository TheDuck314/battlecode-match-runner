package tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class MatchRunner {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Team A: ");
        String teamA = in.readLine().trim();
        System.out.print("Team B: ");
        String teamB = in.readLine().trim();

        // String[] maps = { "bloodbath", "Cairo", "speakers" };
        String[] maps = getAllMaps();

        GameDescriptor[] descriptors = MatchRunner.runMatch(teamA, teamB, maps);
        
        MatchResultInterface.display(teamA, teamB, descriptors);
    }

    public static GameDescriptor[] runMatch(String teamA, String teamB, String[] maps) throws IOException, ClassNotFoundException {
        System.out.format("Running match between %s and %s...\n", teamA, teamB);

        String matchDir = pickMatchDir(teamA, teamB);
        new File(matchDir).mkdirs();

        String[] gameFiles = new String[maps.length];
        GameDescriptor[] descriptors = new GameDescriptor[maps.length];
        for (int i = 0; i < maps.length; i++) {
            gameFiles[i] = gameFilename(matchDir, maps[i]);
            GameRunner.run(teamA, teamB, maps[i], gameFiles[i]);
            descriptors[i] = GameAnalyzer.analyze(gameFiles[i]);
        }
       
        return descriptors;
    }
    
    private static String pickMatchDir(String teamA, String teamB) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int time24 = 100 * c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE);
        return String.format("./matches/%d_%d_%04d__%s_vs_%s", month, day, time24, teamA, teamB);
    }

    private static String gameFilename(String matchDir, String map) {
        return String.format("%s/%s.rms", matchDir, map);
    }

    public static String[] getAllMaps() {
        File mapsDir = new File("./maps");
        String[] filenames = mapsDir.list();
        String[] maps = new String[filenames.length];
        for (int i = 0; i < filenames.length; i++) {
            assert filenames[i].endsWith(".xml");
            maps[i] = filenames[i].substring(0, filenames[i].length() - ".xml".length());
        }
        return maps;
    }
}
