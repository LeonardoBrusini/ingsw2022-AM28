package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Stores the 5 professors (one of each colour), and binds the professor to the player who owns it (to the board otherwise)
 */
public class ProfessorGroup {
    private EnumMap<Colour, Tower> professors;

    /**
     * Constructor of a new object of class ProfessorGroup
     */
    public ProfessorGroup(){
        professors = new EnumMap<>(Colour.class);
    }

    /**
     *
     * @param colour colour of the professor
     * @return the tower of the player who currently owns the professor, null if no player owns it
     */
    public Tower getTower(Colour colour){
        return professors.get(colour);
    }

    /**
     * A professor gets assigned to a player
     * @param colour the colour of the professor we want to assign
     * @param tower the tower of the player who gets the professor
     */
    public void setTower(Colour colour,Tower tower){
        professors.put(colour,tower);
    }

    /**
     * It returns the list of Professor's colour associated with the Tower
     * @param t the tower of the player we want to get the professors
     * @return a list with all the professors assigned to the player
     */
    public ArrayList<Colour> getColours(Tower t){
        ArrayList<Colour> ret = new ArrayList<>();
        for(Colour c : Colour.values())
            if(t.equals(professors.get(c)))
                ret.add(c);
        return ret;
    }

    //setter & getter methods, for testing
    public EnumMap<Colour, Tower> getProfessors() {
        return professors;
    }
    public void setProfessors(EnumMap<Colour, Tower> professors) {
        this.professors = professors;
    }

    public int[] getStatus() {
        int[] prof = new int[Colour.values().length];
        for (Colour c: Colour.values()) {
            if(professors.get(c) != null)
                prof[c.ordinal()] = professors.get(c).ordinal();
            else prof[c.ordinal()] = -1; //it avoids the NullPointerException
        }
        return prof;
    }
}
