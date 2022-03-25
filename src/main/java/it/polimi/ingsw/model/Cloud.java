package it.polimi.ingsw.model;

public class Cloud {
    private StudentGroup cloud;

    public Cloud(){
        this.cloud = new StudentGroup('0');
    }

    public void setGroup(StudentGroup group){
        Colour[] e = Colour.values();
        for(Colour c: e){
            this.cloud.setNumStudents(group.getQuantityColour(c),c);
        }

    }

    public StudentGroup clearStudent(){
        StudentGroup ret = new StudentGroup(this.cloud);
        Colour[] e = Colour.values();
        for(Colour c: e){
            this.cloud.setNumStudents(0,c);
        }
        return ret;
    }
}
