package it.polimi.ingsw.model;

public class CharacterCard {
    private String fileName;
    private int price;
    private boolean coinOnlt;
    private StudentGroup selectedStudentsFrom;
    private StudentGroup selectedStudentsTo;
    private Island selectedIsland;
    private boolean isActivated;
    private Colour selectedColour;
    private Player palterThisTurn;
    private EffectStrategy effect;

    public void CharactedCard(String s,EffectStrategy e){
        this.effect = e;
        this.fileName = s;
    }


}
