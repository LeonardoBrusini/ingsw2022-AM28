package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.cards.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board b;

    /**
     * To do before each test
     */
    @BeforeEach
    void initialize() {
        b = new Board(3);
    }

    /**
     * To test that, thanks to the method, the clouds are correctly filled with students when required
     */
    @Test
    void fillClouds() {
        b.fillClouds();
        StudentGroup studentsOnCards = new StudentGroup();
        for (CharacterCard card: b.getCharacterCards()) {
            for (Colour c: Colour.values()) {
                studentsOnCards.setNumStudents(card.getStudentsOnCard().getQuantityColour(c)+studentsOnCards.getQuantityColour(c),c);
            }
        }
        int expected;
        for (Colour c: Colour.values()) {
            expected = 24-studentsOnCards.getQuantityColour(c)-b.getBag().getStudents().getQuantityColour(c)-b.getClouds().get(1).getStudentsOnCloud().getQuantityColour(c)-b.getClouds().get(2).getStudentsOnCloud().getQuantityColour(c);
            assertEquals(expected,b.getClouds().get(0).getStudentsOnCloud().getQuantityColour(c));
            expected = 24-studentsOnCards.getQuantityColour(c)-b.getBag().getStudents().getQuantityColour(c)-b.getClouds().get(0).getStudentsOnCloud().getQuantityColour(c)-b.getClouds().get(2).getStudentsOnCloud().getQuantityColour(c);
            assertEquals(expected,b.getClouds().get(1).getStudentsOnCloud().getQuantityColour(c));
            expected = 24-studentsOnCards.getQuantityColour(c)-b.getBag().getStudents().getQuantityColour(c)-b.getClouds().get(0).getStudentsOnCloud().getQuantityColour(c)-b.getClouds().get(1).getStudentsOnCloud().getQuantityColour(c);
            assertEquals(expected,b.getClouds().get(2).getStudentsOnCloud().getQuantityColour(c));
        }
    }

    /**
     * To test the right assignment of a Professor to the tower of the Player with the right influence to have the right on the Professor
     */
    @Test
    void assignProfessor() {
        b.assignProfessor(Colour.BLUE, Tower.BLACK);
        assertEquals(Tower.BLACK,b.getProfessorGroup().getTower(Colour.BLUE));
        for(Colour c: Colour.values()) if(c!=Colour.BLUE) assertFalse(b.getProfessorGroup().getProfessors().containsKey(c));
        b.assignProfessor(Colour.GREEN,Tower.GRAY);
        b.assignProfessor(Colour.PINK,Tower.GRAY);
        b.assignProfessor(Colour.RED,Tower.WHITE);
        b.assignProfessor(Colour.YELLOW,Tower.BLACK);
        assertEquals(Tower.GRAY,b.getProfessorGroup().getTower(Colour.GREEN));
        assertEquals(Tower.GRAY,b.getProfessorGroup().getTower(Colour.PINK));
        assertEquals(Tower.WHITE,b.getProfessorGroup().getTower(Colour.RED));
        assertEquals(Tower.BLACK,b.getProfessorGroup().getTower(Colour.YELLOW));
    }

    /**
     * It verifies that Mother Nature is moved properly according to the game's rules
     */
    @Test
    void moveMotherNature() {
        b.getMotherNature().setIsland(1);
        b.moveMotherNature(5);
        assertEquals(6,b.getMotherNature().getIslandIndex());
        assertFalse(b.getIslandManager().getArchipelagoByIslandIndex(1).isPresenceMotherNature());
        assertTrue(b.getIslandManager().getArchipelagoByIslandIndex(6).isPresenceMotherNature());

        b.getMotherNature().setIsland(7);
        b.moveMotherNature(5);
        assertEquals(12,b.getMotherNature().getIslandIndex());
        assertFalse(b.getIslandManager().getArchipelagoByIslandIndex(7).isPresenceMotherNature());
        assertTrue(b.getIslandManager().getArchipelagoByIslandIndex(12).isPresenceMotherNature());

        b.getMotherNature().setIsland(8);
        b.moveMotherNature(5);
        assertEquals(1,b.getMotherNature().getIslandIndex());
        assertFalse(b.getIslandManager().getArchipelagoByIslandIndex(8).isPresenceMotherNature());
        assertTrue(b.getIslandManager().getArchipelagoByIslandIndex(1).isPresenceMotherNature());

        b.getMotherNature().setIsland(12);
        b.moveMotherNature(1);
        assertEquals(1,b.getMotherNature().getIslandIndex());
        assertFalse(b.getIslandManager().getArchipelagoByIslandIndex(12).isPresenceMotherNature());
        assertTrue(b.getIslandManager().getArchipelagoByIslandIndex(1).isPresenceMotherNature());
    }

    /**
     * To test generic getter and setter methods in Board
     */
    @Test
    void getterAndSetterTest() {
        Bag bag = new Bag();
        b.setBag(bag);
        assertEquals(bag,b.getBag());

        ArrayList<CharacterCard> cards = new ArrayList<>();
        b.setCharacterCards(cards);
        assertEquals(cards,b.getCharacterCards());

        ArrayList<Cloud> clouds = new ArrayList<>();
        b.setClouds(clouds);
        assertEquals(clouds,b.getClouds());

        b.setCoins(10);
        assertEquals(10,b.getCoins());

        IslandManager im = new IslandManager(4);
        b.setIslandManager(im);
        assertEquals(im,b.getIslandManager());

        MotherNature mn = new MotherNature();
        b.setMotherNature(mn);
        assertEquals(mn,b.getMotherNature());

        ProfessorGroup professors = new ProfessorGroup();
        b.setProfessorGroup(professors);
        assertEquals(professors,b.getProfessorGroup());
    }
}