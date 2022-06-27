package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.commands.CommandList;
import it.polimi.ingsw.server.controller.commands.CommandStrategy;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;

public class ConnectionManager {
    private final Gson parser;
    private boolean toAllResponse;
    private boolean firstStatus;
    private boolean needUsername;
    private String username;

    public ConnectionManager(){
        parser = new Gson();
        needUsername = true;
        firstStatus = false;
    }

    public String manageMessage(String message, int playerID){
        System.out.println("MANAGE MESSAGE");
        toAllResponse = false;
        firstStatus = false;
        if (message.equals("")) return StatusCode.ILLEGALARGUMENT.toJson();
        if(ConnectionList.instance().getGameManager().isGameStarted()){
            System.out.println("MANAGING IN-GAME MESSAGE");
            return manageInGameMessage(message,playerID);
        }else{
            return managePreGameMessage(message,playerID);
        }
    }

    private String manageInGameMessage(String message, int playerID) {
        if(needUsername) {
            try {
                username = parser.fromJson("\""+message+"\"",String.class);
                if(ConnectionList.instance().getSavedUsernames().containsKey(username)) {
                    System.out.println("Player "+username+" is trying to reconnect");
                    CurrentStatus fullStatus = ConnectionList.instance().getGameManager().getFullCurrentStatus();
                    int index = ConnectionList.instance().getSavedUsernames().get(username);
                    fullStatus.setPlayerID(index);
                    ArrayList<ServerClientHandler> clients = ConnectionList.instance().getClients();
                    System.out.println("PlayerID attuale: "+playerID+", ID precedente: "+index);
                    clients.set(index,clients.get(playerID));
                    clients.remove(playerID);
                    clients.get(index).setPlayerID(index);
                    ConnectionList.instance().setStillConnected(index,true);
                    ConnectionList.instance().getGameManager().getPlayers().get(index).setConnected(true);
                    ConnectionList.instance().getSavedUsernames().remove(username);
                    needUsername = false;
                    System.out.println("Sending status to "+username);
                    return new Gson().toJson(fullStatus);
                } else {
                    return StatusCode.GAMESTARTED.toJson();
                }
            } catch (JsonSyntaxException e) {
                return StatusCode.INVALIDUSERNAME.toJson();
            }
        }
        try {
            System.out.println("PARSING COMMAND");
            Command c = parser.fromJson(message,Command.class);
            CommandList command = CommandList.valueOf(c.getCmd());
            System.out.println("read command: "+ command);
            CommandStrategy cs = command.getCmd();
            StatusCode sc = cs.resolveCommand(ConnectionList.instance().getGameManager(), c);
            if(sc == null) {
                toAllResponse = true;
                System.out.println("trying to get status");
                return command.getCmd().getUpdatedStatus(ConnectionList.instance().getGameManager(), c);
            }
            else return sc.toJson();
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            return StatusCode.ILLEGALARGUMENT.toJson();
        }
    }

    private String managePreGameMessage(String message, int playerID){
        if(needUsername) {
            try {
                username = parser.fromJson("\""+message+"\"",String.class);
                ArrayList<Player> players = ConnectionList.instance().getGameManager().getPlayers();
                if(players.size()<3){
                    for (ServerClientHandler e: ConnectionList.instance().getClients()){
                        ConnectionManager c = e.getConnectionManager();
                        if (c!=this && c.getUsername()!=null && c.getUsername().equals(username)) {
                            return StatusCode.ALREADYLOGGED.toJson();
                        }
                    }
                    if(ConnectionList.instance().getNumConnections()>players.size()) {
                        ConnectionList.instance().getGameManager().addPlayer();
                    }
                    needUsername = false;
                    ConnectionList.instance().sendToOne(generateCorrectAddPlayerResponse(playerID),playerID);
                } else {
                    if(ConnectionList.instance().getClients().size()>players.size()) return StatusCode.FULL_LOBBY.toJson();
                    else ConnectionList.instance().sendToOne(generateCorrectAddPlayerResponse(playerID),playerID);
                }
                GameParameters gp = ConnectionList.instance().getGameParameters();
                if(gp!=null && ConnectionList.instance().getGameManager().getPlayers().size()==gp.getNumPlayers()) {
                    System.out.println("STARTING GAME");
                    ConnectionList.instance().getGameManager().newGame(gp.getGameMode().equals("expert"),gp.getNumPlayers());
                    ConnectionList.instance().setNumOfActualPlayers(gp.getNumPlayers());
                    ConnectionList.instance().setNicknames();
                    firstStatus = true;
                    ConnectionList.instance().sendFirstStatus();
                }
                return null;
            } catch (JsonSyntaxException e) {
                return StatusCode.INVALIDUSERNAME.toJson();
            }
        } else {
            try {
                GameParameters tmp = parser.fromJson(message,GameParameters.class);
                if(playerID!=0) return StatusCode.NOTFIRSTPLAYER.toJson();
                boolean expert;
                if (tmp.getGameMode().equals("expert")) expert=true;
                else if (tmp.getGameMode().equals("simple")) expert = false;
                else return StatusCode.ILLEGALARGUMENT.toJson();
                if(tmp.getNumPlayers()<2 || tmp.getNumPlayers()>3) return StatusCode.ILLEGALARGUMENT.toJson();
                ConnectionList.instance().orderClients();
                int loggedPlayers = ConnectionList.instance().getNumOfLoggedPlayers();
                if(loggedPlayers<tmp.getNumPlayers()) {
                    ConnectionList.instance().setGameParameters(tmp);
                    System.out.println("SAVED PARAMETERS");
                    return StatusCode.WAITINGFORPLAYERS.toJson();
                }
                ConnectionList.instance().getGameManager().newGame(expert,tmp.getNumPlayers());
                ConnectionList.instance().setNumOfActualPlayers(tmp.getNumPlayers());
                ConnectionList.instance().setNicknames();
                firstStatus = true;
                ConnectionList.instance().sendFirstStatus();
                return null;
            } catch (JsonSyntaxException e) {
                return StatusCode.ILLEGALARGUMENT.toJson();
            }
        }
    }

    public String generateCorrectAddPlayerResponse(int playerID){
        AddPlayerResponse response=new AddPlayerResponse();
        response.setStatus(0);
        response.setFirst(playerID == 0);
        return parser.toJson(response);
    }

    public boolean isToAllResponse() {
        return toAllResponse;
    }

    public boolean doesNeedUsername() {
        return needUsername;
    }

    public String getUsername() {
        return username;
    }

    public boolean isFirstStatus() {
        return firstStatus;
    }
}
