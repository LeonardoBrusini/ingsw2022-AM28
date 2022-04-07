package it.polimi.ingsw.model.players;

public class AssistantCard {
    private int motherNatureShifts;
    private int turnWeight;
    private String fileName;
    private boolean played;

    public AssistantCard(int mns,int tw,String name){
        motherNatureShifts = mns;
        turnWeight = tw;
        fileName = name;
        played = false;
    }

    public int getMotherNatureShifts() {
        return motherNatureShifts;
    }

    public int getTurnWeight() {
        return turnWeight;
    }

   public String getFileName(){
        return fileName;
   }

    public void setPlayed() {
        played = true;
    }

    public void setMotherNatureShifts(int motherNatureShifts) {
        this.motherNatureShifts = motherNatureShifts;
    }
    public boolean getPlayed(){return played;}
}
