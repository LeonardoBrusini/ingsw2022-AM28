package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.controller.GameManager;

import java.util.ArrayList;

public class DefaultCardEffect implements EffectStrategy{
    @Override
    public void resolveEffect(CharacterCard c) {
        c.setActivated(true);
    }

    @Override
    public CurrentStatus getUpdatedStatus(CharacterCard c, GameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            if(c.getPlayerThisTurn()==gameManager.getPlayers().get(i)) {
                ps0.setIndex(i);
                ps0.setCoins(gameManager.getPlayers().get(i).getCoins());
                ps.add(ps0);
                break;
            }
        }
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        for(int i=0;i<gameManager.getBoard().getCharacterCards().size();i++) {
            if(c==gameManager.getBoard().getCharacterCards().get(i)) {
                ccs0.setIndex(i);
                ccs0.setCoinOnIt(c.isCoinOnIt());
                ccs.add(ccs0);
                break;
            }
        }
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        return cs;
    }
}
