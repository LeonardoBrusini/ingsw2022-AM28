package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.players.Dashboard;
import it.polimi.ingsw.server.model.players.Player;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardToEntranceSwitchEffectTest {
    private final CharacterCard c = new CharacterCard(CharacterCardInfo.CARD7);
    /**
     * It verifies the correct throw of Exceptions and the correct number of students
     * at the end of computation in each area
     */
    @Test
    void resolveEffect() {
        Board b = new Board(2);
        c.initializeCards(b.getBag());
        Player p = new Player(Tower.WHITE);
        c.setPlayerThisTurn(p);
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        StudentGroup studentOnCard = new StudentGroup(6);
        c.setStudentsOnCard(studentOnCard);
        StudentGroup studentsTo = new StudentGroup();
        StudentGroup sp = new StudentGroup(6);
        StudentGroup studentsFrom = new StudentGroup();
        d.fillEntrance(sp);
        c.setSelectedStudentsTo(studentsTo);
        c.setSelectedStudentsFrom(studentsFrom);
        //c.getCardInfo().getEffect().resolveEffect(c);
        //The function returns IllegalArgumentException

        ArrayList<Integer> expectedCard = new ArrayList<>();
        ArrayList<Integer> expectedDashboard = new ArrayList<>();
        Player p1 = new Player(Tower.WHITE);
        c.setPlayerThisTurn(p1);
        Dashboard d1 = c.getPlayerThisTurn().getDashboard();
        StudentGroup studentOnCard1 = new StudentGroup(6);
        c.setStudentsOnCard(studentOnCard1);
        ArrayList<Colour> extractedColours1 = b.getBag().removeStudents(3);
        ArrayList<Colour> extractedColours2 = b.getBag().removeStudents(3);
        StudentGroup studentsTo1 = new StudentGroup(extractedColours1);
        StudentGroup sp1 = new StudentGroup(6);
        StudentGroup studentsFrom1 = new StudentGroup(extractedColours2);
        d1.fillEntrance(sp1);
        c.setSelectedStudentsTo(studentsTo1);
        c.setSelectedStudentsFrom(studentsFrom1);
        for(Colour cc: Colour.values()){
            expectedCard.add(c.getStudentsOnCard().getQuantityColour(cc) + c.getSelectedStudentsTo().getQuantityColour(cc) - c.getSelectedStudentsFrom().getQuantityColour(cc));
            expectedDashboard.add(sp.getQuantityColour(cc) - c.getSelectedStudentsTo().getQuantityColour(cc) + c.getSelectedStudentsFrom().getQuantityColour(cc));
        }
        c.getCardInfo().getEffect().resolveEffect(c);
        int i = 0;
        for(Colour cc: Colour.values()){
            //assertEquals(7, resCard);
            assertEquals(expectedDashboard.get(i), d1.getEntrance().getQuantityColour(cc));
            assertEquals(expectedCard.get(i), c.getStudentsOnCard().getQuantityColour(cc));
            i++;
        }

        Player p2 = new Player(Tower.WHITE);
        c.setPlayerThisTurn(p2);
        Dashboard d2 = c.getPlayerThisTurn().getDashboard();
        StudentGroup studentsOnCard2 = new StudentGroup(6);
        c.setStudentsOnCard(studentsOnCard2);
        StudentGroup studentsTo2 = new StudentGroup(10);
        StudentGroup sp2 = new StudentGroup(6);
        StudentGroup studentsFrom2 = new StudentGroup(10);
        d2.fillEntrance(sp2);
        c.setSelectedStudentsTo(studentsTo2);
        c.setSelectedStudentsFrom(studentsFrom2);
        //c.getCardInfo().getEffect().resolveEffect(c);
        //The function returns IllegalArgumentException

        /*Player p = new Player(Tower.BLACK);
        int[] sOnEntrance = {0,1,2,3,1};
        p.fillDashboardEntrance(new StudentGroup(sOnEntrance));
        int[] sOnCard = {0,1,1,1,3};
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD7);
        c.setStudentsOnCard(new StudentGroup(sOnCard));
        int[] sTo = {0,1,1,1,0};
        int[] sFrom = {0,0,0,0,3};
        c.setSelectedStudentsFrom(new StudentGroup(sFrom));
        c.setSelectedStudentsTo(new StudentGroup(sTo));
        c.setPlayerThisTurn(p);
        c.getCardInfo().getEffect().resolveEffect(c);
        assertEquals(0,c.getStudentsOnCard().getQuantityColour(Colour.YELLOW));
        assertEquals(2,c.getStudentsOnCard().getQuantityColour(Colour.GREEN));
        assertEquals(2,c.getStudentsOnCard().getQuantityColour(Colour.BLUE));
        assertEquals(2,c.getStudentsOnCard().getQuantityColour(Colour.PINK));
        assertEquals(0,c.getStudentsOnCard().getQuantityColour(Colour.RED));
        assertEquals(0,p.getDashboard().getEntrance().getQuantityColour(Colour.YELLOW));
        assertEquals(0,p.getDashboard().getEntrance().getQuantityColour(Colour.GREEN));
        assertEquals(1,p.getDashboard().getEntrance().getQuantityColour(Colour.BLUE));
        assertEquals(2,p.getDashboard().getEntrance().getQuantityColour(Colour.PINK));
        assertEquals(4,p.getDashboard().getEntrance().getQuantityColour(Colour.RED));*/
    }
}