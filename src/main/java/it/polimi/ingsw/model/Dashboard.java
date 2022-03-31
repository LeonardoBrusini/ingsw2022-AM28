package it.polimi.ingsw.model;

public class Dashboard {
    private StudentGroup hall;
    private StudentGroup entrance;
    private int numTower;
    private Tower tower;


    public Dashboard(int n,Tower t){
        this.hall = new StudentGroup(0);
        this.entrance = new StudentGroup(0);
        this.numTower = n;
        this.tower = t;
    }

    public void fillEntrance(StudentGroup studentGroup){
        Colour[] e = Colour.values();
        for(Colour c: e){
            this.entrance.setNumStudents(studentGroup.getQuantityColour(c),c);
        }
    }

    public void addToHall(Colour colour) {
        removeFromEntrance(colour);
        this.hall.addStudent(colour);
        if (Board.instance().getProfessorGroup().getTower(colour) == null){
            Board.instance().setProfessorGroup(colour, this.tower);
        }
        /*else{
            ExpertGameManager.instance().checkProfessors();
        }*/
    }

    public void removeFromEntrance(Colour colour){
        this.entrance.removeStudent(colour);
    }

    public int getNumStudentsHall(Colour colour){
        return this.hall.getQuantityColour(colour);
    }

    public void addTower(){
        this.numTower++;
    }

    public void buildTower() {
        this.numTower--;
    }
}
