package it.polimi.ingsw.model.cards;

public enum CharacterCardsInfo {
    CARD1("P01.jpg",1,new StudentToIslandEffect()),
    CARD2("P02.jpg",2,new DefaultCardEffect()),
    CARD3("P03.jpg",3,new ComputeInfluenceEffect()),
    CARD4("P04.jpg",1,new AddMotherNatureShiftsEffect()),
    CARD5("P05.jpg",2,new NoEntryTileEffect()),
    CARD6("P06.jpg",3,new DefaultCardEffect()),
    CARD7("P07.jpg",1,new CardToEntranceSwitchEffect()),
    CARD8("P08.jpg",2,new DefaultCardEffect()),
    CARD9("P09.jpg",3,new DefaultCardEffect()),
    CARD10("P10.jpg",1,new EntranceToHallSwitchEffect()),
    CARD11("P11.jpg",2,new StudentToHallEffect()),
    CARD12("P12.jpg",3,new RemoveFromHallEffect());

    private final String fileName;
    private final int price;
    private final EffectStrategy effect;

    CharacterCardsInfo(String fileName, int price, EffectStrategy effect) {
        this.fileName = fileName;
        this.price = price;
        this.effect = effect;
    }

    public String getFileName() {
        return fileName;
    }
    public int getPrice() {
        return price;
    }
    public EffectStrategy getEffect() {
        return effect;
    }
}
