package it.polimi.ingsw.model.players;

import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.players.AssistantCard;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {
    private AssistantCard card;



    @Test
    void isPlayed(){
        this.card = new AssistantCard(AssistantCardInfo.CARD3);
        assertFalse(this.card.isPlayed());
    }

    @Test
    void setPlayed() {
        card = new AssistantCard(AssistantCardInfo.CARD1);
        assertFalse(card.isPlayed());
        card.setPlayed(true);
        assertTrue(card.isPlayed());
        card.setPlayed(false);
        assertFalse(card.isPlayed());
    }

    @Test
    void getInfo() {
        card = new AssistantCard(AssistantCardInfo.CARD1);
        AssistantCardInfo info = card.getInfo();
        assertEquals(AssistantCardInfo.CARD1,info);
    }

    @Test
    void setInfo() {
        card = new AssistantCard(AssistantCardInfo.CARD1);
        assertEquals(AssistantCardInfo.CARD1,card.getInfo());
        card.setInfo(AssistantCardInfo.CARD2);
        assertEquals(AssistantCardInfo.CARD2,card.getInfo());
    }
}