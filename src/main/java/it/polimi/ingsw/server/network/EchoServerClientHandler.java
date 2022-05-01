package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.controller.ExpertGameManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoServerClientHandler implements Runnable {
    private final Socket socket;
    private final int playerID;
    private final ConnectionsSender sender;
    private final Scanner in;
    private final PrintWriter out;
    private final ConnectionManager connectionManager;

    public EchoServerClientHandler(Socket socket, ExpertGameManager gameManager, int playerID, ConnectionsSender sender) {
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
                line = in.nextLine();
                if (line.equals("pong")) {
                    System.out.println("pong recieved");
                    sender.setStillConnected(playerID,true);
                    System.out.println("player still connected");
                } else {
                    outputString = connectionManager.manageMessage(line, playerID);
                    System.out.println("command recieved");
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

    public PrintWriter getOut() {
        return out;
    }
}
