package it.polimi.ingsw.model;

public class Cloud {
    private StudentGroup students;

    public void setGroup(StudentGroup s){
        for(Colour c: Colour.values()){
            this.students.setNumStudents(students.getQuantityColour(c) + s.getQuantityColour(c),c);
        }
    }

    public StudentGroup clearStudents(){
        StudentGroup s;
        s = students;
        return s;
    }
}
