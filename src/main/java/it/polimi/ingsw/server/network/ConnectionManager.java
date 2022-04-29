package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.AddPlayerResponse;
import it.polimi.ingsw.network.GameParameters;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.model.players.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ConnectionManager {
    private final ExpertGameManager gameManager;
    private final Gson parser;
    private boolean toAllResponse;

    public ConnectionManager(ExpertGameManager gameManager){
        this.gameManager= gameManager;
        this.parser = new Gson();
    }

    public String manageMessage(String message, int playerID){
        toAllResponse = false;
        if(gameManager.isGameStarted()){
            return manageInGameMessage(message);
        }else{
            return managePreGameMessage(message);
        }
    }

    private String manageInGameMessage(String message) {
        return null;
    }

    public String managePreGameMessage(String message){
        try {
            GameParameters tmp = parser.fromJson(message,GameParameters.class);
            boolean expert;
            if (tmp.getGameMode().equals("expert")) expert=true;
            else if (tmp.getGameMode().equals("simple")) expert = false;
            else return StatusCode.ILLEGALARGUMENT.toJson();
            if(tmp.getNumPlayers()<2 || tmp.getNumPlayers()>3) return StatusCode.ILLEGALARGUMENT.toJson();
            gameManager.newGame(expert,tmp.getNumPlayers());
            toAllResponse = true;
            return generateFullCurrentStatus();
        } catch (JsonSyntaxException e) {
            String tmp = parser.fromJson(message,String.class);
            ArrayList<Player> players= gameManager.getPlayers();
            if(players.size()<3){
                for (Player p : players)
                    if (p.getNickname().equals(tmp)) {
                        return StatusCode.ALREADYLOGGED.toJson();
                    }
                gameManager.addPlayer(tmp);
                return generateCorrectAddPlayerResponse();
            } else {
                return StatusCode.FULL_LOBBY.toJson();
            }
        }
    }

    private String generateFullCurrentStatus() {
        //not yet implemented;
        return null;
    }

    public String generateCorrectAddPlayerResponse(){
        AddPlayerResponse response=new AddPlayerResponse();
        response.setStatus(0);
        response.setUserID(gameManager.getNumPlayers());
        return parser.toJson(response);
    }

    public ExpertGameManager getExpertGameManager(){
        return this.gameManager;
    }

    public boolean isToAllResponse() {
        return toAllResponse;
    }
}
