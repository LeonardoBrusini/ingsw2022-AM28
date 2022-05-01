package it.polimi.ingsw.network;

/**
 * It represents the current status of an Island during the match
 */
public class IslandStatus {
    int islandIndex;
    int[] students;
    String towerColour;

    public int getIslandIndex() {
        return islandIndex;
    }

    public void setIslandIndex(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    public int[] getStudents() {
        return students;
    }

    public void setStudents(int[] students) {
        this.students = students;
    }

    public String getTowerColour() {
        return towerColour;
    }

    public void setTowerColour(String towerColour) {
        this.towerColour = towerColour;
    }

    public void setStudentsOfAColour(int pos,int quantity){
        students[pos]=quantity;
    }

    public int getStudentsOfAColour(int pos){
        return students[pos];
    }
}
