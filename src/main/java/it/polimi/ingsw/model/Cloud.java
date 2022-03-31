package it.polimi.ingsw.model;

public class Cloud {
    private StudentGroup cloud;
    private StudentGroup students;

    public StudentGroup getCloud() {
        return cloud;
    }

    public Cloud(){
        this.cloud = new StudentGroup('0');
    }

    public boolean empty(){
        return this.cloud.empty();
    }

    public void setGroup(StudentGroup group){
        Colour[] e = Colour.values();
        for(Colour c: e){
            this.cloud.setNumStudents(group.getQuantityColour(c),c);
        }

    }

    public StudentGroup clearStudent() {
        StudentGroup ret = new StudentGroup(this.cloud);
        Colour[] e = Colour.values();
        for (Colour c : e) {
            this.cloud.setNumStudents(0, c);
        }
        return ret;
    }



    public void addGroup(StudentGroup s){
        for(Colour c: Colour.values()){
            students.setNumStudents(students.getQuantityColour(c) + s.getQuantityColour(c),c);
        }
    }

    public StudentGroup clearStudents(){
        StudentGroup s;
        s = students;
        return s;
    }
}
