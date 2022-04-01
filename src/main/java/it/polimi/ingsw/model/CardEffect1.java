package it.polimi.ingsw.model;

public class CardEffect1 implements EffectStrategy{
    @Override
    public void resolveEffect(CharacterCard c) {
        c.getStudentsOnCard().removeStudent(c.getSelectedColour());
        c.getSelectedIsland().addStudent(c.getSelectedColour());
        c.getStudentsOnCard().addStudent(Board.instance().getBag().removeStudents(1).get(0));
    }
}
