package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.EnumMap;

public class ProfessorGroup {
    private EnumMap<Colour, Tower> professors;

    public ProfessorGroup(){
        this.professors = new EnumMap<Colour,Tower>(Colour.class);
    }

    public Tower getTower(Colour colour){
        return this.professors.get(colour);
    }

    public void setTower(Colour colour,Tower tower){
        this.professors.put(colour,tower);
    }

    public ArrayList<Colour> getColours(Tower t){
        ArrayList<Colour> ret = new ArrayList<>();
        for(Colour c: Colour.values())
            if(t.equals(this.professors.get(c)))
                ret.add(c);

        return ret;
    }





}
