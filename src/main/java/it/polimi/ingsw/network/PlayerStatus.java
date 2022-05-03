package it.polimi.ingsw.network;

/**
 * It represents the current status of a Player during the match
 */
public class PlayerStatus {
    private int index;
    private int lastAssistantCardPlayed;
    private int coins;
    private int numTowers;
    private int [] studentsOnHall;
    private int[] studentsOnEntrance;
    private boolean[] AssistantCard;
    private String towerColour;

    /**
     * To set the tower's colour
     * @param towerColour (WHITE, BLACK, GREY)
     */
    public void setTowerColour(String towerColour) {
        this.towerColour = towerColour;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * To set what the last Assistant Card Played is
     * @param lastAssistantCardPlayed the card to set
     */
    public void setLastAssistantCardPlayed(int lastAssistantCardPlayed) {
        this.lastAssistantCardPlayed = lastAssistantCardPlayed;
    }

    /**
     * To set how many coins remain to the player
     * @param coins the code to set
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * To set how many towers remain to the player
     * @param numTowers the towers to set
     */
    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    /**
     * What are the AssistantCard that remain to the player
     * @param assistantCard AssistantCard to set
     */
    public void setAssistantCard(boolean[] assistantCard) {
        AssistantCard = assistantCard;
    }

    /**
     * To set how many students of that colour there are in the hall
     * @param pos the position of the colour in the corresponding enumeration
     * @param quantity the number of students of that colour to set
     */
    public void setStudentsOnHallOfAColour(int pos,int quantity){
        studentsOnHall[pos] = quantity;
    }

    /**
     * To set how many students of that colour there are in the entrance
     * @param pos the position of the colour in the corresponding enumeration
     * @param quantity the number of students of that colour to set
     */
    public void setStudentsOnEntrance(int pos,int quantity){
        studentsOnEntrance[pos]=quantity;
    }

    /**
     * It sets the array of students in the hall
     * @param s students in the hall to set
     */
    public void setStudentsOnHall(int[] s){
        this.studentsOnHall = s;
    }

    /**
     * It sets the array of students in the entrance
     * @param s students in the entrance to set
     */
    public void setStudentsOnEntrance(int [] s){
        this.studentsOnEntrance = s;
    }

    /*
    public void playAssistantCard(int pos) {
        if (AssistantCard[pos])
            AssistantCard[pos] = false;
        /*else {
            //Lancia errore;
        }
    }
    */
    /**
     * The method initializes the AssistantCard
     */
    public void initializeAssistantCard(){
        for(boolean b:AssistantCard)
            b=true;
    }

}
