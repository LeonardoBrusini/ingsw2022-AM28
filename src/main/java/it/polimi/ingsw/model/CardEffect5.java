package it.polimi.ingsw.model;

public class CardEffect5 implements EffectStrategy{
    /**
     * puts one "no entry tile" on a selected archipelago
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c, Bag bag, MotherNature mn, IslandManager manager) {
        Archipelago a = manager.getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()); //parametro in CharacterCard
        if(c.getNoEntryTiles()>0) {
            a.setNoEntryTiles(a.getNoEntryTiles()+1);
            c.setNoEntryTiles(c.getNoEntryTiles()-1);
        }
    }
}
