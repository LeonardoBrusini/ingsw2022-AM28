package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.*;

public class Island {
    private Tower tower;
    private StudentGroup students;
    private int islandIndex;

    /**
     * constructor
     * @param index of the island on the board
     */
    public Island(int index){
        tower = null;
        islandIndex = index;
        students = new StudentGroup();
    }

    /**
     * adds a student to the island
     * @param c colour of the student we want to add
     */
    public void addStudent(Colour c) throws IllegalArgumentException {
        if(c==null) throw new IllegalArgumentException();
        students.addStudent(c);
    }

    /**
     * the influence of a player is the number of students on the island with the same colour as the professor the player owns, +1 is the player has his tower on the island
     * @param t the tower of the Player of witch the method will calculate the influence
     * @return the influence of the player p
     */
    public int playerInfluence(Tower t, ProfessorGroup professors) throws IllegalArgumentException{
        if(t==null || professors==null) throw new IllegalArgumentException();
        int influence = 0;
        for(Colour c : Colour.values()) {
            //doesn't update influence to students selected on CARD9
            if(professors.getTower(c) != null && professors.getTower(c).equals(t)) {
                influence += students.getQuantityColour(c);
            }
        }
        //this condition must be false if CARD6 IS ACTIVATED
        if(tower!=null && tower.equals(t)) influence++;
        //2 additional points if this player activated CARD8
        return influence;
    }

    /**
     * turn to 0 the number of students on the Island
     */
    public void clearStudents(){
        students = new StudentGroup();
    }

    //setter & getter for testing
    public StudentGroup getStudents() {
        return students;
    }
    public void setStudents(StudentGroup students) {
        this.students = students;
    }
    public int getIslandIndex() {
        return islandIndex;
    }
    public void setIslandIndex(int islandIndex) {
        this.islandIndex = islandIndex;
    }
    public Tower getTower() {
        return tower;
    }
    public void setTower(Tower tower) {
        this.tower = tower;
    }
}
