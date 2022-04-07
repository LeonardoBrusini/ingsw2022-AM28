package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.board.MotherNature;

/**
 * the interface for the strategy pattern used for the cards effect
 */

public interface EffectStrategy {
    void resolveEffect(CharacterCard c);
}
