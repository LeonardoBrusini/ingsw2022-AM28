package it.polimi.ingsw.model;

public class Island extends AbstractIsland{

    public Island(boolean PreMotNat, int posIndex){
        this.presenceMotherNature=PreMotNat;
        this.studentGroup = new StudentGroup(0);
        this.numTower = 0;
        this.posIndex = posIndex;
    }

    public Island(int posIndex){
        this.presenceMotherNature=false;
        this.studentGroup = new StudentGroup(0);
        this.numTower = 0;
        this.posIndex = posIndex;
    }



}
