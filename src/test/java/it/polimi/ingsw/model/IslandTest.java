package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    private Island i;

    @BeforeEach
    void initialize() {
        i = new Island(1);
        assertEquals(1,i.getIslandIndex());
    }

    @Test
    void addStudent() {
        StudentGroup sg = new StudentGroup(i.getStudents());
        for (Colour c: Colour.values()) {
            i.addStudent(c);
            assertEquals(sg.getQuantityColour(c)+1,i.getStudents().getQuantityColour(c));
            for (Colour c1: Colour.values()) {
                if(c!=c1) assertEquals(sg.getQuantityColour(c1),i.getStudents().getQuantityColour(c1));
            }
            sg = new StudentGroup(i.getStudents());
        }
    }

    //must be tested after board
    @Test
    void playerInfluence() {

    }


    @Test
    void clearStudents() {
        i.setStudents(new StudentGroup(20));
        i.clearStudents();
        for (Colour c: Colour.values()) assertEquals(0,i.getStudents().getQuantityColour(c));
        i.clearStudents();
        for (Colour c: Colour.values()) assertEquals(0,i.getStudents().getQuantityColour(c));
    }
}