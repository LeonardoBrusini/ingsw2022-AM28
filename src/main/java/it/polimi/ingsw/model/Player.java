package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private Tower hisTower;
    private int coins = 0;
    private ArrayList<AssistantCard> cards;
    private AssistantCard lastPlayedCard;
    private Dashboard dashboard;

    public Player(String n){
        this.nickname = n;
        this.dashboard = new Dashboard(8,hisTower);
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
        cards.get(x).setPlayed();
    }

    public AssistantCard getLastPlayedCard() {
        return lastPlayedCard;
    }
}
