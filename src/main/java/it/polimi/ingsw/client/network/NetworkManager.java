package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.ClientObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientObserver menu;
    private static NetworkManager instance;

    public static NetworkManager instance() {
        if(instance==null) instance = new NetworkManager();
        return instance;
    }

    public boolean startServer(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine())!=null) {
                        if(line.equals("ping")) out.println("pong");
                        else {
                            if (menu!=null) menu.manageMessage(line);
                        }
                    }
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void send(String line) {
        out.println(line);
        out.flush();
    }

    public void setObserver(ClientObserver menu) {
        this.menu = menu;
    }
}
