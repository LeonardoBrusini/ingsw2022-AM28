package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.model.board.Bag;

import java.util.ArrayList;
import java.util.Random;

public class FactoryCharacterCards {
    /**
     * The method constructs Random CharacterCards
     * @param b referiment to the Bag used in the computation
     * @return build CharacterCards
     */
    public static ArrayList<CharacterCard> getCards(Bag b){
        ArrayList<CharacterCard> cards = new ArrayList<>();
        CharacterCardInfo[] cardsInfo = CharacterCardInfo.values();
        CharacterCardInfo cardInfo;
        Random r = new Random();
        int i=0;
        boolean newCard;

        while(i<3) {
            newCard = true;
            cardInfo = cardsInfo[r.nextInt(cardsInfo.length)];
            for (CharacterCard card : cards) {
                if (card.getCardInfo().getFileName().equals(cardInfo.getFileName())) {
                    newCard = false;
                    break;
                }
            }

            if(newCard) {
                cards.add(new CharacterCard(cardInfo));
                cards.get(i).initializeCards(b);
                i++;
            }
        }

        return cards;
    }
}
