package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.server.controller.GameManager;

/**
 * the interface for the strategy pattern used for the cards effect
 */

public interface EffectStrategy {
    void resolveEffect(CharacterCard c);
    CurrentStatus getUpdatedStatus(CharacterCard c, GameManager gameManager);
}
