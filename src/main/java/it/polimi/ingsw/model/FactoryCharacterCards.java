package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class FactoryCharacterCards {
    private static final String[] fileNames = {"P01.jpg","P02.jpg","P03.jpg","P04.jpg","P05.jpg","P06.jpg","P07.jpg","P08.jpg","P09.jpg","P10.jpg","P11.jpg","P12.jpg"};
    public static ArrayList<CharacterCard> getCards(){
        ArrayList<CharacterCard> cards = new ArrayList<>();
        String name;
        Random r = new Random();
        int i=0;
        boolean newCard;
        while(i<3) {
            newCard = true;
            name = fileNames[r.nextInt(12)];
            for(int j=0;j<cards.size();j++) {
                if(cards.get(j).getFileName().equals(name)) {
                    newCard = false;
                }
            }
            if(newCard) {
                cards.add(getCardFromName(name));
                i++;
            }
        }
        return cards;
    }

    private static CharacterCard getCardFromName(String name) {
        switch (name) {
            case "P01.jpg":
                return new CharacterCard(name,new CardEffect1(),1);
            case "P02.jpg":
            case "P08.jpg":
                return new CharacterCard(name,new DefaultCardEffect(),2);
            case "P03.jpg":
                return new CharacterCard(name,new CardEffect3(),3);
            case "P04.jpg":
                return new CharacterCard(name,new CardEffect4(),1);
            case "P05.jpg":
                return new CharacterCard(name,new CardEffect5(),2);
            case "P06.jpg":
            case "P09.jpg":
                return new CharacterCard(name,new DefaultCardEffect(),3);
            case "P07.jpg":
                return new CharacterCard(name,new CardEffect7(),1);
            case "P10.jpg":
                return new CharacterCard(name,new CardEffect10(),1);
            case "P11.jpg":
                return new CharacterCard(name,new CardEffect11(),2);
            case "P12.jpg":
                return new CharacterCard(name,new CardEffect12(),3);
            default: return null;
        }
    }
}
