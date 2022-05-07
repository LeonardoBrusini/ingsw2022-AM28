package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.MotherNature;

public class ComputeInfluenceEffect implements EffectStrategy{
    /**
     * Moves mother nature on the selected island, computes the influence on the island
     * then puts mother nature back where it was
     * @param c the card which is being activated
     */
    //NOW IT SHOULD WORK BUT NOT IN A GOOD WAY
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
    public CurrentStatus getUpdatedStatus(CharacterCard c, ExpertGameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        gs.setArchipelagos(gameManager.getBoard().getIslandManager().getFullArchipelagosStatus());
        PlayerStatus[] ps = new PlayerStatus[gameManager.getPlayers().size()];
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            ps[i] = new PlayerStatus();
            ps[i].setIndex(i);
            ps[i].setCoins(gameManager.getPlayers().get(i).getCoins());
            ps[i].setNumTowers(gameManager.getPlayers().get(i).getDashboard().getNumTowers());
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
