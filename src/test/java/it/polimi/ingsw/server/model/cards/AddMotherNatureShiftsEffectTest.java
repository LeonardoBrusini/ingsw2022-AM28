package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.model.players.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddMotherNatureShiftsEffectTest {
    private final CharacterCard c = new CharacterCard(CharacterCardInfo.CARD4);
    /**
     * It tests the correct execution of the card's effect
     */
    @Test
    void resolveEffect() {
        Player p = new Player(Tower.BLACK);
        try {
            p.playCard(9);
        }catch (AlreadyPlayedException e){
            e.printStackTrace();
        }
        int shiftsBefore = p.getLastPlayedCard().getInfo().getMotherNatureShifts();
        c.setPlayerThisTurn(p);
        c.getCardInfo().getEffect().resolveEffect(c);
        assertEquals(shiftsBefore+2,p.getLastPlayedCard().getInfo().getMotherNatureShifts());
    }

}