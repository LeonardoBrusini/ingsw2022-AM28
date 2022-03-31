package it.polimi.ingsw.model;

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
        archipelagos.get(mnIndex).setMotherNature(true);
    }

    /**
     *
     * @param islandIndex
     * @return the island object with "islandIndex" index
     */
    public Island getIsland(int islandIndex) {
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
    public void checkAggregation(int islandIndex) {
        int archipelagoIndex=0;
        for(int i=0;i<archipelagos.size(); i++) {
            if(archipelagos.get(i).getFirstIslandIndex()==islandIndex) archipelagoIndex = i;
        }

        //checking the next archipelago
        if(archipelagoIndex<archipelagos.size()-1) {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower().equals(archipelagos.get(archipelagoIndex+1).getIslands().get(0).getTower())) {
                archipelagos.get(archipelagoIndex).merge(archipelagos.get(archipelagoIndex+1));
                archipelagos.remove(archipelagoIndex+1);
            }
        } else {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower().equals(archipelagos.get(0).getIslands().get(0).getTower())) {
                archipelagos.get(archipelagoIndex).merge(archipelagos.get(0));
                archipelagos.remove(0);
            }
        }

        //checking the one before
        if(archipelagoIndex>0) {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower().equals(archipelagos.get(archipelagoIndex-1).getIslands().get(0).getTower())) {
                archipelagos.get(archipelagoIndex).merge(archipelagos.get(archipelagoIndex-1));
                archipelagos.remove(archipelagoIndex-1);
            }
        } else {
            if(archipelagos.get(archipelagoIndex).getIslands().get(0).getTower().equals(archipelagos.get(archipelagos.size()-1).getIslands().get(0).getTower())) {
                archipelagos.get(archipelagoIndex).merge(archipelagos.get(archipelagos.size()-1));
                archipelagos.remove(archipelagos.size()-1);
            }
        }
    }
}
