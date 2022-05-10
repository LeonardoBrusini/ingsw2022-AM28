package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;

import java.util.ArrayList;

public class StudentToHallEffect implements EffectStrategy{
    /**
     * takes the selected student from the card and puts it on the hall, then extracts a random student from the bag and adds it on the card
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) throws IllegalArgumentException{
        //Dashboard d = c.getPlayerThisTurn().getDashboard();
        if(c.getStudentsOnCard().getQuantityColour(c.getSelectedColour()) < 1)
            throw new IllegalArgumentException();

        c.getStudentsOnCard().removeStudent(c.getSelectedColour());
        try {
            c.getPlayerThisTurn().addToHall(c.getSelectedColour());
            c.getGameManager().checkProfessors(c.getSelectedColour());
        } catch (FullHallException e) {
            e.printStackTrace();
        }
        //d.fillHall(new StudentGroup(list));
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
                ccs0.setCoinOnIt(true);
                ccs0.setStudents(c.getStudentsOnCard().getStatus());
                ccs.add(ccs0);
                break;
            }
        }
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        return cs;
    }
}
