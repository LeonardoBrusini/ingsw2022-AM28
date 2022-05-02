package it.polimi.ingsw.network;

/**
 * It represents the current status of a Cloud during the match
 */
public class CloudStatus {
    int index;
    int[] students;

    /**
     * It sets the index of the cloud
     * @param index the index of the cloud
     */
    public void setIndex(int index){
        this.index=index;
    }

    /**
     * It sets the number of student of the selected colour that are on the cloud
     * @param pos The position that corresponds to the selected colour in the Colour's enum
     * @param quantity The number of students of the selected colour to set
     */
    public void setStudents(int pos,int quantity){
        this.students[pos]=quantity;
    }

    /**
     * It returns how many students of that Colour there are on the Cloud
     * @param pos he position that corresponds to the selected colour in the Colour's enum
     * @return The number of students of the selected colour there are on the Cloud
     */
    public int getStudentsOfAColour(int pos){
        return students[pos];
    }

    /**
     * It gets the index of the Cloud
     * @return the index of the cloud
     */
    public int getIndex(){
        return index;
    }

    /**
     * Getter quantity of students on the Cloud
     * @return the array that stores the requested value
     */
    public int[] getStudents(){
        return students;
    }
}
