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
        this.dashboard = new Dashboard();
    }
    public void fillDashboard(StudentGroup s){
        this.dashboard.fillEntrance(s);
    }

    public void moveToIsland(Colour c, int x){
        this.dashboard.removeFromEntrance(c);
    }
    public void moveToHall(Colour c){
        this.dashboard.addToHall(c);
    }
    public Tower getTower(){
        this.dashboard.buildTower();
        return this.hisTower;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public int getCoins(){
        return this.coins;
    }
    public void addCoin(){
        this.coins++;
    }
    public void spendCoins(int x){
        this.coins = this.coins - x;
    }
    public void playCard(int x){
        this.cards.get(x).setPlayed();
    }
}
