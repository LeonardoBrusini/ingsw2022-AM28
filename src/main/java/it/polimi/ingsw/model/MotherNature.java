package it.polimi.ingsw.model;

import java.util.Random;

public class MotherNature {
    private int island;
    private Random generator;

    public MotherNature(){
        generator = new Random();
        int island = generator.nextInt(12);
    }
    /*
    public int computeInfluence(){
        Board d;
        d = Board.istance();
        d.getInfluence(island,giocatore);

    }*/

    public int getIslandIndex(){
        return this.island;
    }

    public void setIsland(int pos){
        this.island = pos;
    }
}
