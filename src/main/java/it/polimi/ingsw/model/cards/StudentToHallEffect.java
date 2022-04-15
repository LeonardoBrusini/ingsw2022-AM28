package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.players.Dashboard;

import java.util.ArrayList;

public class StudentToHallEffect implements EffectStrategy{
    /**
     * takes the selected student from the card and puts it on the hall, then extracts a random student from the bag and adds it on the card
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) throws IllegalArgumentException{
        ArrayList<Colour> list = new ArrayList<>(); //invoking addToHall with empty entrance it would had thrown IllegalArgumentExecption in Dashboard
        list.add(c.getSelectedColour());
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        if(c.getStudentsOnCard().getQuantityColour(c.getSelectedColour()) < 1)
            throw new IllegalArgumentException();
        c.getStudentsOnCard().removeStudent(c.getSelectedColour());
        d.fillHall(new StudentGroup(list));
        c.getStudentsOnCard().addStudent(c.getBoard().getBag().removeStudents(1).get(0));
    }
}
