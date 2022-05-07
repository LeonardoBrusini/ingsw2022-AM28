package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;

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
        PlayerStatus[] ps = new PlayerStatus[1];
        ps[0] = new PlayerStatus();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            if(c.getPlayerThisTurn()==gameManager.getPlayers().get(i)) {
                ps[0].setCoins(gameManager.getPlayers().get(i).getCoins());
                ps[0].setIndex(1);
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
                ccs[0].setStudents(c.getStudentsOnCard().getStatus());
                break;
            }
        }
        gs.setCharacterCards(ccs);
        ArchipelagoStatus[] as = new ArchipelagoStatus[1];
        as[0] = new ArchipelagoStatus();
        as[0].setIndex(gameManager.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(c.getSelectedIsland().getIslandIndex()));
        IslandStatus[] is = new IslandStatus[1];
        is[0] = new IslandStatus();
        is[0].setIslandIndex(c.getSelectedIsland().getIslandIndex());
        is[0].setStudents(c.getSelectedIsland().getStudents().getStatus());
        as[0].setIslands(is);
        cs.setGame(gs);
        return cs;
    }
}
