package com.example.prova1;

public class DashBoard {
    private StudentGroup hall;
    private StudentGroup entrance;
    private int numTower;


    public DashBoard(int num){
        this.hall = new StudentGroup();
        this.entrance = new StudentGroup();
        for(Colour e : this.entrance){
            this.entrance.setNumStudents(0,e);
        }

        this.numTower=num;
    }




}
*