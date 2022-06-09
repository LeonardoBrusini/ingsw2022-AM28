package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLIClient;
import it.polimi.ingsw.client.gui.GUIClient;
import it.polimi.ingsw.server.network.MultiEchoServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if(args.length==0) {
            System.setProperty("prism.allowhidpi", "false");
            GUIClient.main(args);
        } else if(args[0].equals("-s")) {
            if(args.length>1) {
                new MultiEchoServer(Integer.parseInt(args[1]));
            } else {
                new MultiEchoServer(1234).startServer();
            }
        } else if(args[0].equals("-c")) {
            if(args.length>1) {
                CLIClient.start(args[1],Integer.parseInt(args[2]));
            } else {
                CLIClient.start("127.0.0.1",1234);
            }
        }
    }
}
