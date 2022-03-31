package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * represents a bag of students. Other board components who need students usually extract them randomly from the bag
 */
public class Bag {
    private StudentGroup students;

    public Bag(){
        students = new StudentGroup();
    }

    /**
     * puts a student each island (except for the one where mother nature is on and the opposite one), then puts 24 students each colour in the bag
     */
    public void inizializeIslands(){
        for(Colour c : Colour.values()) { students.setNumStudents(2,c); }
        ArrayList<Colour> randomExtraction = removeStudent(10);
        int mnIndex = Board.instance().getMotherNature().getIslandIndex();
        int oppositeOfMNIndex = mnIndex>6 ? mnIndex-6 : mnIndex+6;
        int j=0;
        for(int i=1;i<=12;i++) {
            if(i!=mnIndex && i!=oppositeOfMNIndex) {
                Board.instance().getIslandManager().getIsland(i).addStudent(randomExtraction.get(j++));
            }
        }
        for(Colour c : Colour.values()) { students.setNumStudents(24,c); }
    }

    /**
     *
     * @param num
     * @return a list of randomly selected students
     */
    public ArrayList<Colour> removeStudent(int num){
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
     * ???
     * @param group
     */
    public void removeStudent(StudentGroup group){
        int val;
        for(Colour col: Colour.values()){
            val = -group.getQuantityColour(col)+students.getQuantityColour(col);
            students.setNumStudents(val,col);
        }
    }

    /**
     * supprts the operation of extracting the students from the bag
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
        this.students.setStudents(s);
    }


}
