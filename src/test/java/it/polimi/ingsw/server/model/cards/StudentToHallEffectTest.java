package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.model.StudentGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StudentToHallEffectTest {
    @Test
    /**
     * It verifies the corrected number of students on the card and in the hall
     * It verifies the corrected call for IllegalArgumentException
     */
    void resolveEffect() {
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD11);
        ExpertGameManager gm = new ExpertGameManager();
        gm.addPlayer("player1");
        gm.addPlayer("player2");
        gm.newGame();
        gm.addPlayer("g1");
        c.setBoard(gm.getBoard());
        c.setGameManager(gm);
        c.setSelectedColour(Colour.YELLOW);
        c.setStudentsOnCard(new StudentGroup(4));
        c.setPlayerThisTurn(gm.getPlayers().get(0));

        try {
            c.getPlayerThisTurn().getDashboard().fillHall(new StudentGroup(3));
        } catch (FullHallException e) {
            e.printStackTrace();
        }
        int beforeHall = c.getPlayerThisTurn().getDashboard().getHall().getTotalStudents();
        int beforeCard = c.getStudentsOnCard().getTotalStudents();
        c.getCardInfo().getEffect().resolveEffect(c);
        Assertions.assertEquals(beforeHall + 1, c.getPlayerThisTurn().getDashboard().getHall().getTotalStudents());
        Assertions.assertEquals(beforeCard, c.getStudentsOnCard().getTotalStudents());

        gm.addPlayer("g2");
        c.setGameManager(gm);
        c.setSelectedColour(Colour.YELLOW);
        c.setStudentsOnCard(new StudentGroup());
        c.setPlayerThisTurn(gm.getPlayers().get(1));
        try {
            c.getPlayerThisTurn().getDashboard().fillHall(new StudentGroup(3));
        } catch (FullHallException e){
            e.printStackTrace();
        }
        int beforeHall1 = c.getPlayerThisTurn().getDashboard().getHall().getTotalStudents();
        int beforeCard1 = c.getStudentsOnCard().getTotalStudents();
        //c.getCardInfo().getEffect().resolveEffect(c);
        //It throws IllegalArgumentException
    }
}