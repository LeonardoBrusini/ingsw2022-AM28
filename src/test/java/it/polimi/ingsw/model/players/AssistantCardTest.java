package it.polimi.ingsw.model.players;

import it.polimi.ingsw.enumerations.AssistantCardInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {
    private AssistantCard card;


    /**
     * The tests checks if is returned the right value about the Played Card
     */
    @Test
    void isPlayed(){
        this.card = new AssistantCard(AssistantCardInfo.CARD3);
        assertFalse(this.card.isPlayed());
    }

    /**
     * It tests the methods sets the card as Played
     */
    @Test
    void setPlayed() {
        card = new AssistantCard(AssistantCardInfo.CARD1);
        assertFalse(card.isPlayed());
        card.setPlayed(true);
        assertTrue(card.isPlayed());
        card.setPlayed(false);
        assertFalse(card.isPlayed());
    }

    /**
     * It checks if the card's metadata are returned properly
     */
    @Test
    void getInfo() {
        card = new AssistantCard(AssistantCardInfo.CARD1);
        AssistantCardInfo info = card.getInfo();
        assertEquals(AssistantCardInfo.CARD1,info);
    }

    /**
     * It verifies if the card's metadata are set properly
     */
    @Test
    void setInfo() {
        card = new AssistantCard(AssistantCardInfo.CARD1);
        assertEquals(AssistantCardInfo.CARD1,card.getInfo());
        card.setInfo(AssistantCardInfo.CARD2);
        assertEquals(AssistantCardInfo.CARD2,card.getInfo());
    }
}