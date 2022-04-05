package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class MotherNature {
    private int island;

    public MotherNature(){
        Random generator = new Random();
        island = generator.nextInt(12)+1;
    }




    public void computeInfluence() {
        Archipelago a = Board.instance().getIslandManager().getArchipelagoByIslandIndex(island); //diventa parametro
        int maxInfluence = -1;
        Player winningPlayer = null;
        int influence;
        for(Player p : ExpertGameManager.instance().getPlayers()) { //diventa parametro
            influence = a.playerInfluence(p);
            if(influence==maxInfluence) {
                winningPlayer = null;
            } else if(influence>maxInfluence) {
                maxInfluence = influence;
                winningPlayer = p;
            }
        }
        if(winningPlayer!=null && winningPlayer.getTower()!=Board.instance().getIslandManager().getIsland(island).getTower()) { //diventa parametro

            winningPlayer.getDashboard().buildTower();
        }
    }


   /* public int computeInfluence(Player player){
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
