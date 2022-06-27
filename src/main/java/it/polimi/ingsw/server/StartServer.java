package it.polimi.ingsw.server;

import it.polimi.ingsw.server.network.GameServer;

public class StartServer {
    public static void main(String[] args) {
        new GameServer(1234).startServer();
    }
}
