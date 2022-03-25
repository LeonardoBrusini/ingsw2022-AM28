package it.polimi.ingsw.model;

public class DashBoard {
    private StudentGroup hall;
    private StudentGroup entrance;
    private int numTower;

    public DashBoard(int n){
        this.hall = new StudentGroup();
        this.entrance = new StudentGroup();
        this.numTower = n;
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

    public void builtTower(){
        this.numTower--;
    }

    public void addTower(){
        this.numTower++;
    }
}
