package it.polimi.ingsw.model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StudentGroupTest {
    private StudentGroup sg;
    private StudentGroup sg1;
    private StudentGroup sg2;
    private StudentGroup sg3;

    @BeforeEach
    void initializeTest() {
        sg = new StudentGroup();
        sg1 = new StudentGroup(10);
        sg2 = new StudentGroup(sg1);
        ArrayList<Colour> colourList = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            colourList.add(Colour.BLUE);
            colourList.add(Colour.YELLOW);
            colourList.add(Colour.GREEN);
            colourList.add(Colour.RED);
            colourList.add(Colour.PINK);
        }
        sg3 = new StudentGroup(colourList);
    }

    @Test
    void getQuantityColour() {
        for(Colour c : Colour.values()) {
            assertEquals(0,sg.getQuantityColour(c));
            assertEquals(10,sg1.getQuantityColour(c));
            assertEquals(10,sg2.getQuantityColour(c));
        }
    }

    @Test
    void setNumStudents() {
        for (Colour c : Colour.values()) {
            assertEquals(0,sg.getQuantityColour(c));
        }
        for (Colour c1 : Colour.values()) {
            sg.setNumStudents(10,c1);
            assertEquals(10,sg.getQuantityColour(c1));
            for (Colour c2: Colour.values()) {
                if(!c2.equals(c1)) assertEquals(0,sg.getQuantityColour(c2));
            }
            sg.setNumStudents(-1, c1);
            assertEquals(10,sg.getQuantityColour(c1));
            for (Colour c2: Colour.values()) {
                if(!c2.equals(c1)) assertEquals(0,sg.getQuantityColour(c2));
            }
            sg.setNumStudents(0,c1);
            assertEquals(0,sg.getQuantityColour(c1));
            for (Colour c2: Colour.values()) {
                if(!c2.equals(c1)) assertEquals(0,sg.getQuantityColour(c2));
            }
        }
    }

    @Test
    void removeStudent() {
        StudentGroup sg1before;
        StudentGroup sg2before;
        for(Colour c : Colour.values()) {
            sg1before = new StudentGroup(sg1);
            sg2before = new StudentGroup(sg2);
            sg.removeStudent(c);
            assertEquals(0,sg.getQuantityColour(c));
            for (Colour c2: Colour.values()) {
                if(!c2.equals(c)) assertEquals(0,sg.getQuantityColour(c2));
            }
            sg1.removeStudent(c);
            sg2.removeStudent(c);
            for (Colour c2: Colour.values()) {
                if(c2.equals(c)) {
                    assertEquals(sg1before.getQuantityColour(c2)-1,sg1.getQuantityColour(c2));
                    assertEquals(sg2before.getQuantityColour(c2)-1,sg2.getQuantityColour(c2));
                } else {
                    assertEquals(sg1before.getQuantityColour(c2),sg1.getQuantityColour(c2));
                    assertEquals(sg2before.getQuantityColour(c2),sg2.getQuantityColour(c2));
                }
            }
        }
    }

    @Test
    void addStudent() {
        StudentGroup sgBefore;
        StudentGroup sg1before;
        StudentGroup sg2before;
        for (int i=0;i<100;i++) {
            for(Colour c : Colour.values()) {
                sgBefore = new StudentGroup(sg);
                sg1before = new StudentGroup(sg1);
                sg2before = new StudentGroup(sg2);
                sg.addStudent(c);
                sg1.addStudent(c);
                sg2.addStudent(c);
                for (Colour c2: Colour.values()) {
                    if(c2.equals(c)) {
                        assertEquals(sgBefore.getQuantityColour(c2)+1,sg.getQuantityColour(c2));
                        assertEquals(sg1before.getQuantityColour(c2)+1,sg1.getQuantityColour(c2));
                        assertEquals(sg2before.getQuantityColour(c2)+1,sg2.getQuantityColour(c2));
                    } else {
                        assertEquals(sgBefore.getQuantityColour(c2),sg.getQuantityColour(c2));
                        assertEquals(sg1before.getQuantityColour(c2),sg1.getQuantityColour(c2));
                        assertEquals(sg2before.getQuantityColour(c2),sg2.getQuantityColour(c2));
                    }
                }
            }
        }
    }

    @Test
    void empty() {
        assertTrue(sg.empty());
        assertFalse(sg3.empty());
        sg.addStudent(Colour.GREEN);
        assertFalse(sg.empty());
    }
    
    @Test
    void setStudents() {
        sg.setStudents(sg3);
        for (Colour c : Colour.values()) {
            assertEquals(sg3.getQuantityColour(c),sg.getQuantityColour(c));
        }
    }
}