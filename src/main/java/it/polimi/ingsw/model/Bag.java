package it.polimi.ingsw.model;

import java.util.Random;

public class Bag {
    private StudentGroup bag;
    private Random generator = new Random();
    private Colour[] e = Colour.values();

    //Colour.values()

    public Bag(){
        this.bag = new StudentGroup(26);
    }

    public void inizializeIsland(){
        Colour c;

        //for(int j;j<numofIslands;j++) {
            for (int i = 0; i < 4; i++) {
                do {
                    c = this.e[this.generator.nextInt(this.e.length)];
                } while (this.bag.getQuantityColour(c) == 0); //genera un colore casuale che però sia usabile e quindi contenuto nella bag
                this.bag.setNumStudents(this.bag.getQuantityColour(c) - 1, c); // mette il numero di studenti del corrispettivo colore nella bag di uno in meno
                //funzione per mettere sull'isola uno studente
            }
        //}
    }

    public StudentGroup removeStudent(int num){
        StudentGroup ret = new StudentGroup();
        Colour[] e = Colour.values();
        Colour c;

        for(Colour col : this.e){
            ret.setNumStudents(0,col);
        }

        for (int i = 0; i < num; i++) {
            do {
                c = this.e[this.generator.nextInt(this.e.length)];
            } while (this.bag.getQuantityColour(c) == 0);
            ret.addStudent(c);
            this.bag.removeStudent(c);
        }

        return ret;
    }


    public void addStudent(StudentGroup group){
        int val;
        for(Colour col:this.e){
            val = group.getQuantityColour(col)+this.bag.getQuantityColour(col);
            this.bag.setNumStudents(val,col);
        }
    }

    public void removeStudent(StudentGroup group){
        int val;
        for(Colour col:this.e){
            val = -group.getQuantityColour(col)+this.bag.getQuantityColour(col);
            this.bag.setNumStudents(val,col);
        }
    }


}
