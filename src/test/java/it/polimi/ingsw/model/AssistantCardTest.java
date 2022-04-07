package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.players.AssistantCard;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {

    AssistantCard card;

    @Test
    void getMotherNatureShifts() {
        this.card = new AssistantCard(4,10,"Carta");
        assertEquals(4,this.card.getMotherNatureShifts());
    }

    @Test
    void getTurnWeight() {
        this.card = new AssistantCard(4,10,"Carta");
        assertEquals(10,this.card.getTurnWeight());
    }

    @Test
    void getFileName() {
        this.card = new AssistantCard(4,10,"Carta");
        assertEquals("Carta",this.card.getFileName());
    }

    @Test
    void getPlayed(){
        this.card = new AssistantCard(4,10,"Carta");
        assertEquals(false,this.card.getPlayed());
    }

    @Test
    void setPlayed() {
        this.card = new AssistantCard(4,10,"Carta");
        assertEquals(false,this.card.getPlayed());
        this.card.setPlayed();
        assertEquals(true,this.card.getPlayed());
        this.card.setPlayed();
        assertEquals(true,this.card.getPlayed());
    }

    @Test
    void setMotherNatureShifts() {
        this.card = new AssistantCard(4,10,"Carta");
        assertEquals(4,this.card.getMotherNatureShifts());
        this.card.setMotherNatureShifts(7);
        assertEquals(7,this.card.getMotherNatureShifts());

    }
}