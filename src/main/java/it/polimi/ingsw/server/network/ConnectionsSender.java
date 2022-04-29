package it.polimi.ingsw.server.network;

import java.util.ArrayList;

public class ConnectionsSender {
    ArrayList<EchoServerClientHandler> clients;

    public ConnectionsSender() {
        clients = new ArrayList<>();
    }

    public int getNewID() {
        return clients.size();
    }

    public synchronized void addClient(EchoServerClientHandler e) {
        clients.add(e);
    }

    public synchronized void sendToAll(String msg) {
        for (EchoServerClientHandler c : clients) {
            c.getOut().println(msg);
            c.getOut().flush();
        }
    }

    public synchronized void sendToOne(String msg, int ID) {
        clients.get(ID).getOut().println(msg);
        clients.get(ID).getOut().flush();
    }
}
