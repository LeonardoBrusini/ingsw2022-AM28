package it.polimi.ingsw.server.enumerations;

public enum AssistantCardInfo {
    CARD1("A01.png",1,1),
    CARD2("A02.png",2,1),
    CARD3("A03.png",3,2),
    CARD4("A04.png",4,2),
    CARD5("A05.png",5,3),
    CARD6("A06.png",6,3),
    CARD7("A07.png",7,4),
    CARD8("A08.png",8,4),
    CARD9("A09.png",9,5),
    CARD10("A10.png",10,5);

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
