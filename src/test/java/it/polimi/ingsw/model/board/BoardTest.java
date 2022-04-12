package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.cards.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board b;

    @BeforeEach
    void initialize() {
        b = new Board(3);
    }

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

    @Test
    void takeStudentsFromCloud() {
        b.fillClouds();
        StudentGroup sg0 = new StudentGroup(b.getClouds().get(0).getStudentsOnCloud());
        StudentGroup sg1 = new StudentGroup(b.getClouds().get(1).getStudentsOnCloud());
        StudentGroup sg2 = new StudentGroup(b.getClouds().get(2).getStudentsOnCloud());
        StudentGroup takenFromCloud0 = b.takeStudentsFromCloud(0);
        StudentGroup takenFromCloud1 = b.takeStudentsFromCloud(1);
        StudentGroup takenFromCloud2 = b.takeStudentsFromCloud(2);
        for (Colour c: Colour.values()) {
            assertEquals(sg0.getQuantityColour(c),takenFromCloud0.getQuantityColour(c));
            assertEquals(sg1.getQuantityColour(c),takenFromCloud1.getQuantityColour(c));
            assertEquals(sg2.getQuantityColour(c),takenFromCloud2.getQuantityColour(c));
        }
    }


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