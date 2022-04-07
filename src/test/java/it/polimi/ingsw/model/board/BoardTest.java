package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board b;

    @Test
    void instance() {
        /*b = Board.instance();
        assertEquals(20, b.getCoins());
        //clouds AFTER GAME MANAGER
        assertTrue(b.getIslandManager().getArchipelagoByIslandIndex(b.getMotherNature().getIslandIndex()).isPresenceMotherNature());
        for (Colour c: Colour.values()) {
            assertFalse(b.getProfessorGroup().getProfessors().containsKey(c));
            assertEquals(24,b.getBag().getStudents().getQuantityColour(c));
        }
        assertEquals(3, b.getCharacterCards().size());*/
    }

    //AFTER GAME MANAGER
    @Test
    void fillClouds() {
    }

    //AFTER GAME MANAGER
    @Test
    void takeStudentsFromCloud() {
    }


    @Test
    void assignProfessor() {
        /*b = Board.instance();
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
        assertEquals(Tower.BLACK,b.getProfessorGroup().getTower(Colour.YELLOW));*/
    }

    @Test
    void resetInstance() {
       /* b = Board.instance();
        b.resetInstance();
        assertEquals(20, b.getCoins());
        //clouds AFTER GAME MANAGER
        assertTrue(b.getIslandManager().getArchipelagoByIslandIndex(b.getMotherNature().getIslandIndex()).isPresenceMotherNature());
        for (Colour c: Colour.values()) {
            assertFalse(b.getProfessorGroup().getProfessors().containsKey(c));
            assertEquals(24,b.getBag().getStudents().getQuantityColour(c));
        }
        assertEquals(3, b.getCharacterCards().size());*/
    }
}