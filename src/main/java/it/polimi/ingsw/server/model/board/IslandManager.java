package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.network.ArchipelagoStatus;
import it.polimi.ingsw.server.enumerations.Tower;

import java.util.ArrayList;

public class IslandManager {
    private final ArrayList<Archipelago> archipelagos;

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
    public Island getIslandByIndex(int islandIndex) throws IllegalArgumentException{
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
    public void checkAggregation(int islandIndex) throws IllegalArgumentException{
        System.out.println("CHECKING ISLAND AGGREGATION");
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
     *
     * @param islandIndex the index of the island we want to know on which archipelago it is
     * @return the archipelago index containing the selected island
     */
    public int getArchipelagoIndexByIslandIndex(int islandIndex) throws IllegalArgumentException {
        if(islandIndex<1 || islandIndex>12) throw new IllegalArgumentException();
        for(int i=0;i<archipelagos.size();i++) {
            for(Island island : archipelagos.get(i).getIslands()) {
                if(island.getIslandIndex()==islandIndex) return i;
            }
        }
        return -1;
    }

    /**
     * puts the selected tower to the selected island and checks a possible aggregation of archipelagos
     * @param tower colour of the tower
     * @param archipelago the index of the island we want to build the tower
     */
    public void setTowersOnArchipelago(Tower tower, Archipelago archipelago) throws IllegalArgumentException{
        for (Island i: archipelago.getIslands()) {
            i.setTower(tower);
        }
    }

    public ArrayList<ArchipelagoStatus> getFullArchipelagosStatus() {
        ArrayList<ArchipelagoStatus> as = new ArrayList<>();
        for(int i=0;i<archipelagos.size();i++) {
            ArchipelagoStatus asTemp = new ArchipelagoStatus();
            asTemp.setIndex(i);
            asTemp.setIslands(archipelagos.get(i).getFullIslandsStatus());
            asTemp.setNoEntryTiles(archipelagos.get(i).getNoEntryTiles());
            as.add(asTemp);
        }
        return as;
    }

    //getters & setters for testing
    public ArrayList<Archipelago> getArchipelagos() {
        return archipelagos;
    }
}
