package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.players.Player;

public class RemoveFromHallEffect implements EffectStrategy{
    /**
     * removes up to 3 students of the selected colour from every dashboards' hall
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) {
        for(Player p : c.getGameManager().getPlayers()){
            for (int i=0; i<3 && p.getDashboard().getHall().getQuantityColour(c.getSelectedColour())>0 ;i++) {
                p.getDashboard().removeFromHall(c.getSelectedColour());
            }
        }
    }
}