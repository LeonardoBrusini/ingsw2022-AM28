package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import it.polimi.ingsw.network.AddPlayerResponse;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.model.players.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ConnectionList {
    private ArrayList<EchoServerClientHandler> clients;
    private ArrayList<Boolean> connected;
    private ExpertGameManager gameManager;
    private int numOfActualPlayers;
    private HashMap<String,Integer> savedUsernames;
    private static ConnectionList instance;
    public boolean waitForPlayers;

    private ConnectionList() {
        clients = new ArrayList<>();
        connected = new ArrayList<>();
        savedUsernames = new HashMap<>();
        numOfActualPlayers = 0;
        gameManager = new ExpertGameManager();
        waitForPlayers = false;
    }

    public static ConnectionList instance() {
        if(instance==null) instance = new ConnectionList();
        return instance;
    }

    public void move(int playerID, Integer integer) {
        clients.set(integer,clients.get(playerID));
        clients.remove(playerID);
    }

    public synchronized int getNewID() {
        return clients.size();
    }

    public synchronized void addClient(EchoServerClientHandler e) {
        e.setPlayerID(clients.size());
        clients.add(e);
        connected.add(true);
        notifyAll();
        System.out.println("player aggiunto");
    }

    public synchronized void sendToAll(String msg) {
        for (EchoServerClientHandler c : clients) {
            if(c!=null) {
                c.getOut().println(msg);
                c.getOut().flush();
            }
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
        if(gameManager.isGameStarted()) {
            for (int i=0;i<numOfActualPlayers;i++) {
                if(clients.get(i)!=null && !connected.get(i)) {
                    try {
                        clients.get(i).getSocket().close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    savedUsernames.put(clients.get(i).getConnectionManager().getUsername(),i);
                    clients.set(i,null);
                    gameManager.getPlayers().get(i).setConnected(false);
                    if(gameManager.getTurnManager().getCurrentPlayer()==i && !noClients()) {
                        gameManager.getTurnManager().nextPlayer(gameManager.getPlayers(),gameManager.getBoard());
                        sendToAllLogged(new Gson().toJson(gameManager.getTurnManager().getTurnStatus()));
                    }
                }
            }
            if(noClients()) {
                System.out.println("reset");
                clients = new ArrayList<>();
                connected = new ArrayList<>();
                savedUsernames = new HashMap<>();
                numOfActualPlayers = 0;
                gameManager = new ExpertGameManager();
                waitForPlayers = false;
                return;
            }
        } else {
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
        }
        resetIDs();
    }

    private void sendToAllLogged(String msg) {
        for (EchoServerClientHandler c : clients) {
            if(c!=null && !c.getConnectionManager().doesNeedUsername()) {
                c.getOut().println(msg);
                c.getOut().flush();
            }
        }
    }

    private boolean noClients() {
        for (int i=0;i<numOfActualPlayers;i++){
            if(clients.get(i)!=null) return false;
        }
        return true;
    }

    public synchronized void resetIDs() {
        for (int i=0;i<clients.size();i++)
            if(clients.get(i)!=null)
                clients.get(i).setPlayerID(i);
    }

    public synchronized int getNumConnections() {
        return clients.size();
    }

    public synchronized void setNicknames() {
        ArrayList<Player> p = gameManager.getPlayers();
        for(int i=0;i<p.size();i++){
            p.get(i).setNickname(clients.get(i).getConnectionManager().getUsername());
        }
    }

    public synchronized ArrayList<EchoServerClientHandler> getClients() {
        return clients;
    }

    public synchronized void sendFirstStatus() {
        CurrentStatus cs = gameManager.getFullCurrentStatus();
        int i;
        for (i=0;i<gameManager.getPlayers().size();i++) {
            cs.setPlayerID(clients.get(i).getPlayerID());
            clients.get(i).getOut().println(new Gson().toJson(cs));
            clients.get(i).getOut().flush();
        }
        for(;i<clients.size();i++) {
            clients.get(i).getOut().println(StatusCode.KICKEDOUT.toJson());
            clients.get(i).getOut().flush();
        }
    }

    public HashMap<String,Integer> getSavedUsernames() {
        return savedUsernames;
    }

    public void setNumOfActualPlayers(int numOfActualPlayers) {
        this.numOfActualPlayers = numOfActualPlayers;
    }

    public ExpertGameManager getGameManager() {
        return gameManager;
    }

    public void setWaitForPlayers(boolean waitForPlayers) {
        this.waitForPlayers = waitForPlayers;
    }

    public boolean isWaitForPlayers() {
        return waitForPlayers;
    }

    public synchronized void orderClients() {
        ArrayList<EchoServerClientHandler> wUsername = new ArrayList<>();
        ArrayList<EchoServerClientHandler> wOutUsername = new ArrayList<>();
        for (EchoServerClientHandler e: clients){
            if(e!=null) {
                if(e.getConnectionManager().doesNeedUsername()) wOutUsername.add(e);
                else wUsername.add(e);
            }
        }
        for (int i=0;i<wUsername.size();i++) {
            clients.set(i,wUsername.get(i));
        }
        for (int i=wUsername.size();i<clients.size();i++) {
            clients.set(i,wOutUsername.get(i-wUsername.size()));
        }
        resetIDs();
    }

    public int getNumOfLoggedPlayers() {
        int i=0;
        for (EchoServerClientHandler e:clients) {
            if(e!=null && !e.getConnectionManager().doesNeedUsername()) i++;
        }
        return i;
    }
}
