package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.model.StudentGroup;

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
     * @param s StudentGroup of the students to add
     */
    public void addGroup(StudentGroup s) throws IllegalArgumentException{
        if(s==null) throw new IllegalArgumentException();
        for(Colour c: Colour.values()){
            students.setNumStudents( s.getQuantityColour(c),c);
        }
    }

    public StudentGroup getStudentsOnCloud() {
        return students;
    }
}
