package it.polimi.ingsw.network;

public class Command {
    private String cmd;
    private int playerIndex;
    private String studentColour;
    private Integer index;
    private Integer motherNatureShifts;
    //character card parameters
    private Integer pIndex;
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

    public Integer getIndex() {
        return index;
    }

    public Integer getMotherNatureShifts() {
        return motherNatureShifts;
    }

    public Integer getPIndex() {
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

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public void setStudentColour(String studentColour) {
        this.studentColour = studentColour;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMotherNatureShifts(int motherNatureShifts) {
        this.motherNatureShifts = motherNatureShifts;
    }

    public void setPIndex(int pIndex) {
        this.pIndex = pIndex;
    }

    public void setPColour(String pColour) {
        this.pColour = pColour;
    }

    public void setPStudentsFrom(int[] pStudentsFrom) {
        this.pStudentsFrom = pStudentsFrom;
    }

    public void setPStudentsTo(int[] pStudentsTo) {
        this.pStudentsTo = pStudentsTo;
    }
}
