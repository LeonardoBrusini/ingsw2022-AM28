package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.CharacterCardInfo;
import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CharacterCard;

import java.util.ArrayList;

public class Island {
    private Tower tower;
    private StudentGroup students;
    private final int islandIndex;

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
    public int playerInfluence(Tower t, ProfessorGroup professors, ArrayList<CharacterCard> cards) throws IllegalArgumentException{
        if(t==null || professors==null) throw new IllegalArgumentException();
        CharacterCard card = null;

        int influence = 0;
        for(CharacterCard ca : cards) {
            if(ca.getCardInfo()==CharacterCardInfo.CARD9) card = ca;
        }
        for(Colour c : Colour.values()) {
            //doesn't update influence to students selected on CARD9
            if(professors.getTower(c) != null && professors.getTower(c).equals(t) && (card==null || !card.isActivated() || card.getSelectedColour()!=c)) {
                influence += students.getQuantityColour(c);
            }
        }
        //this condition must be false if CARD6 IS ACTIVATED
        card = null;
        for(CharacterCard ca : cards) {
            if(ca.getCardInfo()==CharacterCardInfo.CARD6)  card = ca;
        }
        if(tower!=null && tower.equals(t) && (card==null || !card.isActivated())) influence++;
        //2 additional points if this player activated CARD8
        card = null;
        for(CharacterCard ca : cards) {
            if(ca.getCardInfo()==CharacterCardInfo.CARD8) card = ca;
        }
        if(card!=null && card.isActivated() && card.getPlayerThisTurn().getTower()==t) return influence+2;
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
    public Tower getTower() {
        return tower;
    }
    public void setTower(Tower tower) {
        this.tower = tower;
    }
}
