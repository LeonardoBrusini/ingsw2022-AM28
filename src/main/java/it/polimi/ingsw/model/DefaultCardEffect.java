package it.polimi.ingsw.model;

public class DefaultCardEffect implements EffectStrategy{
    @Override
    public void resolveEffect(CharacterCard c, Bag bag, MotherNature mn, IslandManager manager) {
        c.setActivated(true);
    }
}
