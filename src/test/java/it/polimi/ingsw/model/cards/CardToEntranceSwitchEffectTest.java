package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.CharacterCardInfo;
import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.players.Dashboard;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardToEntranceSwitchEffectTest {
    /**
     * It verifies the correct throw of Excpetions and the correct number of students
     * at the end of computation in each area
     */
    @Test
    void resolveEffect() {
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD7);
        Board b = new Board(2);
        c.initializeCards(b.getBag());
        Player p = new Player("g1", Tower.WHITE);
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
        Player p1 = new Player("g1", Tower.WHITE);
        c.setPlayerThisTurn(p1);
        Dashboard d1 = c.getPlayerThisTurn().getDashboard();
        StudentGroup studentOnCard1 = new StudentGroup(6);
        c.setStudentsOnCard(studentOnCard1);
        ArrayList<Colour> extactedColours1 = b.getBag().removeStudents(3);
        ArrayList<Colour> extactedColours2 = b.getBag().removeStudents(3);
        StudentGroup studentsTo1 = new StudentGroup(extactedColours1);
        StudentGroup sp1 = new StudentGroup(6);
        StudentGroup studentsFrom1 = new StudentGroup(extactedColours2);
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

        Player p2 = new Player("g1", Tower.WHITE);
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
    }

}