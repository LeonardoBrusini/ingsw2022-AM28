package it.polimi.ingsw.server.model.cards;

public class DefaultCardEffect implements EffectStrategy{
    @Override
    public void resolveEffect(CharacterCard c) {
        c.setActivated(true);
    }
}