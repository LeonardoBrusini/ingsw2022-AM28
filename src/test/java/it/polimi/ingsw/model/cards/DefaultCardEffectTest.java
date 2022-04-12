package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCardEffectTest {
    private CharacterCard c = new CharacterCard(CharacterCardsInfo.CARD2);

    /**
     * It tests the correct execution of the card's effect
     */
    @Test
    void resolveEffect() {
        c.getCardInfo().getEffect().resolveEffect(c);
        assertTrue(c.isActivated());
    }
}