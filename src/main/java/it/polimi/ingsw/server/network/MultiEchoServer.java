package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.controller.ExpertGameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiEchoServer {
    ConnectionsSender connections;
    private final int port;

    public MultiEchoServer(int port) {
        this.port = port;
        connections = new ConnectionsSender();
    }

    public void startServer() {
        ExpertGameManager gameManager = new ExpertGameManager();
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                EchoServerClientHandler e = new EchoServerClientHandler(socket,gameManager, connections.getNewID(), connections);
                connections.addClient(e);
                executor.submit(e);
            } catch(IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        MultiEchoServer echoServer = new MultiEchoServer(1234);
        echoServer.startServer();
    }
}
