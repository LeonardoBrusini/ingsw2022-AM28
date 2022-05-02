package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.model.StudentGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RemoveFromHallEffectTest {

    /**
     * It verifies that each Hall has the correct number of students after the card's effect execution
     */
    @Test
    void resolveEffect() {
        ExpertGameManager gm = new ExpertGameManager();
        ArrayList<Integer> before = new ArrayList<>();
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD12);
        c.setGameManager(gm);
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        c.setSelectedColour(Colour.YELLOW);
        try {
            for (int i = 0; i < gm.getPlayers().size(); i++) {
                gm.getPlayers().get(i).getDashboard().fillHall(new StudentGroup(gm.getBoard().getBag().removeStudents(3)));
                before.add(gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
            }
        } catch (FullHallException e) {
            e.printStackTrace();
        }
        c.setGameManager(gm);
        c.getCardInfo().getEffect().resolveEffect(c);
        for(int i = 0; i < gm.getPlayers().size(); i++){
            // I can't figure out why sometimes the first sentence it is false
            if(before.get(i) != 0) {
                Assertions.assertNotEquals(before.get(i), gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
            }
            Assertions.assertNotEquals(-1,  gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
            assertFalse(before.get(i) < gm.getPlayers().get(i).getDashboard().getHall().getQuantityColour(c.getSelectedColour()));
        }
    }
}