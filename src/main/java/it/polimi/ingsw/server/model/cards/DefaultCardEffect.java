package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CurrentStatus;

public class DefaultCardEffect implements EffectStrategy{
    @Override
    public void resolveEffect(CharacterCard c) {
        c.setActivated(true);
    }

    @Override
    public CurrentStatus getUpdatedStatus(CharacterCard c) {
        return new CurrentStatus();
    }
}
