package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * represents a bag of students. Other board components who need students usually extract them randomly from the bag
 */
public class Bag {
    private StudentGroup students;
    private int totalStudents;

    public Bag(){
        students = new StudentGroup();
    }

    /**
     * Fills the bag with all 26 students and puts a student each island (except for the one where mother nature is on and the opposite one)
     */
    public void inizializeIslands(){
        for(Colour c : Colour.values()) { students.setNumStudents(2,c); }
        int mnIndex = Board.instance().getMotherNature().getIslandIndex();
        int oppositeOfMNIndex = mnIndex>6 ? mnIndex-6 : mnIndex+6;
        ArrayList<Colour> extractedStudents = removeStudents(10);
        int j = 0;
        for(int i=1;i<=12;i++) {
            if(i!=mnIndex && i!=oppositeOfMNIndex) {
                Board.instance().getIslandManager().getIsland(i).addStudent(extractedStudents.get(j++));
            }
        }
        for(Colour c : Colour.values()) { students.setNumStudents(24,c); }
    }
    //probably wrong
    /*public void inizializeIslands(){
        for(Colour c : Colour.values()) { students.setNumStudents(26,c); }
        int mnIndex = Board.instance().getMotherNature().getIslandIndex();
        int oppositeOfMNIndex = mnIndex>6 ? mnIndex-6 : mnIndex+6;
        Random generator = new Random();
        Colour[] c = Colour.values();
        for(int i=1;i<=12;i++) {
            if(i!=mnIndex && i!=oppositeOfMNIndex) {
                Board.instance().getIslandManager().getIsland(i).addStudent(c[generator.nextInt(c.length)]);
            }
        }
    }*/

    /**
     *
     * @param num the number of random students to remove
     * @return a list of randomly selected students
     */
    public ArrayList<Colour> removeStudents(int num){
        if(getNumOfStudents()>=num) {
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
            return ret;
        } else {
            return null;
        }
    }

    public StudentGroup removeStudentGroup(int n){
        if(n<=this.totalStudents)
            return students;
        else{
            StudentGroup ret = new StudentGroup();
            for(int i = 0;i<n;i++){
                int pos;
                Colour c;
                Colour[] e = Colour.values();
                Random generator = new Random();
                do{
                    pos = generator.nextInt(e.length);
                    c = e[pos];
                }while(this.students.getQuantityColour(c) == 0);
                this.students.removeStudent(c);
                ret.addStudent(c);
            }
            totalStudents = totalStudents-n;
            return ret;
        }


    }

    /**
     *
     * @param group group of students which is going to be added to the bag
     */
    public void addStudent(StudentGroup group){
        int val;
        for(Colour col: Colour.values()){
            val = group.getQuantityColour(col)+students.getQuantityColour(col);
            students.setNumStudents(val,col);
        }
    }

    /**
     * Removes the selected StudentGroup from the bag
     * @param group the group of students to remove from the bag
     */
    /*public void removeStudent(StudentGroup group){
        int val;
        for(Colour col: Colour.values()){
            val =- group.getQuantityColour(col)+students.getQuantityColour(col);
            students.setNumStudents(val,col);
        }
    }*/

    /**
     * supports the operation of extracting the students from the bag
     * @return the total number of students in the bag
     */
    public int getNumOfStudents() {
        int num=0;
        for(Colour c : Colour.values()) {
            num += students.getQuantityColour(c);
        }
        return num;
    }

    public void setStudents(StudentGroup s){
        students.setStudents(s);
    }

    /*
    /**
     * Extracts the chosen number of students from the bag adding them in a StudentGroup
     * @param num It is the number of students to extract
     * @return the extracted students from the bag returning them in a StudentGroup
     */
    /*
    public StudentGroup removeStudents(int num){
        StudentGroup ris = new StudentGroup();
        Random generator = new Random();
        Colour[] c = Colour.values();
        Colour extracted;
        if(getNumOfStudents()>=num){
            for(int i = 0; i < num && !students.empty(); i++){
                extracted = c[generator.nextInt(c.length)];
                if(students.getQuantityColour(extracted)>0){
                    ris.addStudent(extracted);
                    students.removeStudent(extracted);
                }
            }
            return ris;
        }
        return null;
    }*/

    /**
     * Indicates if the bag is empty
     * @return a boolean value that indicates if the bag is empty
     */
    public boolean isEmpty(){
        return students.empty();
    }

}
