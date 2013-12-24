package tester;

import java.io.IOException;

import battlecode.server.Config;
import battlecode.server.Server;
import battlecode.server.controller.Controller;
import battlecode.server.controller.ControllerFactory;
import battlecode.server.proxy.Proxy;
import battlecode.server.proxy.ProxyFactory;

public class GameRunner {
    public static void run(String teamA, String teamB, String map, String gameFile) throws IOException {
        System.out.format("Running game between %s and %s on %s...\n", teamA, teamB, map);   
        
        //Randomize who is actually A and who is actually B
        if(Math.random() < 0.5) {
            String tmp = teamA;
            teamA = teamB;
            teamB = tmp;
        }
        
        Config bcConfig = Config.getGlobalConfig();
        //bcConfig.set("bc.engine.debug-methods", "false");
        bcConfig.set("bc.game.maps", map);
        bcConfig.set("bc.game.team-a", teamA);
        bcConfig.set("bc.game.team-b", teamB);
        bcConfig.set("bc.server.mode", "headless");

        Controller controller = ControllerFactory.createHeadlessController(bcConfig);
        Proxy[] proxies = new Proxy[] { ProxyFactory.createProxyFromFile(gameFile), };
        Server bcServer = new Server(bcConfig, Server.Mode.HEADLESS, controller, proxies);
        controller.addObserver(bcServer);
        bcServer.run();
    }
}