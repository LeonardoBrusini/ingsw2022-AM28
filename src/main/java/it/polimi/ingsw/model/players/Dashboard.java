package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;

/**
 * Rappresents the Player's Dashboard
 */
public class Dashboard {
    private final StudentGroup hall;
    private final StudentGroup entrance;
    private int numTower;
    private final Tower tower;

    /**
     * Constructor of a new object of class Dashboard
     * @param n initial number of towers allowed for single Player based on game mode choosen by the user
     * @param t colour of the tower that will identify the Player owner of the Dashboard during the match
     */
    public Dashboard(int n,Tower t){
        hall = new StudentGroup(0);
        entrance = new StudentGroup(0);
        numTower = n;
        tower = t;
    }

    /**
     * Sets the students at the entrance of the Dashboard
     * @param studentGroup it is the StudentGroup with the students to add to the entrance
     */
    public void fillEntrance(StudentGroup studentGroup){
        Colour[] e = Colour.values();
        for(Colour c: e){
            entrance.setNumStudents(studentGroup.getQuantityColour(c),c);
        }
    }

    /**
     * Move a student of the chosen colour from the entrance to the Hall
     * @param colour It is the colour of the chosen student to move
     */
    public void addToHall(Colour colour) {
        removeFromEntrance(colour);
        hall.addStudent(colour);
        /*
          ExpertGameManager.instance().checkProfessors();
        */
    }

    /**
     * Removes the student of the chosen colour from the entrance
     * @param colour It is the colour of the chosen student to remove
     */
    public void removeFromEntrance(Colour colour){
        entrance.removeStudent(colour);
    }

    /**
     * Getter of the students of the chosen colour remaining in the hall
     * @param colour the method will count the number of students of this colour in the hall
     * @return the number of students in the hall of the chosen colour
     */
    public int getNumStudentsHall(Colour colour){
        return hall.getQuantityColour(colour);
    }

    /**
     * Increments the number of towers in the Dashboard
     */
    public void addTower(){
        numTower++;
    }

    /**
     * Decrements the number of towers in the Dashboard
     */
    public void buildTower() {
        numTower--;
    }

    public StudentGroup getEntrance() {
        return new StudentGroup(entrance);
    }

    public StudentGroup getHall() {
        return new StudentGroup(hall);
    }

    public int getNumTower(){
        return this.numTower;
    }
}
