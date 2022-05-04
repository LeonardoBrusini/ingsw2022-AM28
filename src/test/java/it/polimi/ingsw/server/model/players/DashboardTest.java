package it.polimi.ingsw.server.model.players;

import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Bag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashboardTest {
    private Dashboard d;

    /**
     * It checks if the Dashboard's Entrance is filled properly by a given StudentGroup
     */
    @Test
    void fillEntrance() {
        d = new Dashboard(Tower.BLACK);
        StudentGroup s = d.getEntrance();
        for(Colour c : Colour.values())
            assertEquals(0,s.getQuantityColour(c));

        Bag b = new Bag();
        b.getStudents().setStudents(new StudentGroup(10));
        StudentGroup s1 = new StudentGroup(b.removeStudentGroup(7));
        d.fillEntrance(s1);

        for(Colour c : Colour.values())
            assertEquals(d.getEntrance().getQuantityColour(c),s1.getQuantityColour(c));
    }

    /**
     * It verifies if in the Dashboard's Hall is correctly added the given student
     */
    @Test
    void addToHall() {
        d = new Dashboard(Tower.BLACK);
        StudentGroup s = d.getHall();
        for(Colour c : Colour.values()) {
            assertEquals(0, s.getQuantityColour(c));
        }

        for(Colour c: Colour.values()) {
            d.getHall().setStudents(new StudentGroup());
            for(int i=1;i<=11; i++) {
                try {
                    d.addToHall(c);
                    assertEquals(i,d.getHall().getQuantityColour(c));
                    assertEquals(i,d.getHall().getTotalStudents());
                } catch (FullHallException e) {
                    assertEquals(10,d.getHall().getQuantityColour(c));
                    assertEquals(10,d.getHall().getTotalStudents());
                }
            }
        }
    }

    /**
     * It checks if the given student is removed properly by the Dashboard's Entrance
     */
    @Test
    void removeFromEntrance() {
        d = new Dashboard(Tower.BLACK);
        int numStudents = 5;
        for (Colour c: Colour.values()) {
            d.getHall().setStudents(new StudentGroup());
            for (int i=0;i<numStudents;i++) {
                d.getEntrance().addStudent(c);
            }
            for (int i=numStudents;i>=0;i--) {
                try {
                    d.removeFromEntrance(c);
                    assertEquals(i-1,d.getEntrance().getQuantityColour(c));
                    assertEquals(i-1,d.getEntrance().getTotalStudents());
                } catch (NoStudentsException e) {
                    assertEquals(0,d.getEntrance().getQuantityColour(c));
                    assertEquals(0,d.getEntrance().getTotalStudents());
                }
            }
        }
    }

    /**
     * It returns the total number of students in the Hall of a selected Colour
     */
    @Test
    void getNumStudentsHall() {
        d = new Dashboard(Tower.BLACK);
        for(Colour c: Colour.values()) {
            assertEquals(d.getNumStudentsHall(c),d.getHall().getQuantityColour(c));
        }
    }

    /**
     * It checks if the methods adds the right number of Tower's in the Dashboard
     */
    @Test
    void addTower() {
        d = new Dashboard(Tower.BLACK);
        d.setNumTowers(8);
        d.addTower();
        int num = d.getNumTowers();
        assertEquals(9,num);
    }

    /**
     *  It checks if the methods removes the right number of Tower's in the Dashboard
     */
    @Test
    void buildTower() {
        d = new Dashboard(Tower.BLACK);
        d.setNumTowers(8);
        d.buildTower();
        int num = d.getNumTowers();
        assertEquals(7,num);
    }

    /**
     * To test if is returned the Entrance's pointer
     */
    @Test
    void getEntrance() {
        d = new Dashboard(Tower.BLACK);
        for(Colour c: Colour.values()) {
            assertEquals(0,d.getEntrance().getQuantityColour(c));
        }

        StudentGroup s = new StudentGroup(7);
        d.fillEntrance(s);
        for(Colour c: Colour.values()) {
            assertEquals(7,d.getEntrance().getQuantityColour(c));
        }
    }
    /**
     * To test if is returned the Hall's pointer
     */
    @Test
    void getHall() {
        d = new Dashboard(Tower.BLACK);
        for(Colour c: Colour.values()) {
            assertEquals(0,d.getHall().getQuantityColour(c));
        }
    }

    /**
     * To tests if is returned the right Tower's colour assigned to the Dashboard
     */
    @Test
    void getTower() {
        d = new Dashboard(Tower.BLACK);
        assertEquals(Tower.BLACK, d.getTower());
        d = new Dashboard(Tower.WHITE);
        assertEquals(Tower.WHITE, d.getTower());
    }

    /**
     * Test removeFromHall method
     */
    @Test
    void removeFromHall(){
        d = new Dashboard(Tower.BLACK);
        StudentGroup s = new StudentGroup(1);
        s.addStudent(Colour.YELLOW);
        try {
            d.fillHall(s);
        } catch (FullHallException e) {
            throw new RuntimeException(e);
        }
        d.removeFromHall(Colour.BLUE);
        d.removeFromHall(Colour.GREEN);
        d.removeFromHall(Colour.RED);
        d.removeFromHall(Colour.PINK);
        d.removeFromHall(Colour.YELLOW);
        d.removeFromHall(Colour.YELLOW);
        for (Colour c : Colour.values()) assertEquals(0,d.getHall().getQuantityColour(c));
        try {
            d.removeFromHall(Colour.YELLOW);
        } catch (IllegalArgumentException e){
            assertEquals(0,d.getHall().getQuantityColour(Colour.YELLOW));
        }
    }

    /**
     * Tests fillHall method
     */
    @Test
    void fillHall() {
        d = new Dashboard(Tower.BLACK);
        d = new Dashboard(Tower.BLACK);
        StudentGroup s = new StudentGroup(1);
        s.addStudent(Colour.YELLOW);
        try {
            d.fillHall(s);
        } catch (FullHallException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1,d.getHall().getQuantityColour(Colour.BLUE));
        assertEquals(1,d.getHall().getQuantityColour(Colour.GREEN));
        assertEquals(1,d.getHall().getQuantityColour(Colour.PINK));
        assertEquals(1,d.getHall().getQuantityColour(Colour.RED));
        assertEquals(2,d.getHall().getQuantityColour(Colour.YELLOW));
        try{
            d.fillHall(new StudentGroup(10));
        }catch (FullHallException e) {
            assertEquals(1,d.getHall().getQuantityColour(Colour.BLUE));
            assertEquals(1,d.getHall().getQuantityColour(Colour.GREEN));
            assertEquals(1,d.getHall().getQuantityColour(Colour.PINK));
            assertEquals(1,d.getHall().getQuantityColour(Colour.RED));
            assertEquals(2,d.getHall().getQuantityColour(Colour.YELLOW));
        }
    }
}