package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.model.StudentGroup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EntranceToHallSwitchEffectTest {
    /**
     * It tests the correct number of students in the hall and entrance
     */
    @Test
    void resolveEffect() {
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD10);
        ArrayList<Integer> expectedEntrance = new ArrayList<>();
        ArrayList<Integer> expectedHall = new ArrayList<>();
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        c.setGameManager(gm);
        c.setPlayerThisTurn(gm.getPlayers().get(0));
        c.setBoard(gm.getBoard());
        ArrayList<Colour> extractedColors1 = c.getBoard().getBag().removeStudents(2);
        ArrayList<Colour> extractedColors2 = c.getBoard().getBag().removeStudents(2);
        c.setSelectedStudentsFrom(new StudentGroup(extractedColors1));
        c.setSelectedStudentsTo(new StudentGroup(extractedColors2));
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(5));

        try{
            gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(5));
        }catch (FullHallException e){
            e.printStackTrace();
        }
        for(Colour colour: Colour.values()){
            expectedEntrance.add(colour.ordinal(), c.getPlayerThisTurn().getDashboard().getEntrance().getQuantityColour(colour) - c.getSelectedStudentsFrom().getQuantityColour(colour) + c.getSelectedStudentsTo().getQuantityColour(colour));
            expectedHall.add(colour.ordinal(), c.getPlayerThisTurn().getDashboard().getHall().getQuantityColour(colour) - c.getSelectedStudentsTo().getQuantityColour(colour) + c.getSelectedStudentsFrom().getQuantityColour(colour));
        }

        c.getCardInfo().getEffect().resolveEffect(c);
        for(Colour colour: Colour.values()){
            assertEquals(expectedEntrance.get(colour.ordinal()), c.getPlayerThisTurn().getDashboard().getEntrance().getQuantityColour(colour));
            assertEquals(expectedHall.get(colour.ordinal()),c.getPlayerThisTurn().getDashboard().getHall().getQuantityColour(colour));
        }

        /*ArrayList<Colour> extractedColors11 = c.getBoard().getBag().removeStudents(4);
        ArrayList<Colour> extractedColors12 = c.getBoard().getBag().removeStudents(4);
        c.setSelectedStudentsFrom(new StudentGroup(extractedColors11));
        c.setSelectedStudentsTo(new StudentGroup(extractedColors12));
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(10));
        try{
            gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(9));
        }catch (FullHallException e){
            e.printStackTrace();
        }*/
        //c.getCardInfo().getEffect().resolveEffect(c);
       //It throws IllegalArgumentException

        ArrayList<Colour> extractedColors21 = c.getBoard().getBag().removeStudents(2);
        ArrayList<Colour> extractedColors22 = c.getBoard().getBag().removeStudents(2);
        c.setSelectedStudentsFrom(new StudentGroup(extractedColors21));
        c.setSelectedStudentsTo(new StudentGroup(extractedColors22));
        c.setPlayerThisTurn(c.getGameManager().getPlayers().get(1));
        gm.getPlayers().get(1).getDashboard().fillEntrance(new StudentGroup());
        try{
            gm.getPlayers().get(1).getDashboard().fillHall(new StudentGroup());
        }catch (FullHallException e){
            e.printStackTrace();
        }

        //c.getCardInfo().getEffect().resolveEffect(c);
        //It throws IllegalArgumentException
    }
}