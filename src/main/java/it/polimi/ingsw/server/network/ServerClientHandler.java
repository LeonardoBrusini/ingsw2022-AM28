package it.polimi.ingsw.server.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientHandler implements Runnable {
    private final Socket socket;
    private int playerID;
    private final Scanner in;
    private final PrintWriter out;
    private final ConnectionManager connectionManager;

    public ServerClientHandler(Socket socket, int playerID) {
        this.socket = socket;
        this.connectionManager = new ConnectionManager();
        this.playerID = playerID;
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
       // try {
            String line, outputString;
            while (true) {
                if(socket.isClosed()) break;
                line = in.nextLine();
                if (line.equals("pong")) {
                    ConnectionList.instance().setStillConnected(playerID,true);
                } else {
                    System.out.println(line);
                    outputString = connectionManager.manageMessage(line, playerID);
                    if(!connectionManager.isFirstStatus() && outputString!=null) {
                        if(connectionManager.isToAllResponse()) {
                            ConnectionList.instance().sendToAll(outputString);
                        } else {
                            ConnectionList.instance().sendToOne(outputString,playerID);
                        }
                    }
                }
            }
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

    public int getPlayerID() {
        return playerID;
    }
}
