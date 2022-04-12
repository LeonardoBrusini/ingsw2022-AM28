package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.board.MotherNature;
import it.polimi.ingsw.model.players.AssistantCard;
import it.polimi.ingsw.model.players.Dashboard;
import java.util.ArrayList;

public class Player {
    private final String nickname;
    private Tower hisTower;
    private int coins;
    private ArrayList<AssistantCard> cards;
    private AssistantCard lastPlayedCard;
    private Dashboard dashboard;

    public Player(String n, Tower t){
        coins = 0;
        nickname = n;
        hisTower = t;
        dashboard = new Dashboard(hisTower);
        lastPlayedCard = null;
        cards = new ArrayList<>();
        for (AssistantCardInfo i: AssistantCardInfo.values()){
            cards.add(new AssistantCard(i));
        }
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
    public void moveToIsland(Colour c, Island i)throws IllegalArgumentException{
        dashboard.removeFromEntrance(c);
        i.addStudent(c);
    }

    /**
     * It moves a student from the Dashboard's Entrance to the Dashboard's Hall
     * @param c the colour of the selected student
     */
    public void moveToHall(Colour c) throws IllegalArgumentException{
        dashboard.addToHall(c);
    }

    /**
     * It builds a tower removing it from the Dashoboard
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
    public void spendCoins(int x){
        if(coins>0)
            coins -= x;
    }

    /**
     * It changes the status of the choosen card to "played", so it can't be reused again
     * @param x the played card's index
     */
    public void playCard(int x){
        //maybe we can add some controls
        cards.get(x).setPlayed(true);
        lastPlayedCard = cards.get(x);
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
}
