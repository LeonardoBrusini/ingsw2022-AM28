package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.controller.commands.CommandList;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;

public class ConnectionManager {
    private final ExpertGameManager gameManager;
    private final Gson parser;
    private boolean toAllResponse;
    private boolean needUsername;
    private String username;

    public ConnectionManager(ExpertGameManager gameManager){
        this.gameManager= gameManager;
        parser = new Gson();
        needUsername = true;
    }

    public String manageMessage(String message, int playerID, ConnectionList sender){
        toAllResponse = false;
        if(gameManager.isGameStarted()){
            return manageInGameMessage(message);
        }else{
            return managePreGameMessage(message,sender,playerID);
        }
    }

    private String manageInGameMessage(String message) {
        try {
            Command c = parser.fromJson(message,Command.class);
            CommandList command = CommandList.valueOf(c.getCmd());
            StatusCode sc = command.getCmd().resolveCommand(gameManager,c);
            if(sc == null) return command.getCmd().getUpdatedStatus(gameManager);
            else return sc.toJson();
        } catch (JsonSyntaxException e) {
            return StatusCode.ILLEGALARGUMENT.toJson();
        }
    }

    public String managePreGameMessage(String message, ConnectionList sender, int playerID){
        if(needUsername) {
            username = parser.fromJson(message,String.class);
            ArrayList<Player> players = gameManager.getPlayers();
            if(players.size()<3){
                for (EchoServerClientHandler e: sender.getClients()){
                    ConnectionManager c = e.getConnectionManager();
                    if (c!=this && c.getUsername()!=null && c.getUsername().equals(username)) {
                        System.out.println("a");
                        return StatusCode.ALREADYLOGGED.toJson();
                    }
                }

                if(sender.getNumConnections()>players.size()) {
                    gameManager.addPlayer();
                }
                needUsername = false;
                System.out.println("b");
                return generateCorrectAddPlayerResponse(playerID);
            } else {
                return StatusCode.FULL_LOBBY.toJson();
            }
        } else {
            try {
                GameParameters tmp = parser.fromJson(message,GameParameters.class);
                boolean expert;
                if (tmp.getGameMode().equals("expert")) expert=true;
                else if (tmp.getGameMode().equals("simple")) expert = false;
                else return StatusCode.ILLEGALARGUMENT.toJson();
                if(tmp.getNumPlayers()<2 || tmp.getNumPlayers()>3) return StatusCode.ILLEGALARGUMENT.toJson();
                gameManager.newGame(expert,tmp.getNumPlayers());
                sender.setNickNames(gameManager);
                toAllResponse = true;
                return parser.toJson(gameManager.getFirstCurrentStatus());
            } catch (JsonSyntaxException e) {
                return StatusCode.ILLEGALARGUMENT.toJson();
            }
        }
    }

    public String generateCorrectAddPlayerResponse(int playerID){
        AddPlayerResponse response=new AddPlayerResponse();
        response.setStatus(0);
        if(playerID==0) {
            response.setFirst(true);
        }
        return parser.toJson(response);
    }

    public ExpertGameManager getExpertGameManager(){
        return gameManager;
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
}
