package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Archipelago;
import it.polimi.ingsw.model.board.Island;

import java.util.ArrayList;

public class IslandManager {
    private ArrayList<Archipelago> archipelagos;

    /**
     * IslandManager constructor
     * @param mnIndex the index of the island where mother nature is
     */
    public IslandManager(int mnIndex) {
        archipelagos = new ArrayList<>();
        for(int i=1; i<=12; i++) {
            archipelagos.add(new Archipelago(i));
        }
        archipelagos.get(mnIndex-1).setPresenceMotherNature(true);
    }

    /**
     *
     * @param islandIndex the index of the island to find
     * @return the island object with "islandIndex" index
     */
    public Island getIsland(int islandIndex) throws IllegalArgumentException{
        if(islandIndex<1 || islandIndex>12) throw new IllegalArgumentException();
        for(Archipelago a : archipelagos) {
            for(Island i : a.getIslands()) {
                if(i.getIslandIndex()==islandIndex) return i;
            }
        }
        return null;
    }

    /**
     * checks if an archipelago is next to another one with same towers, if so, merges them
     * @param islandIndex has to be one of the firstIslandIndex of one of the archipelagos
     */
    private void checkAggregation(int islandIndex) throws IllegalArgumentException{
        if(islandIndex<1 || islandIndex>12) throw new IllegalArgumentException();
        int archipelagoIndex=0;
        for(int i=0;i<archipelagos.size(); i++) {
            for (int j=0;j<archipelagos.get(i).getIslands().size();j++) {
                if(archipelagos.get(i).getIslands().get(j).getIslandIndex()==islandIndex){
                    archipelagoIndex = i;
                    break;
                }
            }
        }

        if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower()==null) return;

        //checking the next archipelago
        if(archipelagoIndex<archipelagos.size()-1) {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower()==archipelagos.get(archipelagoIndex+1).getIslands().get(0).getTower()) {
                archipelagos.set(archipelagoIndex, archipelagos.get(archipelagoIndex).merge(archipelagos.get(archipelagoIndex+1)));
                archipelagos.remove(archipelagoIndex+1);
            }
        } else {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower()==archipelagos.get(0).getIslands().get(0).getTower()) {
                archipelagos.set(archipelagoIndex, archipelagos.get(archipelagoIndex).merge(archipelagos.get(0)));
                archipelagos.remove(0);
                archipelagoIndex--;
            }
        }

        //checking the one before
        if(archipelagoIndex>0) {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower()==archipelagos.get(archipelagoIndex-1).getIslands().get(0).getTower()) {
                archipelagos.set(archipelagoIndex,archipelagos.get(archipelagoIndex).merge(archipelagos.get(archipelagoIndex-1)));
                archipelagos.remove(archipelagoIndex-1);
            }
        } else {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower()==archipelagos.get(archipelagos.size()-1).getIslands().get(0).getTower()) {
                archipelagos.set(archipelagoIndex, archipelagos.get(archipelagoIndex).merge(archipelagos.get(archipelagos.size()-1)));
                archipelagos.remove(archipelagos.size()-1);
            }
        }
    }

    /**
     *
     * @param islandIndex the index of the island we want to know on which archipelago it is
     * @return the archipelago containing the selected island
     */
    public Archipelago getArchipelagoByIslandIndex(int islandIndex) throws IllegalArgumentException{
        if(islandIndex<1 || islandIndex>12) throw new IllegalArgumentException();
        for(Archipelago a : archipelagos) {
            for(Island i : a.getIslands()) {
                if(i.getIslandIndex()==islandIndex) return a;
            }
        }
        return null;
    }

    /**
     * puts the selected tower to the selected island and checks a possible aggregation of archipelagos
     * @param tower colour of the tower
     * @param islandIndex the index of the island we want to build the tower
     */
    public void setTowerOnIsland(Tower tower, int islandIndex) throws IllegalArgumentException{
        if(islandIndex<1 || islandIndex>12 || tower==null) throw new IllegalArgumentException();
        Island i = getIsland(islandIndex);
        i.setTower(tower);
        checkAggregation(islandIndex);
    }

    //getters & setters for testing
    public ArrayList<Archipelago> getArchipelagos() {
        return archipelagos;
    }
    public void setArchipelagos(ArrayList<Archipelago> archipelagos) {
        this.archipelagos = archipelagos;
    }
}
