package it.polimi.ingsw.model;

import java.util.Random;

/**
 * represents a bag of students. Other board components who need students usually extract them randomly from the bag
 */
public class Bag {
    private StudentGroup students;

    public Bag(){
        students= new StudentGroup();
    }


    /*public void inizializeIsland(){
        Colour c;

        for(int j;j<numofIslands;j++) {
            for (int i = 0; i < 4; i++) {
                do {
                    c = this.e[this.generator.nextInt(this.e.length)];
                } while (this.bag.getQuantityColour(c) == 0); //genera un colore casuale che però sia usabile e quindi contenuto nella bag
                this.bag.setNumStudents(this.bag.getQuantityColour(c) - 1, c); // mette il numero di studenti del corrispettivo colore nella bag di uno in meno
                //funzione per mettere sull'isola uno studente
            }
        }
    }*/

    /**
     *
     * @param num
     * @return a StudentGroup object with "num" of random students removed from the bag
     */
    public StudentGroup removeStudent(int num){
        if(getNumOfStudents()>=num) {
            Random generator = new Random();
            StudentGroup ret = new StudentGroup();
            Colour[] e = Colour.values();
            Colour c;
            int i = 0;

            for(Colour col : e){
                ret.setNumStudents(0,col);
            }
            while (i<num) {
                c = e[generator.nextInt(e.length)];
                if(students.getQuantityColour(c)>0) {
                    ret.addStudent(c);
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
     * @param group group of students added to the bag
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
