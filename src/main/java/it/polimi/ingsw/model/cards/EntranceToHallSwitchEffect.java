package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.players.Dashboard;

public class EntranceToHallSwitchEffect implements EffectStrategy{
    /**
     * takes the selected students (max 2) from the entrance (stored in selectedStudentsFrom) and moves them from the entrance to the hall
     * then takes the selected students (max 2) from the hall (stored in selectedStudentTo) and moves them from the hall to the entrance
     * @param c the card which is being activated
     */
    @Override
    //NOT COMPLETED
    public void resolveEffect(CharacterCard c) throws IllegalArgumentException {
        if(c.getSelectedStudentsFrom().getTotalStudents() > 2 || c.getSelectedStudentsTo().getTotalStudents() > 2)
            throw new IllegalArgumentException();
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            if(d.getEntrance().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            for(int i=0; i<quantityColour; i++) {
                d.removeFromEntrance(colour);
            }
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            if(d.getHall().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            for(int i=0; i<quantityColour; i++) {
                d.removeFromHall(colour);
            }
        }
        d.fillHall(c.getSelectedStudentsFrom());
        d.fillEntrance(c.getSelectedStudentsTo());
        c.getGameManager().checkInfluence();
        //ExpertGameManager.instance().checkProfessors();
    }
}
