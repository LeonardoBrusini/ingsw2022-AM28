package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.controller.ExpertGameManager;
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

    @Override
    public CurrentStatus getUpdatedStatus(CharacterCard c, ExpertGameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        gs.setArchipelagos(gameManager.getBoard().getIslandManager().getFullArchipelagosStatus());
        PlayerStatus[] ps = new PlayerStatus[gameManager.getPlayers().size()];
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            ps[i] = new PlayerStatus();
            ps[i].setIndex(i);
            ps[i].setCoins(gameManager.getPlayers().get(i).getCoins());
            ps[i].setStudentsOnHall(gameManager.getPlayers().get(i).getDashboard().getHall().getStatus());
        }
        gs.setPlayers(ps);
        CharacterCardStatus[] ccs = new CharacterCardStatus[1];
        ccs[0] = new CharacterCardStatus();
        for(int i=0;i<gameManager.getBoard().getCharacterCards().size();i++) {
            if(c==gameManager.getBoard().getCharacterCards().get(i)) {
                ccs[0].setIndex(i);
                ccs[0].setCoinOnIt(true);
                break;
            }
        }
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        return cs;
    }
}
