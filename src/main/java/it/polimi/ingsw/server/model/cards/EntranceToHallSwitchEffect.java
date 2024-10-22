package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.model.players.Dashboard;

import java.util.ArrayList;

public class EntranceToHallSwitchEffect implements EffectStrategy{
    /**
     * takes the selected students (max 2) from the entrance (stored in selectedStudentsFrom) and moves them from the entrance to the hall
     * then takes the selected students (max 2) from the hall (stored in selectedStudentTo) and moves them from the hall to the entrance
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) throws IllegalArgumentException {
        if(c.getSelectedStudentsFrom().getTotalStudents() > 2 || c.getSelectedStudentsTo().getTotalStudents() > 2 || c.getSelectedStudentsTo().getTotalStudents()<=0  || c.getSelectedStudentsFrom().getTotalStudents()<=0)
            throw new IllegalArgumentException();
        if(c.getSelectedStudentsFrom().getTotalStudents()!=c.getSelectedStudentsTo().getTotalStudents()) throw new IllegalArgumentException();
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            if(d.getEntrance().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            if(d.getHall().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
        }

        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            for(int i=0; i<quantityColour; i++) {
                try {
                    d.removeFromEntrance(colour);
                } catch (NoStudentsException e) {
                    throw new RuntimeException(e);
                }
            }
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            for(int i=0; i<quantityColour; i++) {
                d.removeFromHall(colour);
            }
        }

        try {
            c.getPlayerThisTurn().fillHall(c.getBoard(),c.getSelectedStudentsFrom());
        } catch (FullHallException e) {
            e.printStackTrace();
        }
        //d.fillHall(c.getSelectedStudentsFrom());
        d.fillEntrance(c.getSelectedStudentsTo());
        for (Colour col: Colour.values()) {
            if(c.getSelectedStudentsFrom().getQuantityColour(col)>0) {
                c.getGameManager().checkProfessors(col);
            }
        }
    }

    @Override
    public CurrentStatus getUpdatedStatus(CharacterCard c, GameManager gameManager) {
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        for(int i=0;i<gameManager.getPlayers().size();i++) {
            if(c.getPlayerThisTurn()==gameManager.getPlayers().get(i)) {
                ps0.setCoins(gameManager.getPlayers().get(i).getCoins());
                ps0.setIndex(i);
                ps0.setStudentsOnEntrance(gameManager.getPlayers().get(i).getDashboard().getEntrance().getStatus());
                ps0.setStudentsOnHall(gameManager.getPlayers().get(i).getDashboard().getHall().getStatus());
                ps.add(ps0);
                break;
            }
        }
        gs.setPlayers(ps);
        gs.setProfessors(gameManager.getBoard().getProfessorGroup().getStatus());
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
