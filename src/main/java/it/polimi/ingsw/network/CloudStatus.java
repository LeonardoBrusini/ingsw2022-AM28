package it.polimi.ingsw.network;

/**
 * It represents the current status of a Cloud during the match
 */
public class CloudStatus {
    int index;
    int[] students;

    public void setIndex(int index){
        this.index=index;
    }

    public void setStudents(int pos,int quantity){
        this.students[pos]=quantity;
    }

    public int getStudentsOfAColour(int pos){
        return students[pos];
    }

    public int getIndex(){
        return index;
    }

    public int[] getStudents(){
        return students;
    }

    public void setStudents(int[] students) {
        this.students = students;
    }
}
