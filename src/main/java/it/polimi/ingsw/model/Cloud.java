package it.polimi.ingsw.model;

public class Cloud {
    private StudentGroup students;

    public StudentGroup getStudentsOnCloud() {
        return students;
    }

    public Cloud(){
        students = new StudentGroup('0');
    }

    public boolean empty(){
        return students.empty();
    }

    public void setStudents(StudentGroup group){
        Colour[] e = Colour.values();
        for(Colour c: e){
            students.setNumStudents(group.getQuantityColour(c),c);
        }
    }

    public StudentGroup clearStudents() {
        StudentGroup ret = new StudentGroup(students);
        Colour[] e = Colour.values();
        for (Colour c : e) {
            students.setNumStudents(0, c);
        }
        return ret;
    }


    public void addGroup(StudentGroup s){
        for(Colour c: Colour.values()){
            students.setNumStudents(students.getQuantityColour(c) + s.getQuantityColour(c),c);
        }
    }
}
