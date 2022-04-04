package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DashboardTest {
    private Dashboard d;
    private Bag b;

    @Test
    void fillEntrance() {
        d = new Dashboard(8, Tower.BLACK);
        StudentGroup s = d.getEntrance();
        for(Colour c : Colour.values())
            assertEquals(0,s.getQuantityColour(c));

        b = new Bag();
        StudentGroup s1 = new StudentGroup(b.removeStudentGroup(7));
        /*d.fillEntrance(s1);

        for(Colour c : Colour.values())
            assertEquals(d.getEntrance().getQuantityColour(c),s1.getQuantityColour(c));

        */
    }

    @Test
    void addToHall() {
        d = new Dashboard(8, Tower.BLACK);
        Random r = new Random();
        StudentGroup s = d.getEntrance();
        for(Colour c : Colour.values()) {
            assertEquals(0, s.getQuantityColour(c));
        }

        StudentGroup s1 = new StudentGroup(7);
        d.fillEntrance(s1);

        Colour[] e = Colour.values();
        int pos;

        for(int i = 0 ; i<5; i++){
            pos = r.nextInt(e.length);
            d.addToHall(e[pos]);
        }

        for(Colour c: e){
            assertEquals(7-d.getEntrance().getQuantityColour(c),d.getHall().getQuantityColour(c));
        }

    }

    @Test
    void removeFromEntrance() {
        d = new Dashboard(8, Tower.BLACK);
        StudentGroup s = new StudentGroup(3);
        d.fillEntrance(s);
        assertEquals(3,d.getEntrance().getQuantityColour(Colour.YELLOW));
        assertEquals(0,d.getHall().getQuantityColour(Colour.YELLOW));

        d.removeFromEntrance(Colour.YELLOW);
        assertEquals(2,d.getEntrance().getQuantityColour(Colour.YELLOW));
        assertEquals(0,d.getHall().getQuantityColour(Colour.YELLOW));

    }

    @Test
    void getNumStudentsHall() {
        d = new Dashboard(8,Tower.BLACK);
        for(Colour c: Colour.values()) {
            assertEquals(d.getNumStudentsHall(c),d.getHall().getQuantityColour(c));
        }

    }

    @Test
    void addTower() {
        d = new Dashboard(8, Tower.BLACK);
        d.addTower();
        int num = d.getNumTower();
        assertEquals(9,num);
    }

    @Test
    void buildTower() {
        d = new Dashboard(8, Tower.BLACK);
        d.buildTower();
        int num = d.getNumTower();
        assertEquals(7,num);
    }

    @Test
    void getEntrance() {
        d = new Dashboard(8,Tower.BLACK);
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
        d = new Dashboard(8,Tower.BLACK);
        for(Colour c: Colour.values()) {
            assertEquals(0,d.getHall().getQuantityColour(c));
        }
    }
}