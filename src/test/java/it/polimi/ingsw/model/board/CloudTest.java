package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.StudentGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    Cloud c ;

    /**
     * To test if the cloud is empty
     */
    @Test
    void empty() {
        c = new Cloud();
        assertTrue(c.empty());
        c.addGroup(new StudentGroup(5));
        assertFalse(c.empty());
    }

    /**
     * It verifies that students are correctly removed from the Cloud
     */
    @Test
    void clearStudents() {
        c = new Cloud();
        c.addGroup(new StudentGroup(5));
        c.clearStudents();
        for(Colour colour : Colour.values()){
            assertEquals(0,c.getStudentsOnCloud().getQuantityColour(colour));
        }

    }

    /**
     * To test if StudentsGroup are correctly added to the clouds, according to the game's rules
     */
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

    /**
     * It verifies that the method returns the right number of students on the cloud, without adding or removing them
     */
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