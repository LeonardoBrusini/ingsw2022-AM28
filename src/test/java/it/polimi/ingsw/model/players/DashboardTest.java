package it.polimi.ingsw.model.players;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.exceptions.FullHallException;
import it.polimi.ingsw.exceptions.NoStudentsException;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.board.Bag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashboardTest {
    private Dashboard d;

    @Test
    void fillEntrance() {
        d = new Dashboard(Tower.BLACK);
        StudentGroup s = d.getEntrance();
        for(Colour c : Colour.values())
            assertEquals(0,s.getQuantityColour(c));

        Bag b = new Bag();
        StudentGroup s1 = new StudentGroup(b.removeStudentGroup(7));
        d.fillEntrance(s1);

        for(Colour c : Colour.values())
            assertEquals(d.getEntrance().getQuantityColour(c),s1.getQuantityColour(c));
    }

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

    @Test
    void getNumStudentsHall() {
        d = new Dashboard(Tower.BLACK);
        for(Colour c: Colour.values()) {
            assertEquals(d.getNumStudentsHall(c),d.getHall().getQuantityColour(c));
        }

    }

    @Test
    void addTower() {
        d = new Dashboard(Tower.BLACK);
        d.setNumTowers(8);
        d.addTower();
        int num = d.getNumTowers();
        assertEquals(9,num);
    }

    @Test
    void buildTower() {
        d = new Dashboard(Tower.BLACK);
        d.setNumTowers(8);
        d.buildTower();
        int num = d.getNumTowers();
        assertEquals(7,num);
    }

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

    @Test
    void getHall() {
        d = new Dashboard(Tower.BLACK);
        for(Colour c: Colour.values()) {
            assertEquals(0,d.getHall().getQuantityColour(c));
        }
    }

    @Test
    void getTower() {
        d = new Dashboard(Tower.BLACK);
        assertEquals(Tower.BLACK, d.getTower());
        d = new Dashboard(Tower.WHITE);
        assertEquals(Tower.WHITE, d.getTower());
    }
}