package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Archipelago {
    private ArrayList<Island> islands;
    private boolean presenceMotherNature;
    private int noEntryTiles;
    private int firstIslandIndex;

    public Archipelago(int index) {
        firstIslandIndex = index;
        islands = new ArrayList<>();
        islands.add(new Island(index));
        noEntryTiles = 0;
        presenceMotherNature = false;
    }

    public int playerInfluence(Player p) {
        int influence = 0;
        for(Island i : islands) {
            influence += i.playerInfluence(p);
        }
        return influence;
    }

    public void merge(Archipelago a) {
        for(Island i : a.islands) {
            islands.add(i);
        }
        if(a.firstIslandIndex+a.islands.size()>12) firstIslandIndex = a.firstIslandIndex;
        presenceMotherNature = presenceMotherNature || a.presenceMotherNature;
        noEntryTiles += a.noEntryTiles;
    }

    public int getFirstIslandIndex() {
        return firstIslandIndex;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public void setMotherNature(boolean b) {
        presenceMotherNature = b;
    }

    /*public Archipelago(Island input, Island output){
        super(input.getPosIndex());
        if(input.getTower().equals(output.getTower()) && (input.getPosIndex()==output.posIndex+1 || input.getPosIndex()+1==output.posIndex)){
            if(input.getPosIndex()>output.getPosIndex()) {
                setPosIndex(output.getPosIndex());
            }



            this.tower = input.getTower();
            this.numTower = input.getNumTower() + output.getNumTower();


            this.presenceMotherNature = input.getPresenceMotherNature() || output.getPresenceMotherNature();


            Colour[] e = Colour.values();

            for(Colour c : e){
                int val;
                val= input.getStudentGroup().getQuantityColour(c) + output.getStudentGroup().getQuantityColour(c);
                this.studentGroup.setNumStudents(val,c);
            }

        }
    }
    */
    /*public Archipelago(AbstractIsland input, AbstractIsland output){
        super();
        if(input.getTower().equals(output.getTower()) && (input.getPosIndex()==output.posIndex+1 || input.getPosIndex()+1==output.posIndex)){
            setPosIndex(input.getPosIndex());

            if(input.getPosIndex()>output.getPosIndex()) {
                setPosIndex(output.getPosIndex());
            }


            setTower(input.getTower());
            this.numTower = input.getNumTower() + output.getNumTower();

            this.presenceMotherNature = input.getPresenceMotherNature() || output.getPresenceMotherNature();

            Colour[] e = Colour.values();

            for(Colour c : e){
                int val;
                val= input.getStudentGroup().getQuantityColour(c) + output.getStudentGroup().getQuantityColour(c);
                this.studentGroup.setNumStudents(val,c);
            }
        }
    }*/
}
