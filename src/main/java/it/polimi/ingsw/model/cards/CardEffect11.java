package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.players.Dashboard;

public class CardEffect11 implements EffectStrategy{
    /**
     * takes the selected student from the card and puts it on the hall, then extracts a random student from the bag and adds it on the card
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c) {
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        c.getStudentsOnCard().removeStudent(c.getSelectedColour());
        d.addToHall(c.getSelectedColour());
        c.getStudentsOnCard().addStudent(c.getBoard().getBag().removeStudents(1).get(0));
    }
}
