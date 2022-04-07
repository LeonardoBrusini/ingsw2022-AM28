package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.board.MotherNature;

public class DefaultCardEffect implements EffectStrategy{
    @Override
    public void resolveEffect(CharacterCard c) {
        c.setActivated(true);
    }
}
