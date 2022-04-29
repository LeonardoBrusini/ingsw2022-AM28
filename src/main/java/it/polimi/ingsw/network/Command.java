package it.polimi.ingsw.network;

public class Command {
    private String cmd;
    private int playerIndex;
    private String studentColour;
    private int index;
    private int motherNatureShifts;
    //character card parameters
    private int pIndex;
    private String pColour;
    private int[] pStudentsFrom, pStudentsTo;

    public String getCmd() {
        return cmd;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public String getStudentColour() {
        return studentColour;
    }

    public int getIndex() {
        return index;
    }

    public int getMotherNatureShifts() {
        return motherNatureShifts;
    }

    public int getPIndex() {
        return pIndex;
    }

    public String getPColour() {
        return pColour;
    }

    public int[] getPStudentsFrom() {
        return pStudentsFrom;
    }

    public int[] getPStudentsTo() {
        return pStudentsTo;
    }
}
