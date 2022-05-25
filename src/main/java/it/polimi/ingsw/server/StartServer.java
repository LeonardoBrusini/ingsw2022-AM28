package it.polimi.ingsw.server;

import it.polimi.ingsw.server.network.MultiEchoServer;

public class StartServer {
    public static void main(String[] args) {
        new MultiEchoServer(1234).startServer();
    }
}
