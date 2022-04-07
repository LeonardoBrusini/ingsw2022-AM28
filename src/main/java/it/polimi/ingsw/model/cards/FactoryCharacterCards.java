package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Bag;

import java.util.ArrayList;
import java.util.Random;

public class FactoryCharacterCards {
    public static ArrayList<CharacterCard> getCards(Bag b){
        ArrayList<CharacterCard> cards = new ArrayList<>();
        CharacterCardsInfo[] cardsInfo = CharacterCardsInfo.values();
        CharacterCardsInfo cardInfo;
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
