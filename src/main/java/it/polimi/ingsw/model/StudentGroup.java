package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.EnumMap;

public class StudentGroup {
    private EnumMap<Colour, Integer> students;

    public StudentGroup(){
        this.students = new EnumMap<Colour,Integer>(Colour.class);
    }

    public StudentGroup(int n){
        this.students = new EnumMap<Colour, Integer>(Colour.class);
        for(Colour c: Colour.values())
            this.students.put(c,n);
    }

    public StudentGroup(StudentGroup r){
        Colour[] e = Colour.values();
        for(Colour c:e){
            this.students.put(c,r.getQuantityColour(c));
        }
    }

    public int getQuantityColour(Colour colore){
        return this.students.get(colore);
    }

    public void setNumStudents(int val,Colour colore){
        this.students.put(colore,val);
    }

    public void removeStudent(Colour colore){
        this.students.put(colore,(this.students.get(colore)-1));
    }

    public void addStudent(Colour a){
        this.students.put(a,(this.students.get(a)+1));
    }
}
