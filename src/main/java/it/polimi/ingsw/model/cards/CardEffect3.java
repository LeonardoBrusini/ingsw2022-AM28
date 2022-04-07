package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.board.MotherNature;

public class CardEffect3 implements EffectStrategy{
    /**
     * Moves mother nature on the selected island, computes the influence on the island
     * then puts mother nature back where it was
     * @param c the card which is being activated
     */

    //NOW IT SHOULD WORK BUT NOT IN A GOOD WAY
    @Override
    public void resolveEffect(CharacterCard c) {
        MotherNature mn = c.getBoard().getMotherNature(); //diventa parametro
        int motherNaturePosition = mn.getIslandIndex();
        mn.setIsland(c.getSelectedIsland().getIslandIndex());
        c.getGameManager().checkInfluence(); //diventa parametro
        mn.setIsland(motherNaturePosition);
    }
}
