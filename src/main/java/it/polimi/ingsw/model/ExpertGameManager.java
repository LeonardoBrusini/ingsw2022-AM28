package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ExpertGameManager {
    private ArrayList<Player> players;
    private static ExpertGameManager instance;


    public ExpertGameManager() {

    }

    public static ExpertGameManager instance() {
        if(instance == null) {
            instance = new ExpertGameManager();
        }
        return instance;
    }


    public int getNumPlayers() {
        return players.size();
    }
}
