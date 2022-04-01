package it.polimi.ingsw.model;

public class DefaultCardEffect implements EffectStrategy{
    @Override
    public void resolveEffect(CharacterCard c) {
        c.setActivated(true);
    }
}
