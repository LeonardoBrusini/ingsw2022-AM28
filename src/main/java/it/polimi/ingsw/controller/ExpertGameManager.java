package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class ExpertGameManager {
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Integer> playerOrder;
    private Board board;

    public ExpertGameManager() {

    }


    public void newGame(){
       board = new Board(players.size());
       // to be continued
    }

    /**
     * adds a new player if the lobby is not full (no more than 3 players)
     * @param s the nickname of the player
     */
    public void addPlayer(String s){
        if(players.size()<3) players.add(new Player(s));
    }

    /**
     * returns the number of player of the lobby
     */
    public int getNumPlayers() {
        return players.size();
    }


    public void playAssistantCard(int p, int c){
        players.get(p).playCard(c);
    }

    public void moveStudentsToHall(int p, Colour c){
        players.get(p).moveToHall(c);
    }
    public void moveStudentToIsland(int p, Colour c, int is){
        players.get(p).moveToIsland(c);
        board.getIslandManager().getIsland(is).addStudent(c);
    }

    /**
     * this method must be activated when the player asks to move mother nature
     * after being moved, it checks the player with most influence on the archipelago mother nature is on and eventually changes the towers
     * and checks for aggregation.
     * @param moves
     */
    public void moveMotherNature(int moves){
        //mother nature moves
        int pos = moves + board.getMotherNature().getIslandIndex();
        if(pos > 12) {
            board.getMotherNature().setIsland(pos%12);
        }else{
            board.getMotherNature().setIsland(pos);
        }
        checkInfluence();
    }

    public void checkInfluence() {
        Player p = board.getMotherNature().playerWithMostInfluence(players,board.getIslandManager(),board.getProfessorGroup());
        if(p!=null) {
            for(Island i: board.getIslandManager().getArchipelagoByIslandIndex(board.getMotherNature().getIslandIndex()).getIslands()) {
                if(i.getTower()==null){
                    p.getDashboard().buildTower();
                    i.setTower(p.getTower());
                } else if(i.getTower()!=p.getTower()) {
                    Player opponent = findPlayerByTower(i.getTower());
                    opponent.getDashboard().addTower();
                    p.getDashboard().buildTower();
                    i.setTower(p.getTower());
                } else {
                    //player already has his towers on the archipelago
                }
            }
        } else {
            //nothing happens, to next player action turn
        }
        //aggregation to do
    }

    /**
     *
     * @param t the colour of the tower
     * @return the player who has that tower colour
     */
    private Player findPlayerByTower(Tower t) {
        for (Player p: players){
            if(p.getTower()==t) return p;
        }
        return null;
    }
    //public void PlayCharacterCard(int)

    //getters & setters methods
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Integer> getPlayerOrder() {
        return playerOrder;
    }

    public void setPlayerOrder(ArrayList<Integer> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

}
