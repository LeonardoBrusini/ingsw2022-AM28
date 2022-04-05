package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    private CharacterCard card;

    //BOARD BEFORE
    /*
    @Test
    void initializeCards() {
        card = new CharacterCard("P01.jpg",new CardEffect1(),1);
        card.initializeCards();
        assertEquals(CardEffect1.class, card.getEffect().getClass());
        assertEquals("P01.jpg",card.getFileName());
        assertEquals(1,card.getPrice());
        assertFalse(card.isCoinOnlt());
        assertFalse(card.isActivated());
        assertEquals(0,card.getNoEntryTiles());
        int students = 0;
        for(Colour c: Colour.values()) {
            students+=card.getStudentsOnCard().getQuantityColour(c);
        }
        assertEquals(4,students);
        assertNull(card.getPlayerThisTurn());
        assertNull(card.getSelectedColour());
        assertNull(card.getSelectedIsland());
        assertNull(card.getSelectedStudentsFrom());
        assertNull(card.getSelectedStudentsTo());
        card = new CharacterCard("P02.jpg",new DefaultCardEffect(),2);
        card.initializeCards();
        card = new CharacterCard("P03.jpg",new CardEffect3(),3);
        card.initializeCards();
        card = new CharacterCard("P04.jpg",new CardEffect4(),1);
        card.initializeCards();
        card = new CharacterCard("P05.jpg",new CardEffect5(),2);
        card.initializeCards();
        card = new CharacterCard("P06.jpg",new DefaultCardEffect(),3);
        card.initializeCards();
        card = new CharacterCard("P07.jpg",new CardEffect7(),1);
        card.initializeCards();
        card = new CharacterCard("P08.jpg",new DefaultCardEffect(),2);
        card.initializeCards();
        card = new CharacterCard("P09.jpg",new DefaultCardEffect(),3);
        card.initializeCards();
        card = new CharacterCard("P10.jpg",new CardEffect10(),1);
        card.initializeCards();
        card = new CharacterCard("P11.jpg",new CardEffect11(),2);
        card.initializeCards();
        card = new CharacterCard("P12.jpg",new CardEffect12(),3);
        card.initializeCards();
    }*/
}