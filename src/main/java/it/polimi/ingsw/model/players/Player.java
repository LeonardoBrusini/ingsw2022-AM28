package it.polimi.ingsw.model.players;

import it.polimi.ingsw.controller.EndOfGameChecker;
import it.polimi.ingsw.enumerations.AssistantCardInfo;
import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.exceptions.FullHallException;
import it.polimi.ingsw.exceptions.NoStudentsException;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.board.Island;
import java.util.ArrayList;

/**
 * Represents a player server-side
 */
public class Player {
    private final String nickname;
    private Tower hisTower;
    private int coins;
    private ArrayList<AssistantCard> cards;
    private AssistantCard lastPlayedCard;
    private Dashboard dashboard;
    private boolean[][] coinPositions;

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
    public void fillDashboard(StudentGroup s){
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
    public void moveToHall(Colour c) throws IllegalArgumentException, FullHallException {
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

    /**
     * It builds a tower removing it from the Dashboard
     * @return the colour of the Player's Tower
     */
    public Tower getTower(){
        return hisTower;
    }

    /**
     * It adds a coin between the ones available to the Player
     */
    public void addCoin(){
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
    public void playCard(int x) {
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

    public void fillHall(StudentGroup selectedStudentsFrom) throws FullHallException{
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
}
