package it.polimi.ingsw.server.model.cards;

/**
 * the interface for the strategy pattern used for the cards effect
 */

public interface EffectStrategy {
    void resolveEffect(CharacterCard c);
}
