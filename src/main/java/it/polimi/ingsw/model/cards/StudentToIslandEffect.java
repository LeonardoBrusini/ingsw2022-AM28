package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Bag;

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
        c.getStudentsOnCard().addStudent(c.getBoard().getBag().removeStudents(1).get(0)); //Board presa dalla carta
    }
}
