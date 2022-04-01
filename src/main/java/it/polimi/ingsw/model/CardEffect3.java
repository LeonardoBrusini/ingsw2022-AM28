package it.polimi.ingsw.model;

public class CardEffect3 implements EffectStrategy{
    //NOT COMPLETED
    @Override
    public void resolveEffect(CharacterCard c) {
        MotherNature mn = Board.instance().getMotherNature();
        int motherNaturePosition = mn.getIslandIndex();
        mn.setIsland(c.getSelectedIsland().getIslandIndex());
        //mn.computeInfluence();
        mn.setIsland(motherNaturePosition);
    }
}
