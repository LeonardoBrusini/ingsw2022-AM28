package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardEffect5Test {

    @Test
    void resolveEffect(CharacterCard c, Bag bag, MotherNature mn, IslandManager manager) {
        Archipelago a = manager.getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex());
        int anet = a.getNoEntryTiles();
        int cnet = c.getNoEntryTiles();
        if(c.getNoEntryTiles()>0){
            assertEquals(anet + 1,a.getNoEntryTiles());
            assertEquals(cnet - 1, c.getNoEntryTiles());
        }else{
            assertEquals(anet ,a.getNoEntryTiles());
            assertEquals(cnet , c.getNoEntryTiles());
        }
    }
}