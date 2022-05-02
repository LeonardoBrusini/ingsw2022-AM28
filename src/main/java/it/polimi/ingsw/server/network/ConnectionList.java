package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import it.polimi.ingsw.network.AddPlayerResponse;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.model.players.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ConnectionList {
    private final ArrayList<EchoServerClientHandler> clients;
    private final ArrayList<Boolean> connected;

    public ConnectionList() {
        clients = new ArrayList<>();
        connected = new ArrayList<>();
    }

    public synchronized int getNewID() {
        return clients.size();
    }

    public synchronized void addClient(EchoServerClientHandler e) {
        e.setPlayerID(clients.size());
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
        if(clients.size()>0) {
            clients.get(ID).getOut().println(msg);
            clients.get(ID).getOut().flush();
        }
    }

    public synchronized boolean isStillConnected(int ID) {
        return connected.get(ID);
    }

    public synchronized void setStillConnected(int ID, boolean val) {
        connected.set(ID,val);
    }

    public synchronized void setAllDisconnected() {
        Collections.fill(connected, false);
    }

    public synchronized void eraseDisconnected() {
        int i=connected.size()-1;
        while (i>=0) {
            if(!connected.get(i)) {
                try {
                    clients.get(0).getSocket().close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                clients.remove(0);
                connected.remove(0);
                if(i==0 && clients.size()>0 && !clients.get(0).getConnectionManager().doesNeedUsername()) {
                    AddPlayerResponse a = new AddPlayerResponse();
                    a.setStatus(0);
                    a.setFirst(true);
                    sendToOne(new Gson().toJson(a),0);
                }
            }
            i--;
        }
        resetIDs();
    }

    public synchronized void resetIDs() {
        for (int i=0;i<clients.size();i++) {
            clients.get(i).setPlayerID(i);
        }
    }

    public synchronized int getNumConnections() {
        return clients.size();
    }

    public synchronized void setNickNames(ExpertGameManager gameManager) {
        ArrayList<Player> p = gameManager.getPlayers();
        for(int i=0;i<p.size();i++){
            p.get(i).setNickname(clients.get(i).getConnectionManager().getUsername());
        }
    }

    public ArrayList<EchoServerClientHandler> getClients() {
        return clients;
    }
}
