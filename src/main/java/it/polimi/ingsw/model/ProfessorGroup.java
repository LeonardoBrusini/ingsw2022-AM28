package it.polimi.ingsw.model;

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
        professors = new EnumMap<Colour,Tower>(Colour.class);
    }

    /**
     *
     * @param colour
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
     *
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
}
