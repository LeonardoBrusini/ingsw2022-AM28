package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpertGameManager {
    private ArrayList<Player> players;
    private ArrayList<Integer> playerOrder;
    private Board board;

    public ExpertGameManager() {
        players = new ArrayList<>();
    }


    public void newGame(){
       board = new Board(players.size());
       if(players.size()==2) {
           for (Player p: players) {
               p.getDashboard().setNumTowers(8);
           }
       } else if (players.size()==3) {
           for (Player p: players) {
               p.getDashboard().setNumTowers(6);
           }
       }
       // to be continued
    }

    /**
     * adds a new player if the lobby is not full (no more than 3 players)
     * @param s the nickname of the player
     */
    public void addPlayer(String s){
        if(players.size()<3) {
            players.add(new Player(s, Tower.values()[players.size()]));
        }
    }

    /**
     * returns the number of player of the lobby
     */
    public int getNumPlayers() {
        return players.size();
    }

    /**
     * the selected player plays an assistant card
     * @param p the player who wants to play a card
     * @param c index of the card the player wants to play
     */
    public void playAssistantCard(int p, int c){
        players.get(p).playCard(c);
        //parameter controls, player who plays card must be the current one in TurnManager and must be in planning phase
    }

    /**
     * the selected player moves a student from the entrance to the hall, then checks for the professor
     * @param p the player who wants to move a student
     * @param c the colour of the student
     */
    public void moveStudentsToHall(int p, Colour c){
        players.get(p).moveToHall(c);
        //parameter controls, player who moves the student must be the current one in TurnManager, and must be in action phase
        checkProfessors(c);
    }

    /**
     * checks which player gets the professor of colour c
     * @param c the colour of the professor
     */
    private void checkProfessors(Colour c) {
        int maxStudents = 0;
        Player maxStudentsPlayer = null;
        for(Player p: players) {
            if(p.getDashboard().getHall().getQuantityColour(c)>maxStudents) {
                maxStudents = p.getDashboard().getHall().getQuantityColour(c);
                maxStudentsPlayer = p;
            } else if (p.getDashboard().getHall().getQuantityColour(c) == maxStudents) {
                maxStudentsPlayer = null;
            }
        }
        if(maxStudentsPlayer!=null) {
            board.assignProfessor(c,maxStudentsPlayer.getTower());
        }
    }

    /**
     * the selected player moves a student from the entrance to the selected island
     * @param p the player who wants to move a student
     * @param c the colour of the student
     * @param is index of the island
     */
    public void moveStudentToIsland(int p, Colour c, int is){
        players.get(p).moveToIsland(c,board.getIslandManager().getIsland(is));
        //parameter controls, player who moves the student must be the current one in TurnManager, and must be in action phase
    }

    /**
     * this method must be activated when the player asks to move mother nature
     * after being moved, it checks the player with most influence on the archipelago mother nature is on and eventually changes the towers
     * and checks for aggregation.
     * @param moves the number of archipelagos mother nature has to move forward
     */
    public void moveMotherNature(int moves){
        //mother nature moves
        board.moveMotherNature(moves);
        checkInfluence();
    }

    /**
     * checks the player with most influence on the archipelago and build towers on it if needed
     */
    public void checkInfluence() {
        Player p = board.getMotherNature().playerWithMostInfluence(players,board.getIslandManager(),board.getProfessorGroup());
        if(p!=null) {
            for(Island i: board.getIslandManager().getArchipelagoByIslandIndex(board.getMotherNature().getIslandIndex()).getIslands()) {
                if(i.getTower()==null){
                    p.getDashboard().buildTower();
                    board.getIslandManager().setTowerOnIsland(p.getTower(),i.getIslandIndex());
                } else if(i.getTower()!=p.getTower()) {
                    Player opponent = findPlayerByTower(i.getTower());
                    opponent.getDashboard().addTower(); //may produce NullPointerException
                    p.getDashboard().buildTower();
                    board.getIslandManager().setTowerOnIsland(p.getTower(),i.getIslandIndex());
                }
            }
        }
    }

    /**
     * takes the students from a cloud and puts them on the entrance of the player's dashboard
     * @param cloudIndex index of the cloud the player selected
     * @param playerIndex player who asked to take the students
     */
    public void takeStudentsFromCloud(int cloudIndex, int playerIndex) {
        //must check if the player is the one of the current turn and if cloud index is correct
        StudentGroup sg = board.getClouds().get(cloudIndex).clearStudents();
        players.get(playerIndex).fillDashboard(sg);
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
