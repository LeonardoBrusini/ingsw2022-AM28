package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class FactoryCharacterCards {
    private static final String[] fileNames = {"P01.jpg","P02.jpg","P03.jpg","P04.jpg","P05.jpg","P06.jpg","P07.jpg","P08.jpg","P09.jpg","P10.jpg","P11.jpg","P12.jpg"};
    /*public static ArrayList<CharacterCard> getCards(){
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
            }
        }
        return cards;
    }*/

    /*private static CharacterCard getCardFromName(String name) {
        switch (name) {
            case fileNames[0]:
                return new CharacterCard(name,new CardEffect1(),1);
            break;
            case fileNames[1]:
                return new CharacterCard(name,new DefaultCardEffect(),2);
            break;
            case fileNames[2]:
                return new CharacterCard(name,new CardEffect3(),3);
            break;
            case fileNames[3]:
                return new CharacterCard(name,new CardEffect4(),1);
            break;
            case fileNames[4]:
                return new CharacterCard(name,new CardEffect5(),2);
            break;
            case fileNames[5]:
                return new CharacterCard(name,new DefaultCardEffect(),3);
            break;
            case fileNames[6]:
                return new CharacterCard(name,new CardEffect7(),1);
            break;
            case fileNames[7]:
                return new CharacterCard(name,new DefaultCardEffect(),2);
            break;
            case fileNames[8]:
                return new CharacterCard(name,new DefaultCardEffect(),3);
            break;
            case fileNames[9]:
                return new CharacterCard(name,new CardEffect10(),1);
            break;
            case fileNames[10]:
                return new CharacterCard(name,new CardEffect11(),2);
            break;
            case fileNames[11]:
                return new CharacterCard(name,new CardEffect12(),3);
            break;
            default: return null;
        }
    }*/
}
