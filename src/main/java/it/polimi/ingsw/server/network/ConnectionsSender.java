package it.polimi.ingsw.server.network;

import java.util.ArrayList;
import java.util.Collections;

public class ConnectionsSender {
    ArrayList<EchoServerClientHandler> clients;
    ArrayList<Boolean> connected;

    public ConnectionsSender() {
        clients = new ArrayList<>();
        connected = new ArrayList<>();
    }

    public synchronized int getNewID() {
        return clients.size();
    }

    public synchronized void addClient(EchoServerClientHandler e) {
        clients.add(e);
        connected.add(true);
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

    public synchronized boolean isStillConnected(int ID) {
        return connected.get(ID);
    }

    public synchronized void setStillConnected(int ID, boolean val) {
        System.out.println("setin");
        connected.set(ID,val);
        System.out.println("setout");
    }

    public synchronized void setAllDisconnected() {
        System.out.println("setalldisconnected");
        Collections.fill(connected, false);
    }

    public synchronized void eraseDisconnected() {
        System.out.println("erasedisconnected");
        /*for (int i=0;i<connected.size();i++) {
            if(!connected.get(i)) {

            }
        }*/
    }
}
