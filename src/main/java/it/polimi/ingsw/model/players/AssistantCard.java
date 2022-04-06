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
    public void setMotherNatureShifts(int motherNatureShifts) {
        this.motherNatureShifts = motherNatureShifts;
    }
    public int getTurnWeight() {
        return turnWeight;
    }
    public void setTurnWeight(int turnWeight) {
        this.turnWeight = turnWeight;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public boolean isPlayed() {
        return played;
    }
    public void setPlayed(boolean played) {this.played = played;}
}
