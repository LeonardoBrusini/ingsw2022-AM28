package it.polimi.ingsw.network;

/**
 * It represents the current status of a Player during the match
 */
public class PlayerStatus {
    public void setIndex(int index) {
        this.index = index;
    }

    public void setLastAssistantCardPlayed(int lastAssistantCardPlayed) {
        this.lastAssistantCardPlayed = lastAssistantCardPlayed;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public void setAssistantCard(boolean[] assistantCard) {
        AssistantCard = assistantCard;
    }

    int index;
    int lastAssistantCardPlayed;
    int coins;
    int numTowers;
    int [] studentsOnHall;
    int[] studentsOnEntrance;
    boolean[] professors;
    boolean[] AssistantCard;

    public void setStudentsOnHallOfAColour(int pos,int quantity){
        studentsOnHall[pos] = quantity;
    }

    public void setStudentsOnEntrance(int pos,int quantity){
        studentsOnEntrance[pos]=quantity;
    }

    public void setStudentsOnHall(int[] s){
        this.studentsOnHall = s;
    }

    public void setStudentsOnEntrance(int [] s){
        this.studentsOnEntrance = s;
    }

    public void playAssistantCard(int pos) {
        if (AssistantCard[pos])
            AssistantCard[pos] = false;
        /*else {
            //Lancia errore;
        }*/
    }

    public void setProfessorsOfAColour(int pos, boolean professor){
        this.professors[pos] = professor;
    }

    public void setProfessors(boolean[] prof){
        this.professors=prof;
    }

    public void inizializeAssistantCard(){
        for(boolean b:AssistantCard)
            b=true;
    }

}
