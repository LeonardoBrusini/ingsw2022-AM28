package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.enumerations.Colour;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * StudentGroup is to store the number of students of each colour, it will be used in Bag, Cloud, Island and on CharacterCard
 */
public class StudentGroup {
    private final EnumMap<Colour, Integer> students;

    /**
     * Constructor of a new StudentGroup without students
     */
    public StudentGroup(){
        this.students = new EnumMap<>(Colour.class);
        for(Colour c: Colour.values())
            students.put(c,0);
    }

    /**
     * Constructor of a new StudentGroup with n students each colour
     * @param n number of students of a colour
     */
    public StudentGroup(int n){
        students = new EnumMap<>(Colour.class);
        for(Colour c: Colour.values())
            students.put(c,n);
    }

    /**
     * Constructor of a new StudentGroup with the same number of students as r
     * @param r is an already defined StudentGroup object
     */
    public StudentGroup(StudentGroup r){
        students = new EnumMap<>(Colour.class);
        for(Colour c: Colour.values()){
            students.put(c,r.getQuantityColour(c));
        }
    }

    /**
     * Constructor of a new StudentGroup by a list of students (i.e. randomly extracted from the bag)
     * @param list list of colours (student extracted in order)
     */
    public StudentGroup(ArrayList<Colour> list) {
        students = new EnumMap<>(Colour.class);
        for(Colour c: Colour.values()) students.put(c,0);
        for(Colour c: list) addStudent(c);
    }

    /**
     *
     * @param colour colour of the students
     * @return the number of students of the parameter Colour
     */
    public int getQuantityColour(Colour colour){
        return students.get(colour);
    }

    /**
     * Sets the number of student of a specified colour (only if > 0)
     * @param val number of students
     * @param colour colour of the students
     */
    public void setNumStudents(int val,Colour colour){
        if(val>=0) students.put(colour,val);
    }

    /**
     * remove a single student of a selected colour
     * @param colour the colour of the student we want to remove
     */
    public void removeStudent(Colour colour){
        if(students.get(colour)>0) students.put(colour,(students.get(colour)-1));
    }

    /**
     * adds a single student of a selected colour
     * @param colour the colour of the student we want to add
     */
    public void addStudent(Colour colour){
        students.put(colour,(students.get(colour)+1));
    }

    /**
     * adds the students in the parameter to the group
     * @param s students to add
     */
    public void addStudents(StudentGroup s) {
        for(Colour c : Colour.values()) {
            students.put(c,students.get(c)+s.getQuantityColour(c));
        }
    }

    /**
     * checks if the map is empty
     * @return true if empty group, false otherwise
     */
    public boolean empty(){
        for(Colour c : Colour.values())
            if(students.containsKey(c) && students.get(c)>0)
                return false;
        return true;
    }

    public int getTotalStudents() {
        int ret =0;
        for (Colour c: Colour.values()) {
            ret+=students.get(c);
        }
        return ret;
    }

    /**
     * copies the number of students in s on this group
     * @param s students to copy
     */
    public void setStudents(StudentGroup s){
        for(Colour c: Colour.values()){
            students.put(c,s.getQuantityColour(c));
        }
    }

    /**
     *
     * @return array of the number of students each colour
     */
    public int[] getStatus() {
        int[] s = new int[Colour.values().length];
        for(Colour c: Colour.values()) {
            s[c.ordinal()] = students.get(c);
        }
        return s;
    }
}
