package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.model.board.Archipelago;

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
    public CurrentStatus getUpdatedStatus(CharacterCard c, ExpertGameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        PlayerStatus[] ps = new PlayerStatus[1];
        ps[0] = new PlayerStatus();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            if(c.getPlayerThisTurn()==gameManager.getPlayers().get(i)) {
                ps[0].setIndex(i);
                ps[0].setCoins(gameManager.getPlayers().get(i).getCoins());
                break;
            }
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
        Archipelago a = c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex());
        ArchipelagoStatus[] as = new ArchipelagoStatus[1];
        as[0].setIndex(c.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(c.getSelectedIsland().getIslandIndex()));
        as[0].setNoEntryTiles(a.getNoEntryTiles());
        gs.setArchipelagos(as);
        cs.setGame(gs);
        return cs;
    }
}
