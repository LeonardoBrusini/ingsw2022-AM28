package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorGroupTest {
    private ProfessorGroup pg;

    /**
     * To do before each test
     */
    @BeforeEach
    void initializeProfessors() {
        pg = new ProfessorGroup();
        for (Colour c: Colour.values()) assertFalse(pg.getProfessors().containsKey(c));
    }

    /**
     *To test if the method returns the correct Tower of the Player related with the Professor of the given colour
     */
    @Test
    void getTower() {
        for (Colour c: Colour.values()) {
            pg.setTower(c,null);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
            pg.setTower(c, Tower.WHITE);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
            pg.setTower(c,Tower.BLACK);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
            pg.setTower(c,Tower.GREY);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
        }
        ProfessorGroup pg2 = new ProfessorGroup();
        for (Colour c : Colour.values()) {
            for (Colour c1 : Colour.values()) {
                pg2.setTower(c1,pg.getProfessors().get(c1));
                assertEquals(pg.getTower(c1),pg2.getTower(c1));
            }
            pg.setTower(c,null);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getTower(c1),pg.getTower(c1));
            }
            pg.setTower(c,Tower.WHITE);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getTower(c1),pg.getTower(c1));
            }
            pg.setTower(c,Tower.BLACK);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getTower(c1),pg.getTower(c1));
            }
            pg.setTower(c,Tower.GREY);
            assertEquals(pg.getTower(c),pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getTower(c1),pg.getTower(c1));
            }
        }
    }

    /**
     * To test if the method sets the correct Tower of the Player related with the Professor of the given colour
     */
    @Test
    void setTower() {
        for (Colour c: Colour.values()) {
            pg.setTower(c,null);
            assertNull(pg.getProfessors().get(c));
            pg.setTower(c,Tower.WHITE);
            assertEquals(Tower.WHITE,pg.getProfessors().get(c));
            pg.setTower(c,Tower.BLACK);
            assertEquals(Tower.BLACK,pg.getProfessors().get(c));
            pg.setTower(c,Tower.GREY);
            assertEquals(Tower.GREY,pg.getProfessors().get(c));
        }
        ProfessorGroup pg2 = new ProfessorGroup();
        for (Colour c : Colour.values()) {
            for (Colour c1 : Colour.values()) {
                pg2.setTower(c1,pg.getProfessors().get(c1));
                assertEquals(pg.getProfessors().get(c1),pg2.getProfessors().get(c1));
            }
            pg.setTower(c,null);
            assertNull(pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getProfessors().get(c1),pg.getProfessors().get(c1));
            }
            pg.setTower(c,Tower.WHITE);
            assertEquals(Tower.WHITE,pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getProfessors().get(c1),pg.getProfessors().get(c1));
            }
            pg.setTower(c,Tower.BLACK);
            assertEquals(Tower.BLACK,pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getProfessors().get(c1),pg.getProfessors().get(c1));
            }
            pg.setTower(c,Tower.GREY);
            assertEquals(Tower.GREY,pg.getProfessors().get(c));
            for (Colour c1 : Colour.values()) {
                if(c!=c1) assertEquals(pg2.getProfessors().get(c1),pg.getProfessors().get(c1));
            }
        }
    }

    /**
     * It tests with the method returns the right Colours linked to given Tower
     */
    @Test
    void getColours() {
        //pg empty
        for(Tower t : Tower.values()) {
            assertTrue(pg.getColours(t).isEmpty());
        }

        //pg half-empty
        pg.setTower(Colour.YELLOW,Tower.BLACK);
        pg.setTower(Colour.GREEN,Tower.BLACK);
        ArrayList<Colour> listBlack = pg.getColours(Tower.BLACK);
        ArrayList<Colour> listGrey = pg.getColours(Tower.GREY);
        ArrayList<Colour> listWhite = pg.getColours(Tower.WHITE);
        assertEquals(2,listBlack.size());
        assertEquals(0,listWhite.size());
        assertEquals(0,listGrey.size());
        assertEquals(Colour.GREEN,listBlack.get(0));
        assertEquals(Colour.YELLOW,listBlack.get(1));

        //pg full
        pg.setTower(Colour.YELLOW,Tower.BLACK);
        pg.setTower(Colour.GREEN,Tower.GREY);
        pg.setTower(Colour.BLUE,Tower.WHITE);
        pg.setTower(Colour.PINK,Tower.GREY);
        pg.setTower(Colour.RED,Tower.GREY);
        listBlack = pg.getColours(Tower.BLACK);
        listGrey = pg.getColours(Tower.GREY);
        listWhite = pg.getColours(Tower.WHITE);
        assertEquals(1,listBlack.size());
        assertEquals(1,listWhite.size());
        assertEquals(3,listGrey.size());
        assertEquals(Colour.YELLOW,listBlack.get(0));
        assertEquals(Colour.BLUE,listWhite.get(0));
        assertEquals(Colour.GREEN,listGrey.get(0));
        assertEquals(Colour.RED,listGrey.get(1));
        assertEquals(Colour.PINK,listGrey.get(2));

    }
}