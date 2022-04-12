package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.*;

/**
 * a single student group which represents the student on the cloud, that players can take at the end of their turn
 */
public class Cloud {
    private final StudentGroup students;

    /**
     * Constructor. initialize an empty student group
     */
    public Cloud(){ students = new StudentGroup(); }

    /**
     *
     * @return true if there are no students on the cloud, false otherwise
     */
    public boolean empty(){ return students.empty(); }

    /**
     * removes the students from the cloud
     * @return the students who were on the cloud
     */
    public StudentGroup clearStudents() {
        StudentGroup ret = new StudentGroup(students);
        Colour[] e = Colour.values();
        for (Colour c : e) {
            students.setNumStudents(0, c);
        }
        return ret;
    }

    /**
     * adds students on the cloud
     * @param s
     */
    public void addGroup(StudentGroup s) throws IllegalArgumentException{
        if(s==null) throw new IllegalArgumentException();
        for(Colour c: Colour.values()){
            students.setNumStudents(students.getQuantityColour(c) + s.getQuantityColour(c),c);
        }
    }

    public StudentGroup getStudentsOnCloud() {
        return students;
    }
}
