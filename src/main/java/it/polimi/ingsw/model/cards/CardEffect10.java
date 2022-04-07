package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.board.MotherNature;
import it.polimi.ingsw.model.players.Dashboard;

public class CardEffect10 implements EffectStrategy{
    /**
     * takes the selected students from the entrance (stored in selectedStudentsFrom) and moves them from the entrance to the hall
     * then takes the selected students from the hall (stored in selectedStudentTo) and moves them from the hall to the entrance
     * @param c the card which is being activated
     */

    @Override
    //NOT COMPLETED
    public void resolveEffect(CharacterCard c) {
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        for(Colour colour : Colour.values()) {
            int quantityColour = c.getSelectedStudentsFrom().getQuantityColour(colour);
            for(int i=0; i<quantityColour; i++) {
                d.getEntrance().removeStudent(colour);
            }
            d.getHall().addStudents(c.getSelectedStudentsFrom());
            quantityColour = c.getSelectedStudentsTo().getQuantityColour(colour);
            for(int i=0; i<quantityColour; i++) {
                d.getHall().removeStudent(colour);
            }
            d.getEntrance().addStudents(c.getSelectedStudentsTo());
            //ExpertGameManager.instance().checkProfessors();
        }
    }
}
