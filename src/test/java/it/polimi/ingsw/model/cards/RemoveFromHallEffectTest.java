package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.enumerations.CharacterCardInfo;
import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.StudentGroup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RemoveFromHallEffectTest {
    @Test
    /**
     * It verifies that each Hall has the correct number of students after the card's effect excetution
     */
    void resolveEffect() {
        // NOT COMPLETED
        ExpertGameManager gm = new ExpertGameManager();
        ArrayList<Integer> before = new ArrayList<>();
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD12);
        c.setGameManager(gm);
        gm.addPlayer("g1");
        gm.newGame();
        c.setSelectedColour(Colour.YELLOW);
        for(int i = 0; i < gm.getPlayers().size(); i++) {
            gm.getPlayers().get(i).getDashboard().fillHall(new StudentGroup(gm.getBoard().getBag().removeStudents(3)));
            before.add(gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
        }
        c.getCardInfo().getEffect().resolveEffect(c);
        for(int i = 0; i < gm.getPlayers().size(); i++){
            // I can't figure out why sometimes the first sentence it is false
            assertNotEquals(before.get(i),gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
            assertNotEquals(-1,  gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
            assertFalse(before.get(i) < gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
        }
    }
}