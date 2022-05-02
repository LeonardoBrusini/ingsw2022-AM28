package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.controller.ExpertGameManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoServerClientHandler implements Runnable {
    private final Socket socket;
    private int playerID;
    private final ConnectionList sender;
    private final Scanner in;
    private final PrintWriter out;
    private final ConnectionManager connectionManager;

    public EchoServerClientHandler(Socket socket, ExpertGameManager gameManager, int playerID, ConnectionList sender) {
        this.socket = socket;
        this.connectionManager = new ConnectionManager(gameManager);
        this.playerID = playerID;
        this.sender = sender;
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
       // try {
            // Leggo e scrivo nella connessione finche' non ricevo "quit"
            String line, outputString;
            while (true) {
                if(socket.isClosed()) break;
                line = in.nextLine();
                if (line.equals("pong")) {
                    sender.setStillConnected(playerID,true);
                } else {
                    outputString = connectionManager.manageMessage(line, playerID, sender);
                    if(connectionManager.isToAllResponse()) {
                        sender.sendToAll(outputString);
                    } else {
                        sender.sendToOne(outputString,playerID);
                    }
                }
            }
            // Chiudo gli stream e il socket
            //in.close();
            //out.close();
            //socket.close();
       // } catch (IOException e) {
        //    System.err.println(e.getMessage());
       // }
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public PrintWriter getOut() {
        return out;
    }

    public Socket getSocket() {
        return socket;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
