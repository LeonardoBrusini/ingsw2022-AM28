package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.ProfessorGroup;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;
import java.util.Random;

public class MotherNature {
    private int island;

    public MotherNature(){
        Random generator = new Random();
        island = generator.nextInt(12)+1;
    }

    /**
     * choose the player with more influence on the archipelago mother nature is on
     * @param players the players of the game
     * @param im the island manager
     * @return the player with more influence, if two or more has the same max influence then null
     */
    public Player playerWithMostInfluence(ArrayList<Player> players, IslandManager im, ProfessorGroup professors, ArrayList<CharacterCard> cards) throws IllegalArgumentException{
        if(players==null || im==null || professors==null) throw new IllegalArgumentException();
        Archipelago a = im.getArchipelagoByIslandIndex(island);
        int maxInfluence = -1;
        Player winningPlayer = null;
        int influence;
        for(Player p: players) {
            influence = a.playerInfluence(p.getTower(),professors, cards);
            if(influence == maxInfluence) {
                winningPlayer = null;
            } else if (influence>maxInfluence) {
                maxInfluence = influence;
                winningPlayer = p;
            }
        }
        return winningPlayer;
    }

    public int getIslandIndex(){
        return island;
    }

    public void setIsland(int pos){
        island = pos;
    }
}
