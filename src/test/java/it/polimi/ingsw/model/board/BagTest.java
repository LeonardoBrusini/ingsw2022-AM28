package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.StudentGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    private Bag b;

    @BeforeEach
    void initialize() {
        b = new Bag();
        for(Colour c: Colour.values()) assertEquals(0,b.getStudents().getQuantityColour(c));
    }

    //must be tested after board
    @Test
    void initializeIslands() {

    }

    @Test
    void removeStudents() {
        b.setStudents(new StudentGroup(26));
        ArrayList<Colour> extractedStudents = b.removeStudents(10);
        assertEquals(10,extractedStudents.size());
        assertEquals(120,b.getTotalStudents());
        StudentGroup sg = new StudentGroup(extractedStudents);
        for (Colour c: Colour.values()) assertEquals(26-b.getStudents().getQuantityColour(c),sg.getQuantityColour(c));
        b.addStudent(sg);
        for(Colour c: Colour.values()) assertEquals(26,b.getStudents().getQuantityColour(c));
        extractedStudents = b.removeStudents(130);
        assertEquals(130,extractedStudents.size());
        assertEquals(0,b.getTotalStudents());
        sg = new StudentGroup(extractedStudents);
        for (Colour c: Colour.values()) assertEquals(26-b.getStudents().getQuantityColour(c),sg.getQuantityColour(c));
        b.addStudent(sg);
        extractedStudents = b.removeStudents(200);
        sg = new StudentGroup(extractedStudents);
        assertEquals(130,extractedStudents.size());
        assertEquals(0,b.getTotalStudents());
        for (Colour c: Colour.values()) assertEquals(26-b.getStudents().getQuantityColour(c),sg.getQuantityColour(c));
        b.setStudents(sg);
        try {
            b.removeStudents(-10);
        } catch (IllegalArgumentException iae) {
            assertEquals(130,b.getTotalStudents());
        }
        for (Colour c: Colour.values()) assertEquals(26,b.getStudents().getQuantityColour(c));
    }

    @Test
    void addStudent() {
        StudentGroup sg = new StudentGroup(2);
        b.addStudent(sg);
        for (Colour c : Colour.values()) {
            assertEquals(2, b.getStudents().getQuantityColour(c));
        }
        sg = new StudentGroup(24);
        b.addStudent(sg);
        for (Colour c : Colour.values()) {
            assertEquals(26, b.getStudents().getQuantityColour(c));
        }
        b.addStudent( new StudentGroup());
        for (Colour c : Colour.values()) {
            assertEquals(26, b.getStudents().getQuantityColour(c));
        }
    }

    @Test
    void getNumOfStudents() {
        assertEquals(0,b.getTotalStudents());
        b.addStudent(new StudentGroup(0));
        assertEquals(0,b.getTotalStudents());
        b.addStudent(new StudentGroup(2));
        assertEquals(10,b.getTotalStudents());
        b.addStudent(new StudentGroup(24));
        assertEquals(130,b.getTotalStudents());
        StudentGroup sg = new StudentGroup();
        sg.addStudent(Colour.RED);
        sg.addStudent(Colour.RED);
        sg.addStudent(Colour.PINK);
        sg.addStudent(Colour.YELLOW);
        b.addStudent(sg);
        assertEquals(134,b.getTotalStudents());
    }

   @Test
    void removeStudentGroup(){
        StudentGroup st;
        int tmp = 0;
        b.setStudents(new StudentGroup(26));
        st = b.removeStudentGroup(130);
        for(Colour color: Colour.values()) {
           tmp += st.getQuantityColour(color);
        }
        assertEquals(130, tmp);
        assertEquals(0, b.getTotalStudents());
        b.setStudents(new StudentGroup(26));
        st = b.removeStudentGroup(120);
        tmp = 0;
        for(Colour color: Colour.values()) {
            tmp += st.getQuantityColour(color);
        }
        assertEquals(120, tmp);
        assertEquals(10, b.getTotalStudents());
    }
    @Test
    void isEmpty(){
        b.setStudents(new StudentGroup());
        assertEquals(0, b.getTotalStudents());
        assertTrue(b.isEmpty());
    }

}