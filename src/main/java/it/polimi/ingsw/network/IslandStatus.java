package it.polimi.ingsw.network;

/**
 * It represents the current status of an Island during the match
 */
public class IslandStatus {
    int islandIndex;
    int[] students;
    String towerColour;

    /**
     * It returns the index of the Island
     * @return index of the Island
     */
    public int getIslandIndex() {
        return islandIndex;
    }

    /**
     * It returns the index of the Island
     * @param islandIndex  index of the Islan
     */
    public void setIslandIndex(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    /**
     * It returns the Students on the Island
     * @return the array of students with the number of students of that colour there are on the Island
     */
    public int[] getStudents() {
        return students;
    }

    /**
     * It sets the Students on the Island
     */
    public void setStudents(int[] students) {
        this.students = students;
    }

    /**
     * It return the Tower, if there is, that is on the Island
     * @return the string of the Colour of the Tower
     */
    public String getTowerColour() {
        return towerColour;
    }

    /**
     * It sets the Tower that is on the Island
     * @param towerColour the string of the Colour of the Tower
     */
    public void setTowerColour(String towerColour) {
        this.towerColour = towerColour;
    }

    /**
     * It sets how many students of that colour there are on the Island
     * @param pos the position of the colour in the enumeration
     * @param quantity the number of students of that colour that are on the Island
     */
    public void setStudentsOfAColour(int pos,int quantity){
        students[pos]=quantity;
    }

    /**
     * It returns how many students of that colour there are on the Island
     * @param pos the position of the colour in the enumeration
     * @return the number of students of that colour that are on the Island
     */
    public int getStudentsOfAColour(int pos){
        return students[pos];
    }
}
