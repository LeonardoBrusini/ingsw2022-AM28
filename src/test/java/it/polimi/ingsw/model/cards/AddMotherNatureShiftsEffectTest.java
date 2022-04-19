package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.CharacterCardInfo;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddMotherNatureShiftsEffectTest {
    private final CharacterCard c = new CharacterCard(CharacterCardInfo.CARD4);
    /**
     * It tests the correct execution of the card's effect
     */
    @Test
    void resolveEffect() {
        Player p = new Player("p1", Tower.BLACK);
        p.playCard(9);
        int shiftsBefore = p.getLastPlayedCard().getInfo().getMotherNatureShifts();
        c.setPlayerThisTurn(p);
        c.getCardInfo().getEffect().resolveEffect(c);
        assertEquals(shiftsBefore+2,p.getLastPlayedCard().getInfo().getMotherNatureShifts());
    }

}