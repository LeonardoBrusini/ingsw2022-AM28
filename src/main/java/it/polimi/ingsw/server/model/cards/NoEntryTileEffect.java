package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.board.Archipelago;

public class NoEntryTileEffect implements EffectStrategy{
    /**
     * puts one "no entry tile" on a selected archipelago
     * @param c the card which is being activated
     */

    @Override
    public void resolveEffect(CharacterCard c) {
        Archipelago a = c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()); //parametro in CharacterCard
        if(c.getNoEntryTiles()>0) {
            a.setNoEntryTiles(a.getNoEntryTiles()+1);
            c.setNoEntryTiles(c.getNoEntryTiles()-1);
        }
    }
}