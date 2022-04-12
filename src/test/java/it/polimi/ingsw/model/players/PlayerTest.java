package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Bag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
   /* @Test
    void Player(){
        for(Tower t: Tower.values()) {
            Player p = new Player("g1", t);
            assertEquals(0, p.getCoins());
            assertEquals(8,p.getDashboard().getNumTowers());
            assertEquals(t, p.getTower()); //here because getTower builds a Tower
        }
        Player p1 = new Player("g2", Tower.WHITE);
        assertEquals("g2", p1.getNickname());
    }*/

   /* @Test
    void fillEntrance(){
        Player p = new Player("g1", Tower.WHITE);
        StudentGroup st = new StudentGroup();
        p.fillDashboard(st);
        for(Colour c: Colour.values())
            assertEquals(st.getQuantityColour(c), p.getDashboard().getEntrance().getQuantityColour(c));
        StudentGroup st1 = new StudentGroup(2);
        p.fillDashboard(st1);
        for(Colour c: Colour.values())
            assertEquals(st1.getQuantityColour(c), p.getDashboard().getEntrance().getQuantityColour(c));
        Bag bag = new Bag();
        StudentGroup st2 = new StudentGroup(bag.removeStudentGroup(10));
        p.fillDashboard(st2);
        for(Colour c: Colour.values())
            assertEquals(st2.getQuantityColour(c), p.getDashboard().getEntrance().getQuantityColour(c));
        ArrayList<Colour> list = bag.removeStudents(10);
        /* Don't understand why message error list is null (NUllPointerException)
        StudentGroup st3 = new StudentGroup(list);
       // assertEquals();
        p.fillDashboard(st3);
        for(Colour c: Colour.values())
            assertEquals(st3.getQuantityColour(c), p.getDashboard().getEntrance().getQuantityColour(c));
    }*/

   /* @Test
    void moveToIsland(){
       /* Player p = new Player("g2", Tower.GRAY);
        StudentGroup st = new StudentGroup(10);
        p.fillDashboard(st);
        int before;
        for(Colour c: Colour.values()){
            before = st.getQuantityColour(c);
            p.moveToIsland(c);
            before--;
            assertEquals(before, p.getDashboard().getEntrance().getQuantityColour(c));
        }
        StudentGroup st1 = new StudentGroup();
        p.fillDashboard(st1);
        for(Colour c: Colour.values()){
            before = st1.getQuantityColour(c);
            assertEquals(0, before);
            p.moveToIsland(c);
            assertEquals(before, p.getDashboard().getEntrance().getQuantityColour(c));
        }
    }*/

    /*@Test
    void moveToHall(){
        Player p = new Player("g3", Tower.BLACK);
        StudentGroup st = new StudentGroup(10);
        p.fillDashboard(st);
        int before;
        for(Colour c: Colour.values()){
            before = st.getQuantityColour(c);
            p.moveToHall(c);
            before--;
            assertEquals(before, p.getDashboard().getEntrance().getQuantityColour(c));
            assertEquals(1, p.getDashboard().getHall().getQuantityColour(c));
        }

        Player p1 = new Player("g4", Tower.WHITE);
        StudentGroup st1 = new StudentGroup();
        p1.fillDashboard(st1);
        for(Colour c: Colour.values()){
            p1.moveToHall(c);
            assertEquals(0, p1.getDashboard().getEntrance().getQuantityColour(c));
            assertEquals(0, p1.getDashboard().getHall().getQuantityColour(c));
        }
    }*/

    /*@Test
    void getTower(){
        for(Tower tower: Tower.values()) {
            Player p = new Player("g5", tower);
            assertEquals(8, p.getDashboard().getNumTowers());
            Tower t = p.getTower();
            assertEquals(tower, t);
            assertEquals(7, p.getDashboard().getNumTowers());
        }
    }*/

   /* @Test
    void addCoin(){
        Player p = new Player("g6", Tower.GRAY);
        assertEquals(0, p.getCoins());
        p.addCoin();
        assertEquals(1, p.getCoins());
    }*/

   /* @Test
    void spendCoins(){
        Player p = new Player("g6", Tower.GRAY);
        assertEquals(0, p.getCoins());
        p.spendCoins(1);
        assertEquals(0, p.getCoins());
        p.addCoin();
        p.addCoin();
        p.spendCoins(1);
        assertEquals(1,p.getCoins());
    }*/

    /*
    To test after deciding who creates AssistantCards
    @Test
    void playCard(){
        Player p = new Player("g7", Tower.WHITE);
        for(int i = 0; i < 10; i++) {
            assertFalse(p.getAssistantCard(i).getPlayed());
            p.playCard(i);
            assertTrue(p.getAssistantCard(i).getPlayed());
            assertEquals(p.getAssistantCard(i), p.getLastPlayedCard());
        }
    }
    */

}