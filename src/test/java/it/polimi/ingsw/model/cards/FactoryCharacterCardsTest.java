package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.board.Bag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FactoryCharacterCardsTest {

    /**
     * It tests if the method extracts and builds the CharacterCards according to the game's rules
     */
    @Test
    void getCards() {
        Bag b = new Bag();
        b.setStudents(new StudentGroup(24));
        ArrayList<CharacterCard> cards = FactoryCharacterCards.getCards(b);

        assertEquals(3,cards.size());
        assertNotEquals(cards.get(0),cards.get(1));
        assertNotEquals(cards.get(1),cards.get(2));
        assertNotEquals(cards.get(2),cards.get(0));
    }
}