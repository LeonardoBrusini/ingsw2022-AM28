package it.polimi.ingsw.model.cards;

/**
 * the interface for the strategy pattern used for the cards effect
 */
public interface EffectStrategy {
    void resolveEffect(CharacterCard c, Bag bag, MotherNature mn, IslandManager manager);
}
