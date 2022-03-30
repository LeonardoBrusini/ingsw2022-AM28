package it.polimi.ingsw.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentGroupTest {

    @Test
    void getQuantityColour() {
        int r,r1,r2;
        StudentGroup sg = new StudentGroup();
        StudentGroup sg1 = new StudentGroup(10);
        StudentGroup sg2 = new StudentGroup(sg1);
        for(Colour c : Colour.values()) {
            r=sg.getQuantityColour(c);
            r1=sg1.getQuantityColour(c);
            r2 = sg2.getQuantityColour(c);
            assertEquals(0,r);
            assertEquals(10,r1);
            assertEquals(10,r2);
        }
    }

    @Test
    void setNumStudents() {
    }

    @Test
    void removeStudent() {
    }

    @Test
    void addStudent() {
    }
}