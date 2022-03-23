package com.example.prova1;

import java.util.ArrayList;
import java.util.EnumMap;

public class StudentGroup {
    private EnumMap<Colour, Integer> students;

    public StudentGroup(){
        this.students = new EnumMap<Colour,Integer>(Colour.class);
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
