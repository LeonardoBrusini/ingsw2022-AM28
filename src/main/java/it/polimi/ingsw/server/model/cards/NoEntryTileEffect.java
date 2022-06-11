package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.board.Archipelago;

import java.util.ArrayList;

public class NoEntryTileEffect implements EffectStrategy{
    /**
     * puts one "no entry tile" on a selected archipelago
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) {
        Archipelago a = c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex());
        if(c.getNoEntryTiles()<=0) throw new IllegalArgumentException();

        a.setNoEntryTiles(a.getNoEntryTiles()+1);
        c.setNoEntryTiles(c.getNoEntryTiles()-1);
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
        Archipelago a = c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex());
        ArrayList<ArchipelagoStatus> as = new ArrayList<>();
        ArchipelagoStatus as0 = new ArchipelagoStatus();
        as0.setIndex(c.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(c.getSelectedIsland().getIslandIndex()));
        as0.setNoEntryTiles(a.getNoEntryTiles());
        as.add(as0);
        gs.setArchipelagos(as);
        cs.setGame(gs);
        return cs;
    }
}
