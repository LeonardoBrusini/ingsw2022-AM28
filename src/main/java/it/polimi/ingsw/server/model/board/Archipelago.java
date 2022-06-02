package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.network.IslandStatus;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.model.ProfessorGroup;
import it.polimi.ingsw.server.model.cards.CharacterCard;

import java.util.ArrayList;

/**
 * Archipelago is a list of islands
 */
public class Archipelago {
    private final ArrayList<Island> islands;
    private boolean presenceMotherNature;
    private int noEntryTiles;
    private int firstIslandIndex;

    /**
     * constructor. Initializes the list of island with a single island
     * @param index the index of the first island
     */
    public Archipelago(int index) throws IllegalArgumentException{
        if (index<1 || index>12) throw new IllegalArgumentException();
        firstIslandIndex = index;
        islands = new ArrayList<>();
        islands.add(new Island(index));
        noEntryTiles = 0;
        presenceMotherNature = false;
    }

    /**
     * sums the influence of the player p on every island in the archipelago
     * @param t the tower selected
     * @return the total influence of the archipelago for the player p
     */
    public int playerInfluence(Tower t, ProfessorGroup professors, ArrayList<CharacterCard> cards) throws NullPointerException{
        if(t == null || professors == null) throw new NullPointerException();
        int influence = 0;
        for(Island i : islands) {
            influence += i.playerInfluence(t,professors,cards);
        }
        System.out.println("influence tower "+t+" on archipelago = "+influence);
        return influence;
    }

    /**
     * adds the island in the archipelago parameter to this archipelago
     * @param a archipelago to merge
     */
    public Archipelago merge(Archipelago a) throws NullPointerException{
        if(a == null) throw new NullPointerException();
        if(a.firstIslandIndex+a.islands.size()-12==firstIslandIndex || a.firstIslandIndex+a.islands.size()==firstIslandIndex) {
            return a.merge(this);
        }
        islands.addAll(a.islands);
        presenceMotherNature = presenceMotherNature || a.presenceMotherNature;
        noEntryTiles += a.noEntryTiles;
        return this;
    }
    public ArrayList<IslandStatus> getFullIslandsStatus() {
        ArrayList<IslandStatus> is = new ArrayList<>();
        for (int i=0;i<islands.size();i++) {
            IslandStatus isTemp = new IslandStatus();
            if(islands.get(i).getTower()!=null) isTemp.setTowerColour(islands.get(i).getTower().toString());
            isTemp.setIslandIndex(islands.get(i).getIslandIndex());
            isTemp.setStudents(islands.get(i).getStudents().getStatus());
            is.add(isTemp);
        }
        return is;
    }

    //getters & setters
    public int getFirstIslandIndex() {
        return firstIslandIndex;
    }
    public ArrayList<Island> getIslands() {
        return islands;
    }
    public boolean isPresenceMotherNature() {
        return presenceMotherNature;
    }
    public void setPresenceMotherNature(boolean presenceMotherNature) {
        this.presenceMotherNature = presenceMotherNature;
    }
    public int getNoEntryTiles() {
        return noEntryTiles;
    }
    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }



}
