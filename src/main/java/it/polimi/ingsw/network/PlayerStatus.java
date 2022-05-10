package it.polimi.ingsw.network;

/**
 * It represents the current status of a Player during the match
 */
public class PlayerStatus {
    private String nickName;
    private int index;
    private Integer lastAssistantCardPlayed;
    private Integer coins;
    private Integer numTowers;
    private int [] studentsOnHall;
    private int[] studentsOnEntrance;
    private boolean[] assistantCards = new boolean[10];
    private String towerColour;
    private Boolean addedShifts;

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
    public void setAssistantCards(boolean[] assistantCard) {
        assistantCards = assistantCard;
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

    public Boolean getAddedShifts() {
        return addedShifts;
    }

    public void setAddedShifts(Boolean addedShifts) {
        this.addedShifts = addedShifts;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getLastAssistantCardPlayed() {
        return lastAssistantCardPlayed;
    }

    public void setLastAssistantCardPlayed(Integer lastAssistantCardPlayed) {
        this.lastAssistantCardPlayed = lastAssistantCardPlayed;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getNumTowers() {
        return numTowers;
    }

    public void setNumTowers(Integer numTowers) {
        this.numTowers = numTowers;
    }

    public int[] getStudentsOnHall() {
        return studentsOnHall;
    }

    public int[] getStudentsOnEntrance() {
        return studentsOnEntrance;
    }

    public boolean[] getAssistantCards() {
        return assistantCards;
    }

    public String getTowerColour() {
        return towerColour;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
