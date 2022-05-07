package it.polimi.ingsw.network;

/**
 * It represents the current status of a CharacterCard during the match
 */
public class CharacterCardStatus {
    private int index;
    private String fileName;
    private boolean coinOnIt;
    private Integer noEntryTiles;
    private int[] students;

    public void setIndex(int index) {
        this.index = index;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCoinOnIt(boolean coinOnIt) {
        this.coinOnIt = coinOnIt;
    }

    public void setNoEntryTiles(Integer noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

    public void setStudents(int[] students) {
        this.students = students;
    }

    public int getIndex() {
        return index;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isCoinOnIt() {
        return coinOnIt;
    }

    public Integer getNoEntryTiles() {
        return noEntryTiles;
    }

    public int[] getStudents() {
        return students;
    }
}
