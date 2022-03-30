package it.polimi.ingsw.model;

public class Dashboard {
    private StudentGroup hall;
    private StudentGroup entrance;
    private int numTower;
    private ProfessorGroup professorgroup;

    public Dashboard(int n){
        this.hall = new StudentGroup(0);
        this.entrance = new StudentGroup(0);
        this.numTower = n;
        this.professorgroup = new ProfessorGroup();
    }

    public void fillEntrance(StudentGroup studentGroup){
        Colour[] e = Colour.values();
        for(Colour c: e){
            this.entrance.setNumStudents(studentGroup.getQuantityColour(c),c);
        }
    }

    public void addToHall(Colour colour){
        removeFromEntrance(colour);
        this.hall.addStudent(colour);
    }

    public void removeFromEntrance(Colour colour){
        this.entrance.removeStudent(colour);
    }

    public int getNumStudentsHall(Colour colour){
        return this.hall.getQuantityColour(colour);
    }

    public void buildTower(){
        this.numTower--;
    }

    public void addTower(){
        this.numTower++;
    }

}
