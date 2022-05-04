package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.model.players.Dashboard;

public class EntranceToHallSwitchEffect implements EffectStrategy{
    /**
     * takes the selected students (max 2) from the entrance (stored in selectedStudentsFrom) and moves them from the entrance to the hall
     * then takes the selected students (max 2) from the hall (stored in selectedStudentTo) and moves them from the hall to the entrance
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) throws IllegalArgumentException {
        if(c.getSelectedStudentsFrom().getTotalStudents() > 2 || c.getSelectedStudentsTo().getTotalStudents() > 2)
            throw new IllegalArgumentException();

        Dashboard d = c.getPlayerThisTurn().getDashboard();
        //This exception can break the game; can be that some students get removed from the card or entrance before throwing the exception
        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            if(d.getEntrance().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            for(int i=0; i<quantityColour; i++) {
                try {
                    d.removeFromEntrance(colour);
                } catch (NoStudentsException e) {
                    throw new RuntimeException(e);
                }
            }
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            if(d.getHall().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            for(int i=0; i<quantityColour; i++) {
                d.removeFromHall(colour);
            }
        }

        try {
            c.getPlayerThisTurn().fillHall(c.getSelectedStudentsFrom());
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
    public CurrentStatus getUpdatedStatus(CharacterCard c) {
        return null;
    }
}
