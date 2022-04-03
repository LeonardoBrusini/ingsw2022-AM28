package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    Cloud c ;

    @Test
    void empty() {
        this.c = new Cloud();
        assertEquals(true,c.empty());
        this.c.addGroup(new StudentGroup(5));
        assertEquals(false,c.empty());
    }

    @Test
    void clearStudents() {
        this.c = new Cloud();
        c.addGroup(new StudentGroup(5));
        c.clearStudents();
        for(Colour colour : Colour.values()){
            assertEquals(0,c.getStudentsOnCloud().getQuantityColour(colour));
        }

    }

    @Test
    void addGroup() {
        c = new Cloud();
        c.addGroup( new StudentGroup( 3 ) );
        for(Colour colour : Colour.values()){
            assertEquals(3,c.getStudentsOnCloud().getQuantityColour(colour));
        }

        c.addGroup( new StudentGroup( 5 ) );
        for(Colour colour : Colour.values()){
            assertEquals(8,c.getStudentsOnCloud().getQuantityColour(colour));
        }
    }

    @Test
    void getStudentsOnCloud() {
        c = new Cloud();
        StudentGroup s = c.getStudentsOnCloud();

        for(Colour colour: Colour.values()) {
            assertEquals(s.getQuantityColour(colour), c.getStudentsOnCloud().getQuantityColour(colour));
            assertEquals(0,c.getStudentsOnCloud().getQuantityColour(colour));
        }
    }
}