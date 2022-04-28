package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import it.polimi.ingsw.network.AddPlayerResponse;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    private ExpertGameManager expertGameManager;
    private boolean gameStarted;
    private Gson parser;

    public ConnectionManager(){
        this.expertGameManager= new ExpertGameManager();
        this.gameStarted=false;
        this.parser = new Gson();
    }

    public ExpertGameManager getExperGameManager(){
        return this.expertGameManager;
    }

    public String manageMessage(String message){
        if(gameStarted){

        }else{
            String tmp = parser.fromJson(message,String.class);
            ArrayList<Player> players= expertGameManager.getPlayers();

            if(players.size()<3){
                for (Player p : players)
                    if (p.getNickname().equals(tmp)) {
                        return generateError(StatusCode.ALREADYLOGGED);
                    }
                expertGameManager.addPlayer(tmp);

            }else{
                return generateError(StatusCode.FULL_LOBBY);
            }
        }

        return generateCorrect();

    }

    public String generateError(StatusCode p){
        AddPlayerResponse response = new AddPlayerResponse();

        response.setStatus(p.getStatusCode());
        response.setErrorMessage(p.getErrorMessage());

        return parser.toJson(response);
    }

    public String generateCorrect(){
        AddPlayerResponse response=new AddPlayerResponse();
        response.setStatus(0);
        response.setUserID(expertGameManager.getNumPlayers());
        return parser.toJson(response);
    }
}
