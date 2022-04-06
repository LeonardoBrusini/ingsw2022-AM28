package it.polimi.ingsw.model;

public class CardEffect11 implements EffectStrategy{
    /**
     * takes the selected student from the card and puts it on the hall, then extracts a random student from the bag and adds it on the card
     * @param c the card which is being activated
     */
    @Override
    public void resolveEffect(CharacterCard c, Bag bag, MotherNature mn, IslandManager manager) {
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        c.getStudentsOnCard().removeStudent(c.getSelectedColour());
        d.addToHall(c.getSelectedColour());
        c.getStudentsOnCard().addStudent(bag.removeStudents(1).get(0));
    }
}
