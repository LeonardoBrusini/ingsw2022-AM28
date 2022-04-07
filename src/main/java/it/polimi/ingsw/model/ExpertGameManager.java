package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ExpertGameManager {
    private final ArrayList<Player> players = new ArrayList<>();
    private int numPlayers;
    //private Board board;
    //private static ExpertGameManager instance;
    public Board board;


    public ExpertGameManager() {
        this.numPlayers = 0;
    }

    /*public static ExpertGameManager instance() {
        if(instance == null) {
            instance = new ExpertGameManager();
        }
        return instance;
    }*/

    public void newGame(int numPlayers){
       this.numPlayers = numPlayers;
       this.board = new Board(this.numPlayers);
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void checkProfessors(){
        for(Colour c:Colour.values()) {
            int valmax = players.get(0).getDashboard().getNumStudentsHall(c);
            int tmp;
            int posmax=0;
            for (int i = 1; i < players.size(); i++) {
                tmp =players.get(i).getDashboard().getNumStudentsHall(c);
                if(tmp>valmax)
                    posmax=i;
            }
            Tower towermax = this.players.get(posmax).getTower();
            this.board.assignProfessor(c,towermax);
        }

    }


}
