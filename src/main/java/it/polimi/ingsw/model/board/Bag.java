package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * represents a bag of students. Other board components who need students usually extract them randomly from the bag
 */
public class Bag {
    private final StudentGroup students;
    private int totalStudents;

    public Bag(){
        students = new StudentGroup();
    }

    /**
     * Fills the bag with 2 students each colour students and puts a student each island (except for the one where mother nature is on and the opposite one)
     * Then fills the bag with 24 students each colour
     */
    public void initializeIslands(int mnIndex, IslandManager im) {
        for(Colour c : Colour.values()) { students.setNumStudents(2,c); }
        int oppositeOfMNIndex = mnIndex>6 ? mnIndex-6 : mnIndex+6;
        ArrayList<Colour> extractedStudents = removeStudents(10);
        int j = 0;
        for(int i=1;i<=12;i++) {
            if(i!=mnIndex && i!=oppositeOfMNIndex) {
                im.getIsland(i).addStudent(extractedStudents.get(j++));
            }
        }
        for(Colour c : Colour.values()) { students.setNumStudents(24,c); }
        totalStudents = 120;
    }

    /**
     *
     * @param num the number of random students to remove
     * @return a list of randomly selected students
     */
    public ArrayList<Colour> removeStudents(int num) throws IllegalArgumentException{
        if(totalStudents<num) throw new IllegalArgumentException();
        Random generator = new Random();
        ArrayList<Colour> ret = new ArrayList<>();
        Colour[] e = Colour.values();
        Colour c;
        int i = 0;
        while (i<num) {
            c = e[generator.nextInt(e.length)];
            if(students.getQuantityColour(c)>0) {
                ret.add(c);
                students.removeStudent(c);
                i++;
            }
        }
        totalStudents-=num;
        return ret;
    }

    /**
     * this method extracts students and inserts them in a StudentGroup
     * @param n the number of students to be extracted
     * @return StudentGroup of extracted students
     */
    public StudentGroup removeStudentGroup(int n) throws IllegalArgumentException{
        StudentGroup ret = new StudentGroup();
        if(n>totalStudents) throw new IllegalArgumentException();
        int i=0;
        Colour c;
        Colour[] e = Colour.values();
        Random generator = new Random();
        while (i<n) {
            c = e[generator.nextInt(e.length)];
            if(students.getQuantityColour(c)>0) {
                ret.addStudent(c);
                students.removeStudent(c);
                i++;
            }
        }
        totalStudents -= n;
        return ret;
    }

    /**
     *
     * @param group group of students which is going to be added to the bag
     */
    public void addStudent(StudentGroup group) throws NullPointerException{
        if (group == null) throw new NullPointerException();
        int val;
        for(Colour col: Colour.values()){
            val = group.getQuantityColour(col)+students.getQuantityColour(col);
            students.setNumStudents(val,col);
            totalStudents += group.getQuantityColour(col);
        }
    }

    /**
     * sets a new student group while updating totalStudents
     * @param s student group argument
     */
    public void setStudents(StudentGroup s){
        students.setStudents(s);
        totalStudents = 0;
        for (Colour c: Colour.values()) {
            totalStudents+=students.getQuantityColour(c);
        }
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public boolean isEmpty(){
        return students.empty();
    }

    public StudentGroup getStudents() { return students; }
}
