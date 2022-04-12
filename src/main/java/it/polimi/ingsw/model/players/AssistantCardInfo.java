package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.cards.EffectStrategy;

public enum AssistantCardInfo {
    CARD1("A01.jpg",1,1),
    CARD2("A02.jpg",2,1),
    CARD3("A03.jpg",3,2),
    CARD4("A04.jpg",4,2),
    CARD5("A05.jpg",5,3),
    CARD6("A06.jpg",6,3),
    CARD7("A07.jpg",7,4),
    CARD8("A08.jpg",8,4),
    CARD9("A09.jpg",9,5),
    CARD10("A10.jpg",10,5);

    private final String fileName;
    private final int turnWeight;
    private int motherNatureShifts;

    AssistantCardInfo(String fileName, int turnWeight, int motherNatureShifts) {
        this.fileName = fileName;
        this.turnWeight = turnWeight;
        this.motherNatureShifts = motherNatureShifts;
    }

    public String getFileName() {
        return fileName;
    }

    public int getTurnWeight() {
        return turnWeight;
    }

    public int getMotherNatureShifts() {
        return motherNatureShifts;
    }

    public void setMotherNatureShifts(int motherNatureShifts) {
        this.motherNatureShifts = motherNatureShifts;
    }
}
