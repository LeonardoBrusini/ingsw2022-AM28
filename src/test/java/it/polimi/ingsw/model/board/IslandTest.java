package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Island;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

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
        assertNull(i.getTower());
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
    @Test
    void playerInfluence(){
        i = new Island(3);
        Bag bag = new Bag();
        i.setStudents(new StudentGroup(2));
        ProfessorGroup pg = new ProfessorGroup();
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        pg.setProfessors(en);
        assertEquals(0,i.playerInfluence(Tower.BLACK, pg));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg));
        en.put(Colour.YELLOW, Tower.BLACK);
        pg.setProfessors(en);
        assertEquals(2,i.playerInfluence(Tower.BLACK, pg));
        i.setTower(Tower.BLACK);
        assertEquals(3,i.playerInfluence(Tower.BLACK, pg));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg));
        i.setTower(Tower.WHITE);
        assertEquals(2, i.playerInfluence(Tower.BLACK, pg));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.WHITE);
        en.put(Colour.RED, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(2, i.playerInfluence(Tower.BLACK, pg));
        assertEquals(3, i.playerInfluence(Tower.WHITE, pg));
        assertEquals(2, i.playerInfluence(Tower.GRAY, pg));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.BLACK);
        en.put(Colour.RED, Tower.BLACK);
        en.put(Colour.PINK, Tower.GRAY);
        en.put(Colour.BLUE, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(6, i.playerInfluence(Tower.BLACK, pg));
        assertEquals(4, i.playerInfluence(Tower.GRAY, pg));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg));
    }
}