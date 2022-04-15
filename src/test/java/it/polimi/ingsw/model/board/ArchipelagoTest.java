package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.enumerations.Tower;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArchipelagoTest {
    @Test
    void constructor() {
        ArrayList<Archipelago> a = new ArrayList<>();
        for(int i=0;i<12;i++) {
            a.add(new Archipelago(i+1));
            assertEquals(i+1, a.get(i).getFirstIslandIndex());
            assertEquals(1, a.get(i).getIslands().size());
            assertEquals(i+1, a.get(i).getIslands().get(0).getIslandIndex());
            assertEquals(0, a.get(i).getNoEntryTiles());
            assertFalse(a.get(i).isPresenceMotherNature());
        }
    }

    //must be tested after board
    @Test
    void playerInfluence() {
        Archipelago a = new Archipelago(3);
        ProfessorGroup p = new ProfessorGroup();
        assertEquals(0, a.playerInfluence(Tower.BLACK,p));
        p.setTower(Colour.YELLOW, Tower.BLACK);
        p.setTower(Colour.RED,Tower.WHITE);
        p.setTower(Colour.BLUE,Tower.GRAY);
        p.setTower(Colour.GREEN,Tower.BLACK);
        p.setTower(Colour.PINK,Tower.WHITE);
        a.getIslands().get(0).getStudents().setNumStudents(17,Colour.GREEN);
        a.getIslands().get(0).getStudents().setNumStudents(2,Colour.YELLOW);
        a.getIslands().get(0).getStudents().setNumStudents(3,Colour.BLUE);
        a.getIslands().get(0).getStudents().setNumStudents(4,Colour.RED);
        a.getIslands().get(0).getStudents().setNumStudents(5,Colour.PINK);
        assertEquals(19, a.playerInfluence(Tower.BLACK,p));
        a.getIslands().get(0).setTower(Tower.WHITE);
        assertEquals(19, a.playerInfluence(Tower.BLACK,p));
        a.getIslands().get(0).setTower(Tower.BLACK);
        assertEquals(20, a.playerInfluence(Tower.BLACK,p));

        a.getIslands().add(new Island(4));
        a.getIslands().add(new Island(5));
        assertEquals(20, a.playerInfluence(Tower.BLACK,p));
        a.getIslands().get(1).getStudents().setNumStudents(0,Colour.GREEN);
        a.getIslands().get(1).getStudents().setNumStudents(0,Colour.YELLOW);
        a.getIslands().get(1).getStudents().setNumStudents(3,Colour.BLUE);
        a.getIslands().get(1).getStudents().setNumStudents(4,Colour.RED);
        a.getIslands().get(1).getStudents().setNumStudents(5,Colour.PINK);
        a.getIslands().get(2).getStudents().setNumStudents(1,Colour.GREEN);
        a.getIslands().get(2).getStudents().setNumStudents(1,Colour.YELLOW);
        a.getIslands().get(2).getStudents().setNumStudents(0,Colour.BLUE);
        a.getIslands().get(2).getStudents().setNumStudents(40,Colour.RED);
        a.getIslands().get(2).getStudents().setNumStudents(50,Colour.PINK);
        a.getIslands().get(1).setTower(Tower.BLACK);
        a.getIslands().get(2).setTower(Tower.BLACK);
        assertEquals(24, a.playerInfluence(Tower.BLACK,p));

    }


    @Test
    void merge() {
        ArrayList<Island> islandList = new ArrayList<>();
        Archipelago a1 = new Archipelago(1);
        Archipelago a2 = new Archipelago(2);
        a1 = a1.merge(a2);
        assertEquals(1,a1.getFirstIslandIndex());
        assertEquals(2,a1.getIslands().size());
        assertEquals(1,a1.getIslands().get(0).getIslandIndex());
        assertEquals(2,a1.getIslands().get(1).getIslandIndex());
        assertEquals(0,a1.getNoEntryTiles());
        assertFalse(a1.isPresenceMotherNature());

        a2 = new Archipelago(3);
        a2.getIslands().add(new Island(4));
        a2.setPresenceMotherNature(true);
        a2.setNoEntryTiles(2);
        a1 = a1.merge(a2);
        assertEquals(1,a1.getFirstIslandIndex());
        assertEquals(4,a1.getIslands().size());
        assertEquals(1,a1.getIslands().get(0).getIslandIndex());
        assertEquals(2,a1.getIslands().get(1).getIslandIndex());
        assertEquals(3,a1.getIslands().get(2).getIslandIndex());
        assertEquals(4,a1.getIslands().get(3).getIslandIndex());
        assertEquals(2,a1.getNoEntryTiles());
        assertTrue(a1.isPresenceMotherNature());

        a1 = new Archipelago(3);
        a1.getIslands().add(new Island(4));
        a2 = new Archipelago(2);
        a2 = a2.merge(new Archipelago(1));
        assertEquals(1,a2.getFirstIslandIndex());
        assertEquals(2,a2.getIslands().size());
        assertEquals(1,a2.getIslands().get(0).getIslandIndex());
        assertEquals(2,a2.getIslands().get(1).getIslandIndex());
        assertEquals(0,a2.getNoEntryTiles());
        assertFalse(a2.isPresenceMotherNature());

        a1.setNoEntryTiles(3);
        a1 = a1.merge(a2);
        assertEquals(1,a1.getFirstIslandIndex());
        assertEquals(4,a1.getIslands().size());
        assertEquals(1,a1.getIslands().get(0).getIslandIndex());
        assertEquals(2,a1.getIslands().get(1).getIslandIndex());
        assertEquals(3,a1.getIslands().get(2).getIslandIndex());
        assertEquals(4,a1.getIslands().get(3).getIslandIndex());
        assertEquals(3,a1.getNoEntryTiles());
        assertFalse(a1.isPresenceMotherNature());

        a1 = new Archipelago(1);
        a2 = new Archipelago(12);
        a1.setPresenceMotherNature(true);
        a1 = a1.merge(a2);
        assertEquals(12,a1.getFirstIslandIndex());
        assertEquals(2,a1.getIslands().size());
        assertEquals(12,a1.getIslands().get(0).getIslandIndex());
        assertEquals(1,a1.getIslands().get(1).getIslandIndex());
        assertEquals(0,a1.getNoEntryTiles());
        assertTrue(a1.isPresenceMotherNature());

        a2 = new Archipelago(10);
        a2.getIslands().add(new Island(11));
        a1 = a1.merge(a2);
        assertEquals(10,a1.getFirstIslandIndex());
        assertEquals(4,a1.getIslands().size());
        assertEquals(10,a1.getIslands().get(0).getIslandIndex());
        assertEquals(11,a1.getIslands().get(1).getIslandIndex());
        assertEquals(12,a1.getIslands().get(2).getIslandIndex());
        assertEquals(1,a1.getIslands().get(3).getIslandIndex());
        assertEquals(0,a1.getNoEntryTiles());
        assertTrue(a1.isPresenceMotherNature());
    }
}