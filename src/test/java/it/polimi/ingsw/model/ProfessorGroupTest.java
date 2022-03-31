package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorGroupTest {
    private ProfessorGroup pg;

    @BeforeEach
    void initializeProfessors() {
        pg = new ProfessorGroup();
    }

    @Test
    void getTower() {

    }

    @RepeatedTest(value = 100)
    void setTower() {
        Random r = new Random();
        Colour c = Colour.values()[r.nextInt(Colour.values().length)];
        Tower t = Tower.values()[r.nextInt(Tower.values().length)];
        Tower t2 = Tower.values()[r.nextInt(Tower.values().length)];
        pg.setTower(c,t);
        assertEquals(t,pg.getTower(c));
        pg.setTower(c,t2);
        assertEquals(t2,pg.getTower(c));
    }

    @Test
    void getColours() {
        //pg vouto
        for(Tower t : Tower.values()) {
            assertEquals(true,pg.getColours(t).isEmpty());
        }

        //pg semipieno
        pg.setTower(Colour.YELLOW,Tower.BLACK);
        pg.setTower(Colour.GREEN,Tower.BLACK);
        ArrayList<Colour> listBlack = pg.getColours(Tower.BLACK);
        ArrayList<Colour> listGrey = pg.getColours(Tower.GRAY);
        ArrayList<Colour> listWhite = pg.getColours(Tower.WHITE);
        assertEquals(2,listBlack.size());
        assertEquals(0,listWhite.size());
        assertEquals(0,listGrey.size());
        assertEquals(Colour.YELLOW,listBlack.get(0));
        assertEquals(Colour.GREEN,listBlack.get(1));

        //pg pieno
        pg.setTower(Colour.YELLOW,Tower.BLACK);
        pg.setTower(Colour.GREEN,Tower.GRAY);
        pg.setTower(Colour.BLUE,Tower.WHITE);
        pg.setTower(Colour.PINK,Tower.GRAY);
        pg.setTower(Colour.RED,Tower.GRAY);
        listBlack = pg.getColours(Tower.BLACK);
        listGrey = pg.getColours(Tower.GRAY);
        listWhite = pg.getColours(Tower.WHITE);
        assertEquals(1,listBlack.size());
        assertEquals(1,listWhite.size());
        assertEquals(3,listGrey.size());
        assertEquals(Colour.YELLOW,listBlack.get(0));
        assertEquals(Colour.BLUE,listWhite.get(0));
        assertEquals(Colour.GREEN,listGrey.get(0));
        assertEquals(Colour.PINK,listGrey.get(1));
        assertEquals(Colour.RED,listGrey.get(2));

    }
}