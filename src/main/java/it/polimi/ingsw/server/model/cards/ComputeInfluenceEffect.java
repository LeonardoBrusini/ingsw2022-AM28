package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.MotherNature;

import java.util.ArrayList;

public class ComputeInfluenceEffect implements EffectStrategy{
    /**
     * Moves mother nature on the selected island, computes the influence on the island
     * then puts mother nature back where it was
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) {
        MotherNature mn = c.getBoard().getMotherNature();
        int islandBefore = mn.getIslandIndex();
        c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).setPresenceMotherNature(true);
        c.getBoard().getIslandManager().getArchipelagoByIslandIndex(islandBefore).setPresenceMotherNature(false);
        c.getBoard().getMotherNature().setIsland(c.getSelectedIsland().getIslandIndex());
        c.getGameManager().checkInfluence();
        c.getBoard().getMotherNature().setIsland(islandBefore);
        for(Archipelago a: c.getGameManager().getBoard().getIslandManager().getArchipelagos()) {
            a.setPresenceMotherNature(false);
        }
        //c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).setPresenceMotherNature(false);
        c.getBoard().getIslandManager().getArchipelagoByIslandIndex(islandBefore).setPresenceMotherNature(true);
    }

    @Override
    public CurrentStatus getUpdatedStatus(CharacterCard c, GameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        gs.setArchipelagos(gameManager.getBoard().getIslandManager().getFullArchipelagosStatus());
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            PlayerStatus psTemp = new PlayerStatus();
            psTemp.setIndex(i);
            psTemp.setCoins(gameManager.getPlayers().get(i).getCoins());
            psTemp.setNumTowers(gameManager.getPlayers().get(i).getDashboard().getNumTowers());
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
