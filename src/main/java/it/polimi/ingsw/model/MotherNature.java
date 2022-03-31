package it.polimi.ingsw.model;

import java.util.Random;

public class MotherNature {
    private int island;

    public MotherNature(){
        Random generator = new Random();
        int island = generator.nextInt(12)+1;
    }

    /*public int computeInfluence(){

    }*/

    public int getIslandIndex(){
        return island;
    }

    public void setIsland(int pos){
        island = pos;
    }
}
