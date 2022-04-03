package it.polimi.ingsw.model;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    Island i;
    @Test
    void addStudent() {
        i = new Island(3);
        for(Colour c : Colour.values())
            assertEquals(0,i.getStudents().getQuantityColour(c));

        i.addStudent(Colour.YELLOW);

        for(Colour c : Colour.values()) {
            if (c != Colour.YELLOW)
                assertEquals(0, i.getStudents().getQuantityColour(c));
            else
                assertEquals(1, i.getStudents().getQuantityColour(c));
        }
    }

    @Test
    void setTower() {
        i = new Island(3);
        assertEquals(null,i.getTower());
        for(Tower t: Tower.values()) {
            i.setTower(t);
            assertEquals(t, i.getTower());
        }
    }

    @Test
    void getTower() {
        i = new Island(3);
        Tower d = Tower.BLACK;
        i.setTower(d);
        assertEquals(d,i.getTower());
    }


    @Test
    void getIslandIndex() {
        i = new Island(3);
        assertEquals(3,i.getIslandIndex());
    }

    @Test
    void clearStudents() {
        i = new Island(3);
        for(Colour c : Colour.values())
            assertEquals(0,i.getStudents().getQuantityColour(c));

        i.addStudent(Colour.YELLOW);

        for(Colour c : Colour.values()) {
            if (c != Colour.YELLOW)
                assertEquals(0, i.getStudents().getQuantityColour(c));
            else
                assertEquals(1, i.getStudents().getQuantityColour(c));
        }

        i.clearStudents();
        for(Colour c : Colour.values())
            assertEquals(0,i.getStudents().getQuantityColour(c));

    }
}