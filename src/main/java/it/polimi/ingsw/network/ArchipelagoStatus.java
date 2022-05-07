package it.polimi.ingsw.network;

/**
 * It represents the current status of an Archipelago during the match
 */
public class ArchipelagoStatus {
    int index;
    IslandStatus[] islands;
    //ArrayList<IslandStatus> islands;
    Integer noEntryTiles;
    /**
     * Setter for the index of the corresponding Archipelago
     * @param index the index of the Archipelago
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /*public ArrayList<IslandStatus> getIslands() {
        return islands;
    }*/

    /*public void setIslands(ArrayList<IslandStatus> islands) {
        this.islands = islands;
    }*/

    /**
     * Setter for the IslandStatus of the Island in the Archipelago
     * @param islands the IslandStatus array
     */
    public void setIslands(IslandStatus[] islands) {
        this.islands = islands;
    }


    public int getIndex() {
        return index;
    }

    public IslandStatus[] getIslands() {
        return islands;
    }

    public Integer getNoEntryTiles() {
        return noEntryTiles;
    }

    public void setNoEntryTiles(Integer noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }
}
