package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
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

    public Player(String n){
        coins = 0;
        nickname = n;
        //ONLY IF 2 PLAYERS
        dashboard = new Dashboard(8,hisTower);
    }

    public void fillDashboard(StudentGroup s){
        dashboard.fillEntrance(s);
    }
    public void moveToIsland(Colour c){
        dashboard.removeFromEntrance(c);
    }
    public void moveToHall(Colour c){
        dashboard.addToHall(c);
    }

    //??
    public Tower getTower(){
        this.dashboard.buildTower();
        return this.hisTower;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }
    public int getCoins(){
        return coins;
    }
    public void addCoin(){
        coins++;
    }
    public void spendCoins(int x){
        coins -= x;
    }
    public void playCard(int x){
        //maybe we can add some controls
        cards.get(x).setPlayed();
    }
    public AssistantCard getLastPlayedCard() {
        return lastPlayedCard;
    }
}
