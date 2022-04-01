package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ExpertGameManager {
    private ArrayList<Player> players = new ArrayList<>();
    private int numPlayers;
    private Board board;
    private static ExpertGameManager instance;


    public ExpertGameManager() {

    }

    public static ExpertGameManager instance() {
        if(instance == null) {
            instance = new ExpertGameManager();
        }
        return instance;
    }

    public void newGame(int numPlayers){
       this.numPlayers = numPlayers;
       this.board.reset();
       // to be continued
    }
    public void addPlayer(String s){
        if(players.size()<numPlayers)
            this.players.add(new Player(s));
    }
    public int getNumPlayers() {
        return players.size();
    }

    public void playAssistantCard(int p, int c){
        this.players.get(p).playCard(c);
    }

    public void moveStudentsToHall(int p, Colour c){
        this.players.get(p).moveToHall(c);
    }
    public void moveStudentToIsland(int p, Colour c, int is){
        this.players.get(p).moveToIsland(c);
        this.board.getIslandManager().getIsland(is).addStudent(c);
    }

    public void moveMotherNature(int moves){
        int pos = moves + this.board.getMotherNature().getIslandIndex();
        if(pos < 12) {
            this.board.getMotherNature().setIsland(pos);
        }else{
            this.board.getMotherNature().setIsland(pos%12);
        }
    }

}
