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

    /**
     * the influence of a player is the number of students on the island with the same colour as the professor the player owns, +1 is the player has his tower on the island
     * @param t the Tower of witch the method will calculate the influence
     * @return the influence of the player p
     */
    public int playerInfluence(Tower t,ProfessorGroup professors) {
        int influence = 0;
        for(Colour c : Colour.values()) {
            if(professors.getTower(c).equals(t)) {
                influence += students.getQuantityColour(c);
            }
        }
        if(tower!=null && tower.equals(t)) influence += 1;
        return influence;
    }

    public void clearStudents(){
        students = new StudentGroup();
    }

    //setter & getter for testing

    /*public StudentGroup getStudents() {
        return students;
    }
    public void setStudents(StudentGroup students) {
        this.students = students;
    }*/
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

    public StudentGroup getStudents(){
        return new StudentGroup(this.students);
    }

    public void setStudents(StudentGroup s){
        this.students = new StudentGroup(s);
    }
}
