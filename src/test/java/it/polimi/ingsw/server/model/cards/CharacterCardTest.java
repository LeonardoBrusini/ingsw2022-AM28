package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Bag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    ArrayList<CharacterCard> cards;

    /**
     * To do before each test
     */
    @BeforeEach
    void constructor() {
        cards=new ArrayList<>();
        for (CharacterCardInfo info: CharacterCardInfo.values()) {
            cards.add(new CharacterCard(info));
        }
    }

    /**
     * It tests if the cards are initialized properly
     */
    @Test
    void initializeCards() {
        Bag b = new Bag();
        b.setStudents(new StudentGroup(24));
        for(CharacterCard c: cards) {
            c.initializeCards(b);
            switch (c.getCardInfo()) {
                case CARD1, CARD11 -> {
                    int counter = 0;
                    for (Colour cc : Colour.values()) {
                        counter += c.getStudentsOnCard().getQuantityColour(cc);
                    }
                    assertEquals(4, counter);
                }
                case CARD5 -> assertEquals(4, c.getNoEntryTiles());
                case CARD7 -> {
                    int count = 0;
                    for (Colour cc : Colour.values()) {
                        count += c.getStudentsOnCard().getQuantityColour(cc);
                    }
                    assertEquals(6, count);
                }
            }
        }
    }

    /**
     * It tests if is returned the real card's price
     */
    @Test
    void getPrice() {
        for (CharacterCard c: cards) {
            assertFalse(c.isCoinOnIt());
            assertEquals(c.getCardInfo().getPrice(),c.getPrice());
            c.setCoinOnIt(true);
            assertTrue(c.isCoinOnIt());
            assertEquals(c.getCardInfo().getPrice()+1,c.getPrice());
            assertTrue(c.isCoinOnIt());
        }
    }

}
