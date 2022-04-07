package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.model.ProfessorGroup;

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
    public Player playerWithMostInfluence(ArrayList<Player> players, IslandManager im, ProfessorGroup professors) {
        Archipelago a = im.getArchipelagoByIslandIndex(island);
        int maxInfluence = -1;
        Player winningPlayer = null;
        int influence;
        for(Player p: players) {
            influence = a.playerInfluence(p, professors);
            if(influence == maxInfluence) {
                winningPlayer = null;
            } else if (influence>maxInfluence) {
                maxInfluence = influence;
                winningPlayer = p;
            }
        }
        return winningPlayer;
    }

   /* /**
     * mother nature puts the tower on the island she's on if one of the Players has more students on it then others

    public void computeInfluence() {
        Archipelago a = Board.instance().getIslandManager().getArchipelagoByIslandIndex(island);
        int maxInfluence = -1;
        Player winningPlayer = null;
        int influence;
        for(Player p : ExpertGameManager.instance().getPlayers()) {
            influence = a.playerInfluence(p);
            if(influence==maxInfluence) {
                winningPlayer = null;
            } else if(influence>maxInfluence) {
                maxInfluence = influence;
                winningPlayer = p;
            }
        }
        if(winningPlayer!=null && winningPlayer.getTower()!=Board.instance().getIslandManager().getIsland(island).getTower()) {

            winningPlayer.getDashboard().buildTower();
        }
    }*/

    /*
    public int computeInfluence(Player p){
        ProfessorGroup p = Board.instance().getProfessorGroup();
        Tower t= player.getTower();
        StudentGroup s;
        Island i = Board.instance().getIslandManager().getIsland(island);
        s = i.getStudents();

        int res = 0;
        for(Colour c: Colour.values()){
            if(p.getTower(c).equals(t))
                res+=s.getQuantityColour(c);
        }

        return res;
    }*/

    public int getIslandIndex(){
        return island;
    }

    public void setIsland(int pos){
        island = pos;
    }
}
