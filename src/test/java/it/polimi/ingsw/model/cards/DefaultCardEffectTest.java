package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.CharacterCardInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCardEffectTest {
    private CharacterCard c = new CharacterCard(CharacterCardInfo.CARD2);

    /**
     * It tests the correct execution of the card's effect
     */
    @Test
    void resolveEffect() {
        c.getCardInfo().getEffect().resolveEffect(c);
        assertTrue(c.isActivated());
    }
}