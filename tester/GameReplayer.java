package tester;

import java.io.IOException;

import javax.swing.JFrame;

import battlecode.client.ClientProxy;
import battlecode.client.StreamClientProxy;
import battlecode.client.viewer.MatchViewer;

public class GameReplayer {
    public static void replay(String rmsFilename) throws IOException {
        ClientProxy proxy = new StreamClientProxy(rmsFilename);
        final JFrame frame = new JFrame("battlecode");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        battlecode.client.Main.showViewer(frame, new MatchViewer(proxy, false));
    }
}
