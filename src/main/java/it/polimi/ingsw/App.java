package it.polimi.ingsw;

import it.polimi.ingsw.client.network.EchoClient;
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
            System.out.println("GUI APP");
        } else if(args[0].equals("-s")) {
            new MultiEchoServer(1234).startServer();
        } else if(args[0].equals("-c")) {
            EchoClient.start();
        }
    }
}
