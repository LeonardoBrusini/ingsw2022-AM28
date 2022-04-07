package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.players.AssistantCard;

public class CardEffect4 implements EffectStrategy{
    /**
     * adds 2 to the attribute motherNatureShifts of the last assistant card played by the player who activates this effect
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c, Bag bag, MotherNature mn, IslandManager manager) {
        AssistantCard as = c.getPlayerThisTurn().getLastPlayedCard();
        as.setMotherNatureShifts(as.getMotherNatureShifts()+2);
    }
}
