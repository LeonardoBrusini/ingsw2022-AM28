package com.example.prova1;

public class AssistantCard {
    private int motherNatureShifts;
    private int turnWeight;
    private String fileName;
    private boolean played;

    public AssistantCard(int mns,int tw,String name){
        this.motherNatureShifts = mns;
        this.turnWeight = tw;
        this.fileName=name;
        this.played = false;
    }

    public int getMotherNatureShifts() {
        return this.motherNatureShifts;
    }

    public int getTurnWeight() {
        return this.turnWeight;
    }

   public String getFileName(){
        return this.fileName;
   }
}
