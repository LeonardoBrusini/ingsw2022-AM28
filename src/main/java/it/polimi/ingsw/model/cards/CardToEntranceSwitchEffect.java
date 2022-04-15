package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.players.Dashboard;

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
        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            if(c.getStudentsOnCard().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            for(int i=0; i<quantityColour; i++) {
                c.getStudentsOnCard().removeStudent(colour);
            }
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            if(d.getEntrance().getQuantityColour(colour) < quantityColour)
                throw new IllegalArgumentException();
            for(int i=0; i<quantityColour; i++) {
                d.removeFromEntrance(colour);
            }
        }
        d.fillEntrance(c.getSelectedStudentsFrom());
        c.getStudentsOnCard().addStudents(c.getSelectedStudentsTo());
    }
}
