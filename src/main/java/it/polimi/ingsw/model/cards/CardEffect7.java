package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.players.Dashboard;

public class CardEffect7 implements EffectStrategy{
    /**
     * takes the selected students from the card (stored in selectedStudentsFrom) and moves them from the card to the dashboard entrance
     * then takes the selected students from the entrance (stored in selectedStudentTo) and moves them from the entrance to the card
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c, Bag bag, MotherNature mn, IslandManager manager) {
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            for(int i=0; i<quantityColour; i++) {
                c.getStudentsOnCard().removeStudent(colour);
            }
            d.getEntrance().addStudents(c.getSelectedStudentsFrom());
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            for(int i=0; i<quantityColour; i++) {
                d.getEntrance().removeStudent(colour);
            }
            c.getStudentsOnCard().addStudents(c.getSelectedStudentsTo());
        }
    }
}