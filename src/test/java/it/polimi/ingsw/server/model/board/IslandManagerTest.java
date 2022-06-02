package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.enumerations.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandManagerTest {
    private IslandManager im;

    /**
     * To do before each test
     */
    @BeforeEach
    void initialize() {
        im = new IslandManager(4);
        assertEquals(12,im.getArchipelagos().size());
        for(int i=0;i<12;i++) {
            assertEquals(i+1,im.getArchipelagos().get(i).getFirstIslandIndex());
        }
        assertTrue(im.getArchipelagos().get(3).isPresenceMotherNature());
    }

    /**
     * It verifies if the method assign and build a Player's Tower on the Island according to the game's rules
     */
    @Test
    void setTowerOnIsland() {
        im.setTowersOnArchipelago(Tower.WHITE,im.getArchipelagoByIslandIndex(1));
        assertEquals(12,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.BLACK,im.getArchipelagoByIslandIndex(2));
        assertEquals(12,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.BLACK,im.getArchipelagoByIslandIndex(4));
        assertEquals(12,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.WHITE,im.getArchipelagoByIslandIndex(5));
        assertEquals(12,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.WHITE,im.getArchipelagoByIslandIndex(6));
        im.checkAggregation(5);
        assertEquals(11,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.WHITE,im.getArchipelagoByIslandIndex(7));
        im.checkAggregation(7);
        assertEquals(10,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.BLACK,im.getArchipelagoByIslandIndex(8));
        im.checkAggregation(8);
        assertEquals(10,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.BLACK,im.getArchipelagoByIslandIndex(9));
        im.checkAggregation(9);
        assertEquals(9,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.WHITE,im.getArchipelagoByIslandIndex(11));
        im.checkAggregation(11);
        assertEquals(9,im.getArchipelagos().size());
        im.setTowersOnArchipelago(Tower.BLACK,im.getArchipelagoByIslandIndex(12));
        im.checkAggregation(12);
        assertEquals(9,im.getArchipelagos().size());

        assertEquals(1,im.getArchipelagos().get(0).getIslands().size());
        assertEquals(1,im.getArchipelagos().get(0).getFirstIslandIndex());
        assertEquals(Tower.WHITE,im.getArchipelagos().get(0).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(1).getIslands().size());
        assertEquals(2,im.getArchipelagos().get(1).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(1).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(2).getIslands().size());
        assertEquals(3,im.getArchipelagos().get(2).getFirstIslandIndex());
        assertNull(im.getArchipelagos().get(2).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(3).getIslands().size());
        assertEquals(4,im.getArchipelagos().get(3).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(3).getIslands().get(0).getTower());
        assertEquals(3,im.getArchipelagos().get(4).getIslands().size());
        assertEquals(5,im.getArchipelagos().get(4).getFirstIslandIndex());
        assertEquals(Tower.WHITE,im.getArchipelagos().get(4).getIslands().get(0).getTower());
        assertEquals(2,im.getArchipelagos().get(5).getIslands().size());
        assertEquals(8,im.getArchipelagos().get(5).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(5).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(6).getIslands().size());
        assertEquals(10,im.getArchipelagos().get(6).getFirstIslandIndex());
        assertNull(im.getArchipelagos().get(6).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(7).getIslands().size());
        assertEquals(11,im.getArchipelagos().get(7).getFirstIslandIndex());
        assertEquals(Tower.WHITE,im.getArchipelagos().get(7).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(8).getIslands().size());
        assertEquals(12,im.getArchipelagos().get(8).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(8).getIslands().get(0).getTower());

        im.setTowersOnArchipelago(Tower.BLACK,im.getArchipelagoByIslandIndex(1));
        im.checkAggregation(1);
        assertEquals(7,im.getArchipelagos().size());

        assertEquals(1,im.getArchipelagos().get(1).getIslands().size());
        assertEquals(3,im.getArchipelagos().get(1).getFirstIslandIndex());
        assertNull(im.getArchipelagos().get(1).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(2).getIslands().size());
        assertEquals(4,im.getArchipelagos().get(2).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(2).getIslands().get(0).getTower());
        assertEquals(3,im.getArchipelagos().get(3).getIslands().size());
        assertEquals(5,im.getArchipelagos().get(3).getFirstIslandIndex());
        assertEquals(Tower.WHITE,im.getArchipelagos().get(3).getIslands().get(0).getTower());
        assertEquals(2,im.getArchipelagos().get(4).getIslands().size());
        assertEquals(8,im.getArchipelagos().get(4).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(4).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(5).getIslands().size());
        assertEquals(10,im.getArchipelagos().get(5).getFirstIslandIndex());
        assertNull(im.getArchipelagos().get(5).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(6).getIslands().size());
        assertEquals(11,im.getArchipelagos().get(6).getFirstIslandIndex());
        assertEquals(Tower.WHITE,im.getArchipelagos().get(6).getIslands().get(0).getTower());
        assertEquals(3,im.getArchipelagos().get(0).getIslands().size());
        assertEquals(12,im.getArchipelagos().get(0).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(0).getIslands().get(0).getTower());

        im.setTowersOnArchipelago(Tower.BLACK,im.getArchipelagoByIslandIndex(3));
        im.checkAggregation(3);
        assertEquals(5,im.getArchipelagos().size());

        assertEquals(5,im.getArchipelagos().get(0).getIslands().size());
        assertEquals(12,im.getArchipelagos().get(0).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(0).getIslands().get(0).getTower());
        assertEquals(3,im.getArchipelagos().get(1).getIslands().size());
        assertEquals(5,im.getArchipelagos().get(1).getFirstIslandIndex());
        assertEquals(Tower.WHITE,im.getArchipelagos().get(1).getIslands().get(0).getTower());
        assertEquals(2,im.getArchipelagos().get(2).getIslands().size());
        assertEquals(8,im.getArchipelagos().get(2).getFirstIslandIndex());
        assertEquals(Tower.BLACK,im.getArchipelagos().get(2).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(3).getIslands().size());
        assertEquals(10,im.getArchipelagos().get(3).getFirstIslandIndex());
        assertNull(im.getArchipelagos().get(3).getIslands().get(0).getTower());
        assertEquals(1,im.getArchipelagos().get(4).getIslands().size());
        assertEquals(11,im.getArchipelagos().get(4).getFirstIslandIndex());
        assertEquals(Tower.WHITE,im.getArchipelagos().get(4).getIslands().get(0).getTower());
    }

    /**
     * It tests if is returned the Archipelago witch contains the island of the given index
     */
    @Test
    void getArchipelagoByIslandIndex() {
        for (int i = 0;i<12; i++) {
            assertSame(im.getArchipelagos().get(i), im.getArchipelagoByIslandIndex(i + 1));
        }
        im.getArchipelagos().get(2).merge(im.getArchipelagos().get(3));
        im.getArchipelagos().remove(3);
        assertSame(im.getArchipelagos().get(2), im.getArchipelagoByIslandIndex(3));
        assertSame(im.getArchipelagos().get(2), im.getArchipelagoByIslandIndex(4));
        im.getArchipelagos().get(10).merge(im.getArchipelagos().get(0));
        im.getArchipelagos().remove(0);
        assertSame(im.getArchipelagos().get(9), im.getArchipelagoByIslandIndex(12));
        assertSame(im.getArchipelagos().get(9), im.getArchipelagoByIslandIndex(1));
    }

    @Test
    void getArchipelagoIndexByIslandIndex() {
        for (int i = 0;i<12; i++) {
            assertEquals(i, im.getArchipelagoIndexByIslandIndex(i + 1));
        }
        im.getArchipelagos().get(2).merge(im.getArchipelagos().get(3));
        im.getArchipelagos().remove(3);
        assertEquals(2, im.getArchipelagoIndexByIslandIndex(3));
        assertEquals(2, im.getArchipelagoIndexByIslandIndex(4));
        im.getArchipelagos().get(10).merge(im.getArchipelagos().get(0));
        im.getArchipelagos().remove(0);
        assertEquals(9, im.getArchipelagoIndexByIslandIndex(12));
        assertEquals(9, im.getArchipelagoIndexByIslandIndex(1));
    }
}