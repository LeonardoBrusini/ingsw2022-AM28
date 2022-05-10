package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;

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
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            PlayerStatus psTemp = new PlayerStatus();
            psTemp.setIndex(i);
            psTemp.setCoins(gameManager.getPlayers().get(i).getCoins());
            psTemp.setStudentsOnHall(gameManager.getPlayers().get(i).getDashboard().getHall().getStatus());
            ps.add(psTemp);
        }
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        for(int i=0;i<gameManager.getBoard().getCharacterCards().size();i++) {
            if(c==gameManager.getBoard().getCharacterCards().get(i)) {
                ccs0.setIndex(i);
                ccs0.setCoinOnIt(true);
                ccs.add(ccs0);
                break;
            }
        }
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        return cs;
    }
}
