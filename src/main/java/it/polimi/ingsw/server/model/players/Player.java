package it.polimi.ingsw.server.model.players;

import it.polimi.ingsw.server.controller.EndOfGameChecker;
import it.polimi.ingsw.server.enumerations.AssistantCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.enumerations.Tower;

import java.util.ArrayList;

/**
 * Represents a player server-side
 */
public class Player {
    private final String nickname;
    private final Tower hisTower;
    private int coins;
    private final ArrayList<AssistantCard> cards;
    private AssistantCard lastPlayedCard;
    private final Dashboard dashboard;
    private final boolean[][] coinPositions;
    private boolean ccActivatedThisTurn;

    /**
     * Player constructor, builds dashboard and 10 assistant cards
     * @param n nickname of the player
     * @param t tower colour of the player
     */
    public Player(String n, Tower t){
        coins = 1;
        nickname = n;
        hisTower = t;
        dashboard = new Dashboard(hisTower);
        lastPlayedCard = null;
        ccActivatedThisTurn = false;
        cards = new ArrayList<>();
        for (AssistantCardInfo i: AssistantCardInfo.values()){
            cards.add(new AssistantCard(i));
        }
        coinPositions = new boolean[Colour.values().length][3];
    }

    /**
     * it inserts the StudentGroup given by @param in the dashboard's entrance
     * @param s StudentGroup to add in the dashboard's entrance
     */
    public void fillDashboardEntrance(StudentGroup s){
        dashboard.fillEntrance(s);
    }

    /**
     * It removes from the Dashboard's entrance a student send to an Island
     * @param c the colour of the selected student
     */
    public void moveToIsland(Colour c, Island i)throws NoStudentsException {
        dashboard.removeFromEntrance(c);
        i.addStudent(c);
    }

    /**
     * It moves a student from the Dashboard's Entrance to the Dashboard's Hall
     * @param c the colour of the selected student
     */
    public void moveToHall(Colour c) throws NoStudentsException, FullHallException {
        dashboard.removeFromEntrance(c);
        dashboard.addToHall(c);
        checkCoins(c);
    }

    /**
     * every 3rd student of the selected colour added to to the hall, gives the player a coin
     * @param c colour of the students
     */
    private void checkCoins(Colour c) {
        int lastCoinIndex = dashboard.getHall().getQuantityColour(c)/3;
        if (lastCoinIndex>0) {
            if(!coinPositions[c.ordinal()][lastCoinIndex-1]) {
                coinPositions[c.ordinal()][lastCoinIndex-1] = true;
                addCoin();
            }
        }
    }

    public Tower getTower(){
        return hisTower;
    }

    /**
     * It adds a coin to the ones available to the Player
     */
    private void addCoin(){
        coins++;
    }

    /**
     * It removes a coin from the ones available to the Player
     * @param x the number of coins chosen to be spent
     */
    public void spendCoins(int x) throws IllegalArgumentException{
        if(coins<x) throw new IllegalArgumentException();
        coins -= x;
    }

    /**
     * It changes the status of the chosen card to "played", so it can't be reused again
     * @param x the played card's index
     */
    public void playCard(int x) throws AlreadyPlayedException{
        if(cards.get(x).isPlayed()) throw new AlreadyPlayedException();
        cards.get(x).setPlayed(true);
        lastPlayedCard = cards.get(x);
        for (AssistantCard c: cards) {
            if(!c.isPlayed()) return;
        }
        EndOfGameChecker.instance().setLastTurn(true);
    }

    public void setNumTowers(int num) {
        dashboard.setNumTowers(num);
    }

    //getters & setters
    public AssistantCard getLastPlayedCard() {
        return lastPlayedCard;
    }
    public Dashboard getDashboard() {
        return dashboard;
    }
    public int getCoins(){
        return coins;
    }
    public String getNickname() {
        return nickname;
    }
    //getter & setter fot tests
    public AssistantCard getAssistantCard(int x){
        return cards.get(x);
    }

    /**
     * The method fills the hall with the given StudentGroup
     * @param selectedStudentsFrom StudentGroup to add to the Hall
     * @throws FullHallException if the Hall is already full
     */
    public void fillHall(StudentGroup selectedStudentsFrom) throws FullHallException{
        for (Colour c: Colour.values()) {
            if(dashboard.getHall().getQuantityColour(c)+selectedStudentsFrom.getQuantityColour(c)>10) throw new FullHallException();
        }

        for (Colour c: Colour.values()) {
            for(int i=0; i<selectedStudentsFrom.getQuantityColour(c); i++) {
                try {
                    addToHall(c);
                } catch (FullHallException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addToHall(Colour selectedColour) throws FullHallException{
        dashboard.addToHall(selectedColour);
        checkCoins(selectedColour);
    }

    public boolean isCcActivatedThisTurn() {
        return ccActivatedThisTurn;
    }

    public void setCcActivatedThisTurn(boolean ccActivatedThisTurn) {
        this.ccActivatedThisTurn = ccActivatedThisTurn;
    }
}
