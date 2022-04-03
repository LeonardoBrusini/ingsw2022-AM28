package it.polimi.ingsw.model;

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
    public void addStudent(Colour c) {
        students.addStudent(c);
    }

    public void setTower(Tower t) {
        tower = t;
    }

    /**
     * the influence of a player is the number of students on the island with the same colour as the professor the player owns, +1 is the player has his tower on the island
     * @param p the Player of witch the method will calculate the influence
     * @return the influence of the player p
     */
    public int playerInfluence(Player p) {
        int influence = 0;
        for(Colour c : Colour.values()) {
            if(Board.instance().getProfessorGroup().getTower(c).equals(p.getTower())) {
                influence += students.getQuantityColour(c);
            }
        }
        if(tower!=null && tower.equals(p.getTower())) influence += 1;
        return influence;
    }

    /**
     * Getter for the tower that controls the island
     * @return the tower witch controls the island
     */
    public Tower getTower() {
        return tower;
    }

    /**
     * Getter for the index of the island
     * @return the number that identifies the island
     */
    public int getIslandIndex() {
        return islandIndex;
    }

    public void clearStudents(){
        this.students = new StudentGroup();
    }

    public StudentGroup getStudents(){
        return new StudentGroup(this.students);
    }

    public void setStudents(StudentGroup s){
        this.students = new StudentGroup(s);
    }
}
