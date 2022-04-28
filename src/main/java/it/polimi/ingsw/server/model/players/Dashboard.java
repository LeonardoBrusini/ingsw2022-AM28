package it.polimi.ingsw.server.model.players;

import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.enumerations.Tower;

/**
 * represents the Player's Dashboard
 */
public class Dashboard {
    private final StudentGroup hall;
    private final StudentGroup entrance;
    private int numTowers;
    private final Tower tower;


    /**
     * Constructor of a new object of class Dashboard
     * @param t colour of the tower that will identify the Player owner of the Dashboard during the match
     */
    public Dashboard(Tower t){
        hall = new StudentGroup();
        entrance = new StudentGroup();
        numTowers = 0;
        tower = t;
    }

    /**
     * Sets the students at the entrance of the Dashboard
     * @param studentGroup it is the StudentGroup with the students to add to the entrance
     */
    public void fillEntrance(StudentGroup studentGroup){
        for(Colour c: Colour.values()){
            entrance.setNumStudents(studentGroup.getQuantityColour(c)+entrance.getQuantityColour(c),c);
        }
    }

    /**
     * Move a student of the chosen colour from the entrance to the Hall
     * @param colour It is the colour of the chosen student to move
     */
    public void addToHall(Colour colour) throws FullHallException {
        if(hall.getQuantityColour(colour)>=10) throw new FullHallException();
        hall.addStudent(colour);
    }

    /**
     * Removes the student of the chosen colour from the entrance
     * @param colour It is the colour of the chosen student to remove
     */
    public void removeFromEntrance(Colour colour) throws NoStudentsException {
        if(entrance.getQuantityColour(colour)<=0) throw new NoStudentsException();
        entrance.removeStudent(colour);
    }

    /**
     * Removes the student of the chosen colour from the hall
     * @param colour It is the colour of the chosen student to remove
     */
    public void removeFromHall(Colour colour) throws IllegalArgumentException{
        if(hall.getQuantityColour(colour)<=0) throw new IllegalArgumentException();
        hall.removeStudent(colour);
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
        numTowers++;
    }

    /**
     * Decrements the number of towers in the Dashboard
     */
    public void buildTower() {
        numTowers--;
    }

    //getter & setter for testing
    public StudentGroup getEntrance() {
        return entrance;
    }

    public StudentGroup getHall() {
        return hall;
    }

    public int getNumTowers(){
        return this.numTowers;
    }

    public void setNumTowers(int numTower) {
        this.numTowers = numTower;
    }

    public Tower getTower() {
        return tower;
    }

    public void fillHall(StudentGroup studentGroup) throws FullHallException{
        for(Colour colour: Colour.values())
            if(hall.getQuantityColour(colour)+studentGroup.getQuantityColour(colour) >= 10)
                throw new FullHallException();
        for(Colour colour: Colour.values())
            hall.setNumStudents(hall.getQuantityColour(colour) + studentGroup.getQuantityColour(colour), colour);
    }

}
