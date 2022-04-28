package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.players.AssistantCard;

public class AddMotherNatureShiftsEffect implements EffectStrategy{
    /**
     * adds 2 to the attribute motherNatureShifts of the last assistant card played by the player who activates this effect
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) {
        AssistantCard as = c.getPlayerThisTurn().getLastPlayedCard();
        as.getInfo().setMotherNatureShifts(as.getInfo().getMotherNatureShifts()+2);
    }
}
