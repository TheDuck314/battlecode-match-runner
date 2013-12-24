package tester;

import info.clearthought.layout.TableLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RoundRobin {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Space-separated list of teams: ");
        String teamsString = in.readLine().trim();
        String[] teams = teamsString.split(" ");

        String[][] records = new String[teams.length][teams.length];

        for (int i = 0; i < teams.length - 1; i++) {
            for (int j = i + 1; j < teams.length; j++) {
                String[] maps = MatchRunner.getAllMaps();
                GameDescriptor[] descriptors = MatchRunner.runMatch(teams[i], teams[j], maps);
                records[i][j] = GameDescriptor.calculateRecord(teams[i], teams[j], descriptors);
                records[j][i] = GameDescriptor.calculateRecord(teams[j], teams[i], descriptors);
            }
        }
        
        
        JFrame frame = new JFrame("Round robin");
        frame.setSize(450, 450);
        
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        double columnWidths[] = new double[teams.length+3];
        columnWidths[0] = 10;
        columnWidths[1] = 75;
        for(int i = 0; i < teams.length; i++) columnWidths[i+2] = 40;
        columnWidths[teams.length+2] = 10;
        
        double[] rowHeights = new double[teams.length+3];
        rowHeights[0] = 10;
        rowHeights[1] = 20;
        for(int i = 0; i < teams.length; i++) rowHeights[i+2] = 20;
        rowHeights[teams.length+2] = 10;
                        
        double[][] layout = new double[][]{columnWidths, rowHeights};
        panel.setLayout(new TableLayout(layout));

        for(int i = 0; i < teams.length; i++) {
            panel.add(new JLabel(teams[i]), String.format("%d, 1", i+2));
            panel.add(new JLabel(teams[i]), String.format("1, %d", i+2));
        }
        
        for(int i = 0; i < teams.length; i++) {
            for(int j = 0; j < teams.length; j++) {
                if(i == j) continue;
                panel.add(new JLabel(records[i][j]), String.format("%d, %d", j+2, i+2));
            }
        }
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
