package it.polimi.ingsw.server.model.players;

import it.polimi.ingsw.server.controller.EndOfGameChecker;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.model.board.Bag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    /**
     * It tests the Player's constructor
     */
    @Test
    void Player() {
        for (Tower t : Tower.values()) {
            Player p = new Player(t);
            assertEquals(1, p.getCoins());
            assertEquals(t, p.getTower()); //here because getTower builds a Tower
        }
        Player p1 = new Player(Tower.WHITE);
    }

    /**
     * It verifies that the Player's Dashboard is filled properly with the given StudentGroup
     */
    @Test
    void fillDashboardEntrance() {
        Player p = new Player(Tower.WHITE);
        StudentGroup st = new StudentGroup();
        p.fillDashboardEntrance(st);
        for (Colour c : Colour.values())
            Assertions.assertEquals(st.getQuantityColour(c), p.getDashboard().getEntrance().getQuantityColour(c));
        StudentGroup st1 = new StudentGroup(2);
        p.fillDashboardEntrance(st1);
        for (Colour c : Colour.values())
            Assertions.assertEquals(2, p.getDashboard().getEntrance().getQuantityColour(c));
        Bag bag = new Bag();
        bag.setStudents(new StudentGroup(26));
        StudentGroup st2 = new StudentGroup(bag.removeStudentGroup(10));
        p.fillDashboardEntrance(st2);
        for (Colour c : Colour.values())
            Assertions.assertEquals(2 + st2.getQuantityColour(c), p.getDashboard().getEntrance().getQuantityColour(c));
        ArrayList<Colour> list = bag.removeStudents(10);
        StudentGroup st3 = new StudentGroup(list);
        p.fillDashboardEntrance(st3);
        for (Colour c : Colour.values())
            Assertions.assertEquals(2 + st2.getQuantityColour(c) + st3.getQuantityColour(c), p.getDashboard().getEntrance().getQuantityColour(c));
    }

    /**
     * To tests if the method removes from the Dashboard's entrance a student adding it to an Island
     */
    @Test
    void moveToIsland() {

        Player p = new Player(Tower.GREY);
        StudentGroup st = new StudentGroup(1);
        st.addStudent(Colour.GREEN);
        st.addStudent(Colour.BLUE);
        Island i = new Island(4);
        p.fillDashboardEntrance(st);

        int beforeOnEntrance;
        int beforeOnIsland;
        for (Colour c : Colour.values()) {
            beforeOnEntrance = st.getQuantityColour(c);
            beforeOnIsland = i.getStudents().getQuantityColour(c);
            try {
                p.moveToIsland(c, i);
                assertEquals(++beforeOnIsland, i.getStudents().getQuantityColour(c));
                Assertions.assertEquals(--beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            } catch (NoStudentsException e) {
                assertEquals(beforeOnIsland, i.getStudents().getQuantityColour(c));
                Assertions.assertEquals(beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            }
            try {
                p.moveToIsland(c, i);
                assertEquals(++beforeOnIsland, i.getStudents().getQuantityColour(c));
                Assertions.assertEquals(--beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            } catch (NoStudentsException e) {
                assertEquals(beforeOnIsland, i.getStudents().getQuantityColour(c));
                Assertions.assertEquals(beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            }
        }
    }

    /**
     * It checks if it moves properly a student from the Dashboard's Entrance to the Dashboard's Hall
     */
    @Test
    void moveToHall() {
        Player p = new Player(Tower.GREY);
        StudentGroup st = new StudentGroup(1);
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(false,2);
        st.addStudent(Colour.GREEN);
        st.addStudent(Colour.BLUE);
        p.fillDashboardEntrance(st);

        int beforeOnEntrance;
        int beforeOnHall;
        for (Colour c : Colour.values()) {
            beforeOnEntrance = st.getQuantityColour(c);
            beforeOnHall = p.getDashboard().getNumStudentsHall(c);
            try {
                p.moveToHall(gm.getBoard(), c);
                assertEquals(++beforeOnHall, p.getDashboard().getNumStudentsHall(c));
                Assertions.assertEquals(--beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            } catch (NoStudentsException | FullHallException e) {
                assertEquals(beforeOnHall, p.getDashboard().getNumStudentsHall(c));
                Assertions.assertEquals(beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            }

            try {
                p.moveToHall(gm.getBoard(), c);
                assertEquals(++beforeOnHall, p.getDashboard().getNumStudentsHall(c));
                Assertions.assertEquals(--beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            } catch (NoStudentsException | FullHallException e) {
                assertEquals(beforeOnHall, p.getDashboard().getNumStudentsHall(c));
                Assertions.assertEquals(beforeOnEntrance, p.getDashboard().getEntrance().getQuantityColour(c));
            }
        }
        assertEquals(1, p.getCoins());
        p.fillDashboardEntrance(st);
        try {
            p.moveToHall(gm.getBoard(), Colour.BLUE);
            assertEquals(2, p.getCoins());
            p.moveToHall(gm.getBoard(), Colour.GREEN);
            assertEquals(3, p.getCoins());
        } catch (NoStudentsException | FullHallException e) {
            throw new RuntimeException(e);
        }
        p.getDashboard().getHall().removeStudent(Colour.BLUE);
        try {
            p.moveToHall(gm.getBoard(), Colour.BLUE);
            assertEquals(3, p.getCoins());
        } catch (NoStudentsException | FullHallException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To test Tower's getter and setters
     */
    @Test
    void TowerGettersAndSetters() {
        for (Tower tower : Tower.values()) {
            Player p = new Player(tower);
            p.setNumTowers(8);
            assertEquals(8, p.getDashboard().getNumTowers());
            assertEquals(tower, p.getTower());
        }
    }

    /**
     * To check if coins are spent according to the game's rules
     */
    @Test
    void spendCoins() {
        Player p = new Player(Tower.GREY);
        assertEquals(1, p.getCoins());
        try {
            p.spendCoins(1);
        }catch (NotEnoughCoinsException e){
            assertEquals(0, p.getCoins());
        }
        assertEquals(0, p.getCoins());
        try {
            p.spendCoins(1);
        } catch (NotEnoughCoinsException e) {
            assertEquals(0, p.getCoins());
        }
    }

    /**
     * To check if cards are played according to the game's rules
     */
    @Test
    void playCard() {
        EndOfGameChecker.resetInstance();
        Player p = new Player(Tower.WHITE);
        for (int i = 0; i < 9; i++) {
            assertFalse(p.getAssistantCard(i).isPlayed());
            try {
                p.playCard(i);
            }catch (AlreadyPlayedException e){
                e.printStackTrace();
            }
            assertTrue(p.getAssistantCard(i).isPlayed());
            assertEquals(p.getAssistantCard(i), p.getLastPlayedCard());
        }
        assertFalse(EndOfGameChecker.instance().isLastTurn());
        try {
            p.playCard(9);
        }catch (AlreadyPlayedException e){
            e.printStackTrace();
        }
        assertTrue(EndOfGameChecker.instance().isLastTurn());
        try {
            p.playCard(5);
        } catch (AlreadyPlayedException e) {
            assertTrue(p.getAssistantCard(5).isPlayed());
            assertNotEquals(p.getAssistantCard(5), p.getLastPlayedCard());
        }
    }

    /**
     * It tests if the method fills the hall with the given StudentGroup
     */
    @Test
    void fillHall() {
        StudentGroup sg = new StudentGroup(3);
        Player p = new Player(Tower.BLACK);
        try {
            p.fillHall(new Board(2),sg);
            for (Colour c: Colour.values()) {
                Assertions.assertEquals(3,p.getDashboard().getHall().getQuantityColour(c));
            }
            assertEquals(6,p.getCoins());
        } catch (FullHallException e) {
            throw new RuntimeException(e);
        }
        sg = new StudentGroup();
        sg.setNumStudents(8,Colour.BLUE);
        try{
            p.fillHall(new Board(2), sg);
        } catch (FullHallException e) {
            Assertions.assertEquals(3,p.getDashboard().getHall().getQuantityColour(Colour.BLUE));
        }
    }

    @Test
    void gettersAndSetters() {
        Player p = new Player(Tower.BLACK);
        p.setConnected(false);
        assertFalse(p.isConnected());
        p.setConnected(true);
        assertTrue(p.isConnected());
        p.setNickname("Leonardo");
        assertEquals("Leonardo",p.getNickname());
        p.setCcActivatedThisTurn(true);
        assertTrue(p.isCcActivatedThisTurn());
        assertNull(p.getLastPlayedCard());
        try {
            p.playCard(1);
        } catch (AlreadyPlayedException e) {
            assertNull(p.getLastPlayedCard());
        }
        assertEquals(p.getCards().get(1),p.getLastPlayedCard());
        assertTrue(p.getAssistantCard(1).isPlayed());
    }

}