package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;

import java.util.ArrayList;

public class StudentToIslandEffect implements EffectStrategy{
    /**
     * effect of the card 1: takes the selected student from the card and puts it on the selected island
     * then gets a student from the bag and puts it on the card
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) {
        c.getStudentsOnCard().removeStudent(c.getSelectedColour());
        c.getSelectedIsland().addStudent(c.getSelectedColour());
        c.getStudentsOnCard().addStudent(c.getBoard().getBag().removeStudents(1).get(0));
    }

    @Override
    public CurrentStatus getUpdatedStatus(CharacterCard c, ExpertGameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            if(c.getPlayerThisTurn()==gameManager.getPlayers().get(i)) {
                ps0.setCoins(gameManager.getPlayers().get(i).getCoins());
                ps0.setIndex(i);
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
                ccs0.setCoinOnIt(true);
                ccs0.setStudents(c.getStudentsOnCard().getStatus());
                ccs.add(ccs0);
                break;
            }
        }
        gs.setCharacterCards(ccs);
        ArrayList<ArchipelagoStatus> as = new ArrayList<>();
        ArchipelagoStatus as0 = new ArchipelagoStatus();
        as0.setIndex(gameManager.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(c.getSelectedIsland().getIslandIndex()));
        ArrayList<IslandStatus> is = new ArrayList<>();
        IslandStatus is0 = new IslandStatus();
        is0.setIslandIndex(c.getSelectedIsland().getIslandIndex());
        is0.setStudents(c.getSelectedIsland().getStudents().getStatus());
        is.add(is0);
        as0.setIslands(is);
        as.add(as0);
        gs.setArchipelagos(as);
        cs.setGame(gs);
        return cs;
    }
}
