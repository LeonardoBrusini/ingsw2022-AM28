package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.controller.ExpertGameManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoServerClientHandler implements Runnable {
    private Socket socket;
    private ConnectionManager connectionManager;

    public EchoServerClientHandler(Socket socket, ConnectionManager c) {
        this.socket = socket;
        this.connectionManager = c;
    }
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
// Leggo e scrivo nella connessione finche' non ricevo "quit"
            while (true) {
                String line = in.nextLine();
                String outPutString = connectionManager.manageMessage(line);

                if (line.equals("quit")) {
                    break;
                } else {
                    out.println("Received: " + outPutString);
                    out.flush();
                }
            }



// Chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
