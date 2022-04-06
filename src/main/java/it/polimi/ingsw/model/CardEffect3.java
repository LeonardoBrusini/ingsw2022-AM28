package it.polimi.ingsw.model;

public class CardEffect3 implements EffectStrategy{
    /**
     * Moves mother nature on the selected island, computes the influence on the island
     * then puts mother nature back where it was
     * @param c the card which is being activated
     */
    //NOT COMPLETED
    @Override
    public void resolveEffect(CharacterCard c) {
        MotherNature mn = Board.instance().getMotherNature(); //diventa parametro
        int motherNaturePosition = mn.getIslandIndex();
        mn.setIsland(c.getSelectedIsland().getIslandIndex());
        //mn.computeInfluence();
        mn.setIsland(motherNaturePosition);
    }
}
