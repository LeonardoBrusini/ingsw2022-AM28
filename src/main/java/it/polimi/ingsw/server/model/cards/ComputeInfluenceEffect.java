package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.board.MotherNature;

public class ComputeInfluenceEffect implements EffectStrategy{
    /**
     * Moves mother nature on the selected island, computes the influence on the island
     * then puts mother nature back where it was
     * @param c the card which is being activated
     */

    //NOW IT SHOULD WORK BUT NOT IN A GOOD WAY
    @Override
    public void resolveEffect(CharacterCard c) {
        MotherNature mn = c.getBoard().getMotherNature();
        int islandbefore = mn.getIslandIndex();
        c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).setPresenceMotherNature(true);
        c.getBoard().getIslandManager().getArchipelagoByIslandIndex(islandbefore).setPresenceMotherNature(false);
        c.getBoard().getMotherNature().setIsland(c.getSelectedIsland().getIslandIndex());
        c.getGameManager().checkInfluence();
        c.getBoard().getMotherNature().setIsland(islandbefore);
        c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).setPresenceMotherNature(false);
        c.getBoard().getIslandManager().getArchipelagoByIslandIndex(islandbefore).setPresenceMotherNature(true);
    }
}
