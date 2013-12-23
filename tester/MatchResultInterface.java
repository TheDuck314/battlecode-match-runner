package tester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import info.clearthought.layout.TableLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import battlecode.common.Team;

public class MatchResultInterface {
    public static void display(String teamA, String teamB, GameDescriptor[] descriptors) {
        int numGames = descriptors.length;
        
        JFrame frame = new JFrame("Match results");
        frame.setSize(450, 450);
        
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        double columnWidths[] = new double[]{10, 75, 75, 75, 75, 75, 10};
        double rowHeights[] = new double[numGames+2];
        for(int i = 0; i < numGames+2; i++) rowHeights[i] = 20;
        double[][] layout = new double[][]{columnWidths, rowHeights};

        panel.setLayout(new TableLayout(layout));


        //String label[] = {"(1,1)", "(1,5)", "(1,3)", "(5,3)", "(3,3)"};
        //JButton button[] = new JButton[label.length];

        //for (int i = 0; i < label.length; i++) {
        //    button[i] = new JButton(label[i]);
        //}


        //frame.add(button[0], "1, 1");
        //frame.add(button[1], "1, 5");
        //frame.add(button[2], "1, 3");
        //frame.add(button[3], "5, 3");
        //frame.add(button[4], "3, 3");
        
        String vs = String.format("%s vs %s", teamA, teamB);
        String result = String.format("Match result: %s", calculateRecord(teamA, teamB, descriptors));
        panel.add(new JLabel(vs), "1, 0, 2, 0");
        panel.add(new JLabel(result), "3, 0, 4, 0");
        
        panel.add(new JLabel("Map"), "1, 1");
        panel.add(new JLabel("Team A"), "2, 1");
        panel.add(new JLabel("Team B"), "3, 1");
        panel.add(new JLabel("Winner"), "4, 1");
        
        for(int i = 0; i < numGames; i++) {
            final GameDescriptor desc = descriptors[i];
            String map = desc.getMap();
            String thisA = desc.getTeamName(Team.A);
            String thisB = desc.getTeamName(Team.B);
            String winner = desc.getWinnerName();
            
            JButton watchButton = new JButton("watch");
            watchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        GameReplayer.replay(desc.getRmsFile());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            
            int row = i+2;
            panel.add(new JLabel(map), String.format("1, %d", row));
            panel.add(new JLabel(thisA), String.format("2, %d", row));
            panel.add(new JLabel(thisB), String.format("3, %d", row));
            panel.add(new JLabel(winner), String.format("4, %d", row));
            panel.add(watchButton, String.format("5, %d", row));
            
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    private static String calculateRecord(String teamA, String teamB, GameDescriptor[] descriptors) {
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
