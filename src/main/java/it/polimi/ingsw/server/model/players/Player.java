package it.polimi.ingsw.server.model.players;

import it.polimi.ingsw.server.controller.EndOfGameChecker;
import it.polimi.ingsw.server.enumerations.AssistantCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.enumerations.Tower;

import java.util.ArrayList;

/**
 * Represents a player server-side
 */
public class Player {
    private String nickname;
    private final Tower hisTower;
    private int coins;
    private final ArrayList<AssistantCard> cards;
    private AssistantCard lastPlayedCard;
    private final Dashboard dashboard;
    private final boolean[][] coinPositions;
    private boolean ccActivatedThisTurn;
    private boolean acActivatedThisTurn;
    private boolean connected;

    /**
     * Player constructor, builds dashboard and 10 assistant cards
     * @param t tower colour of the player
     */
    public Player(Tower t){
        coins = 1;
        hisTower = t;
        dashboard = new Dashboard(hisTower);
        lastPlayedCard = null;
        ccActivatedThisTurn = false;
        acActivatedThisTurn = false;
        cards = new ArrayList<>();
        for (int i=0; i<AssistantCardInfo.values().length;i++){
            cards.add(new AssistantCard(AssistantCardInfo.values()[i]));
        }
        coinPositions = new boolean[Colour.values().length][3];
        connected = true;
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
     *
     * @param board
     * @param c     the colour of the selected student
     */
    public void moveToHall(Board board, Colour c) throws NoStudentsException, FullHallException {
        dashboard.removeFromEntrance(c);
        dashboard.addToHall(c);
        checkCoins(board, c);
    }

    /**
     * every 3rd student of the selected colour added to the hall, gives the player a coin
     *
     * @param board
     * @param c     colour of the students
     */
    private void checkCoins(Board board, Colour c) {
        int lastCoinIndex = dashboard.getHall().getQuantityColour(c)/3;
        if (lastCoinIndex>0) {
            if(!coinPositions[c.ordinal()][lastCoinIndex-1]) {
                coinPositions[c.ordinal()][lastCoinIndex-1] = true;
                if(board.getCoins()>0) {
                    addCoin();
                    board.setCoins(board.getCoins()-1);
                }
            }
        }
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
    public void spendCoins(int x) throws NotEnoughCoinsException{
        if(coins<x) throw new NotEnoughCoinsException();
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
        acActivatedThisTurn = true;
        for (AssistantCard c: cards) {
            if(!c.isPlayed()) return;
        }
        EndOfGameChecker.instance().setLastTurn(true);
    }

    /**
     * The method fills the hall with the given StudentGroup
     * @param selectedStudentsFrom StudentGroup to add to the Hall
     * @throws FullHallException if the Hall is already full
     */
    public void fillHall(Board board, StudentGroup selectedStudentsFrom) throws FullHallException{
        for (Colour c: Colour.values()) {
            if(dashboard.getHall().getQuantityColour(c)+selectedStudentsFrom.getQuantityColour(c)>10) throw new FullHallException();
        }

        for (Colour c: Colour.values()) {
            for(int i=0; i<selectedStudentsFrom.getQuantityColour(c); i++) {
                try {
                    addToHall(board, c);
                } catch (FullHallException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * adds a single student to the dashboard's hall
     *
     * @param board
     * @param selectedColour colour of the student
     * @throws FullHallException if there are already 10 students of the selected colour in the hall
     */
    public void addToHall(Board board, Colour selectedColour) throws FullHallException{
        dashboard.addToHall(selectedColour);
        checkCoins(board, selectedColour);
    }
    public void setNumTowers(int num) {
        dashboard.setNumTowers(num);
    }

    //getters & setters
    public Tower getTower(){
        return hisTower;
    }
    public AssistantCard getLastPlayedCard() {
        return lastPlayedCard;
    }
    public Dashboard getDashboard() {
        return dashboard;
    }
    public int getCoins(){
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getNickname() {
        return nickname;
    }
    //getter & setter fot tests
    public AssistantCard getAssistantCard(int x){
        return cards.get(x);
    }
    public boolean isCcActivatedThisTurn() {
        return ccActivatedThisTurn;
    }
    public void setCcActivatedThisTurn(boolean ccActivatedThisTurn) {
        this.ccActivatedThisTurn = ccActivatedThisTurn;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    public boolean isConnected() {
        return connected;
    }
    public ArrayList<AssistantCard> getCards() {
        return cards;
    }
    public void setLastPlayedCard(AssistantCard lastPlayedCard) {
        this.lastPlayedCard = lastPlayedCard;
    }
    public boolean isAcActivatedThisTurn() {
        return acActivatedThisTurn;
    }
    public void setAcActivatedThisTurn(boolean acActivatedThisTurn) {
        this.acActivatedThisTurn = acActivatedThisTurn;
    }
}
