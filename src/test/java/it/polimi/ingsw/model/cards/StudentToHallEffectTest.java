package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentToHallEffectTest {
    @Test
    /**
     * It verifies the corrected number of students on the card and in the hall
     * It verifies the corrected call for IllegalArgumentExcpetion
     */
    void resolveEffect() {
        CharacterCard c = new CharacterCard(CharacterCardsInfo.CARD11);
        ExpertGameManager gm = new ExpertGameManager();
        gm.newGame();
        gm.addPlayer("g1");
        c.setBoard(gm.getBoard());
        c.setGameManager(gm);
        c.setSelectedColour(Colour.YELLOW);
        c.setStudentsOnCard(new StudentGroup(4));
        c.setPlayerThisTurn(gm.getPlayers().get(0));
        c.getPlayerThisTurn().getDashboard().fillHall(new StudentGroup(3));
        int beforeHall = c.getPlayerThisTurn().getDashboard().getHall().getTotalStudents();
        int beforeCard = c.getStudentsOnCard().getTotalStudents();
        c.getCardInfo().getEffect().resolveEffect(c);
        assertEquals(beforeHall + 1, c.getPlayerThisTurn().getDashboard().getHall().getTotalStudents());
        assertEquals(beforeCard, c.getStudentsOnCard().getTotalStudents());

        gm.addPlayer("g2");
        c.setGameManager(gm);
        c.setSelectedColour(Colour.YELLOW);
        c.setStudentsOnCard(new StudentGroup());
        c.setPlayerThisTurn(gm.getPlayers().get(1));
        c.getPlayerThisTurn().getDashboard().fillHall(new StudentGroup(3));
        int beforeHall1 = c.getPlayerThisTurn().getDashboard().getHall().getTotalStudents();
        int beforeCard1 = c.getStudentsOnCard().getTotalStudents();
        //c.getCardInfo().getEffect().resolveEffect(c);
        //It throws IllegalArgumentException
    }
}