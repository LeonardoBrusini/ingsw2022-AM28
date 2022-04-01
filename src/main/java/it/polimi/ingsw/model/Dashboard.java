package it.polimi.ingsw.model;

/**
 * Rappresents the Player's Dashboard
 */
public class Dashboard {
    private StudentGroup hall;
    private StudentGroup entrance;
    private int numTower;
    private Tower tower;

    /**
     * Constructor of a new object of class Dashboard
     * @param n initial number of towers allowed for single Player based on game mode choosen by the user
     * @param t colour of the tower that will identify the Player owner of the Dashboard during the match
     */

    public Dashboard(int n,Tower t){
        this.hall = new StudentGroup(0);
        this.entrance = new StudentGroup(0);
        this.numTower = n;
        this.tower = t;
    }

    /**
     * Sets the students at the entrance of the Dashboard
     * @param studentGroup it is the StudentGroup with the students to add to the entrance
     */
    public void fillEntrance(StudentGroup studentGroup){
        Colour[] e = Colour.values();
        for(Colour c: e){
            this.entrance.setNumStudents(studentGroup.getQuantityColour(c),c);
        }
    }

    /**
     * Move a student of the chosen colour from the entrance to the Hall
     * @param colour It is the colour of the chosen student to move
     */
    public void addToHall(Colour colour) {
        removeFromEntrance(colour);
        this.hall.addStudent(colour);
        if (Board.instance().getProfessorGroup().getTower(colour) == null){
            Board.instance().getProfessorGroup().setTower(colour, this.tower);
        }
        /*else{
            ExpertGameManager.instance().checkProfessors();
        }*/
    }

    /**
     * Removes the student of the chosen colour from the entrance
     * @param colour It is the colour of the chosen student to remove
     */
    public void removeFromEntrance(Colour colour){
        this.entrance.removeStudent(colour);
    }

    /**
     * Getter of the students of the chosen colour remaining in the hall
     * @param colour the method will count the number of students of this colour in the hall
     * @return the number of students in the hall of the chosen colour
     */
    public int getNumStudentsHall(Colour colour){
        return this.hall.getQuantityColour(colour);
    }

    /**
     * Increments the number of towers in the Dashboard
     */
    public void addTower(){
        this.numTower++;
    }

    /**
     * Decrements the number of towers in the Dashboard
     */
    public void buildTower() {
        this.numTower--;
    }
}
