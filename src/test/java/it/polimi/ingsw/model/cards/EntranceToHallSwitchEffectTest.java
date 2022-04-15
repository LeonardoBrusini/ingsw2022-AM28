package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EntranceToHallSwitchEffectTest {
    /**
     * It tests the correct number of students in the hall and entrance
     */
    @Test
    void resolveEffect() {
        CharacterCard c = new CharacterCard(CharacterCardsInfo.CARD10);
        ArrayList<Integer> expectedEntrance = new ArrayList<>();
        ArrayList<Integer> exptectedHall = new ArrayList<>();
        ExpertGameManager gm = new ExpertGameManager();
        gm.newGame();
        c.setGameManager(gm);
        gm.addPlayer("g1");
        gm.addPlayer("g2");
        c.setPlayerThisTurn(gm.getPlayers().get(0));
        c.setBoard(gm.getBoard());
        ArrayList<Colour> extractedColors1 = c.getBoard().getBag().removeStudents(2);
        ArrayList<Colour> extractedColors2 = c.getBoard().getBag().removeStudents(2);
        c.setSelectedStudentsFrom(new StudentGroup(extractedColors1));
        c.setSelectedStudentsTo(new StudentGroup(extractedColors2));
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(10));
        gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(11));
        for(Colour colour: Colour.values()){
            expectedEntrance.add(c.getPlayerThisTurn().getDashboard().getEntrance().getQuantityColour(colour) - c.getSelectedStudentsFrom().getQuantityColour(colour) + c.getSelectedStudentsTo().getQuantityColour(colour));
            exptectedHall.add(c.getPlayerThisTurn().getDashboard().getHall().getQuantityColour(colour) - c.getSelectedStudentsTo().getQuantityColour(colour) + c.getSelectedStudentsFrom().getQuantityColour(colour));
        }
        c.getCardInfo().getEffect().resolveEffect(c);
        int i = 0;
        for(Colour colour: Colour.values()){
            assertEquals(expectedEntrance.get(i), c.getPlayerThisTurn().getDashboard().getEntrance().getQuantityColour(colour));
            assertEquals(exptectedHall.get(i),c.getPlayerThisTurn().getDashboard().getHall().getQuantityColour(colour));
            i++;
        }

        ArrayList<Colour> extractedColors11 = c.getBoard().getBag().removeStudents(4);
        ArrayList<Colour> extractedColors12 = c.getBoard().getBag().removeStudents(4);
        c.setSelectedStudentsFrom(new StudentGroup(extractedColors11));
        c.setSelectedStudentsTo(new StudentGroup(extractedColors12));
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(10));
        gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(11));
        //c.getCardInfo().getEffect().resolveEffect(c);
       //It throws IllegalArgumentException

        ArrayList<Colour> extractedColors21 = c.getBoard().getBag().removeStudents(2);
        ArrayList<Colour> extractedColors22 = c.getBoard().getBag().removeStudents(2);
        c.setSelectedStudentsFrom(new StudentGroup(extractedColors21));
        c.setSelectedStudentsTo(new StudentGroup(extractedColors22));
        c.setPlayerThisTurn(c.getGameManager().getPlayers().get(1));
        gm.getPlayers().get(1).getDashboard().fillEntrance(new StudentGroup());
        gm.getPlayers().get(1).getDashboard().fillHall(new StudentGroup());
        //c.getCardInfo().getEffect().resolveEffect(c);
        //It throws IllegalArgumentException
    }
}