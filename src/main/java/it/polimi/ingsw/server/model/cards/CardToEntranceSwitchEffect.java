package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.model.players.Dashboard;

public class CardToEntranceSwitchEffect implements EffectStrategy{
    /**
     * takes the selected students (max 3) from the card (stored in selectedStudentsFrom) and moves them from the card to the dashboard entrance
     * then takes the selected students (max 3) from the entrance (stored in selectedStudentTo) and moves them from the entrance to the card
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) throws IllegalArgumentException {
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        if(c.getSelectedStudentsTo().getTotalStudents() > 3 || c.getSelectedStudentsFrom().getTotalStudents() > 3)
            throw new IllegalArgumentException();
        if(c.getSelectedStudentsTo().getTotalStudents()!=c.getSelectedStudentsFrom().getTotalStudents()) throw new IllegalArgumentException();
        for (Colour colour: Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            if(c.getStudentsOnCard().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            if(d.getEntrance().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
        }

        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            for(int i=0; i<quantityColour; i++) {
                c.getStudentsOnCard().removeStudent(colour);
            }
            for(int i=0; i<quantityColour; i++) {
                try {
                    d.removeFromEntrance(colour);
                } catch (NoStudentsException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        d.fillEntrance(c.getSelectedStudentsFrom());
        c.getStudentsOnCard().addStudents(c.getSelectedStudentsTo());
    }

    @Override
    public CurrentStatus getUpdatedStatus(CharacterCard c, ExpertGameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        PlayerStatus[] ps = new PlayerStatus[1];
        ps[0] = new PlayerStatus();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            if(c.getPlayerThisTurn()==gameManager.getPlayers().get(i)) {
                ps[0].setIndex(1);
                ps[0].setCoins(gameManager.getPlayers().get(i).getCoins());
                ps[0].setStudentsOnEntrance(gameManager.getPlayers().get(i).getDashboard().getEntrance().getStatus());
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
        cs.setGame(gs);
        return cs;
    }
}
