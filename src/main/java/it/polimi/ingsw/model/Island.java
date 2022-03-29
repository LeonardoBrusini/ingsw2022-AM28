package it.polimi.ingsw.model;

public class Island extends AbstractIsland{

    public Island(boolean PreMotNat, int posIndex){
        super(posIndex);
        this.presenceMotherNature=PreMotNat;
    }

    public Island(int posIndex){
        super(posIndex);
        this.presenceMotherNature=false;
    }



}
