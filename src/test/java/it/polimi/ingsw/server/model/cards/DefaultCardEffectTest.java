package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCardEffectTest {
    private final CharacterCard c = new CharacterCard(CharacterCardInfo.CARD2);

    /**
     * It tests the correct execution of the card's effect
     */
    @Test
    void resolveEffect() {
        c.getCardInfo().getEffect().resolveEffect(c);
        assertTrue(c.isActivated());
    }
}