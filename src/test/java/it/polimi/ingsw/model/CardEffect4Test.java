package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardEffect4Test {

    @Test
    void resolveEffect(CharacterCard c) {
        AssistantCard as = c.getPlayerThisTurn().getLastPlayedCard();
        int w = as.getMotherNatureShifts();
        //resolveEffect(c);
        assertEquals(w + 2, as.getMotherNatureShifts());
    }
}