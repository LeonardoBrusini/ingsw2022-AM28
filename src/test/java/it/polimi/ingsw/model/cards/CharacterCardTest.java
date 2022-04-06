package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.CardEffect1;
import it.polimi.ingsw.model.cards.CharacterCard;
import org.junit.jupiter.api.Test;

class CharacterCardTest {
    CharacterCard c = new CharacterCard(new String(), new CardEffect1(), 1);

    @Test
    void initializeCards() {
        //initializeCards(int mn);
       /* switch(c.getFileName()) {
            case "P01.jpg":
            case "P11.jpg":
                int counter = 0;
                for(Colour cc: Colour.values()){
                    counter += c.getStudentsOnCard().getQuantityColour(cc);
                }
                assertEquals(4, counter);
            case "P05.jpg":
                assertEquals(4, c.getNoEntryTiles());
            case "P07.jpg":
                int count = 0;
                for(Colour cc: Colour.values()){
                    count += c.getStudentsOnCard().getQuantityColour(cc);
                }
                assertEquals(6, count);
        }*/
    }

}
