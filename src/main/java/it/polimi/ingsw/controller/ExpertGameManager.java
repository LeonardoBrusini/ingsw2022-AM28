package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CharacterCardInfo;
import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.exceptions.NoStudentsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.enumerations.AssistantCardInfo;
import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class ExpertGameManager {
    private ArrayList<Player> players;
    private Board board;
    private TurnManager turnManager;

    public ExpertGameManager() {
        players = new ArrayList<>();
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
     * initializes the board, which objects on it have attributes which are initialized based on the number of players
     */
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
       turnManager = new TurnManager(players.size());
       // to be continued
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
        if(p<0 || p>=players.size() || c<0 || c>=AssistantCardInfo.values().length) return;
        if(turnManager.getPhase()!=Phase.PLANNING || turnManager.getCurrentPlayer()!=p) return; //not the correct phase
        try {
            players.get(p).playCard(c);
        } catch (AlreadyPlayedException e) {
            //what happens?
        }
        turnManager.nextPhase(board,players);
    }

    /**
     * the selected player moves a student from the entrance to the hall, then checks for the professor
     * @param p the player who wants to move a student
     * @param c the colour of the student
     */
    public void moveStudentsToHall(int p, Colour c){
        if (p<0 || p>=players.size() || c==null) return;
        if(turnManager.getPhase()!=Phase.ACTION || turnManager.getCurrentPlayer()!=p || turnManager.isMoveStudentsPhase()) return;
        try {
            players.get(p).moveToHall(c);
        } catch (IllegalArgumentException e) {
            //what happens?
        }
        checkProfessors(c);
        turnManager.nextPhase(board,players);
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
        //must check if CharacterCard 2 is activated, modify behaviour if so
    }

    /**
     * the selected player moves a student from the entrance to the selected island
     * @param p the player who wants to move a student
     * @param c the colour of the student
     * @param is index of the island
     */
    public void moveStudentToIsland(int p, Colour c, int is){
        if(p<0 || p>=players.size() || c==null || is<1 || is>12) return;
        if(turnManager.getPhase()!=Phase.ACTION || turnManager.getCurrentPlayer()!=p || turnManager.isMoveStudentsPhase()) return;
        try{
            players.get(p).moveToIsland(c,board.getIslandManager().getIslandByIndex(is));
        } catch (NoStudentsException e) {
            //what happens?
        }
        turnManager.nextPhase(board,players);
    }

    /**
     * this method must be activated when the player asks to move mother nature
     * after being moved, it checks the player with most influence on the archipelago mother nature is on and eventually changes the towers
     * and checks for aggregation.
     * @param moves the number of archipelagos mother nature has to move forward
     */
    public void moveMotherNature(int moves){
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMotherNaturePhase()) return;
        if(moves<1 || moves>players.get(turnManager.getCurrentPlayer()).getLastPlayedCard().getInfo().getMotherNatureShifts()) return;
        board.moveMotherNature(moves);
        checkInfluence(); //check if this method works properly
        turnManager.nextPhase(board,players);
        manageEndOfGame();
    }

    /**
     * checks the player with most influence on the archipelago and build towers on it if needed
     */
    public void checkInfluence() {
        //modify behaviour if noEntryTiles is on the archipelago, nothing happens and the noEntryTile goes back to CARD5
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
        if(cloudIndex<0 || cloudIndex>=players.size() || playerIndex<0 || playerIndex>=players.size()) return;
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isCloudSelectionPhase()) return;
        StudentGroup sg = board.getClouds().get(cloudIndex).clearStudents();
        players.get(playerIndex).fillDashboard(sg);
        turnManager.nextPhase(board,players);
        manageEndOfGame();
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

    private void manageEndOfGame() {
        int winnerIndex;
        if(EndOfGameChecker.instance().isEndOfGame()) {
            winnerIndex = EndOfGameChecker.instance().getWinner();
            if(winnerIndex==-1) {
                //manage draw
            } else {
                //manage winner
            }
        }
    }

    //getters & setters methods
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
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

    public void playCharacterCard(int index, int posCharacterCard){
        Player p = this.players.get(index);

        CharacterCardInfo[] e = CharacterCardInfo.values();

        p.spendCoins(e[posCharacterCard].getPrice());

        board.playCharacterCard(posCharacterCard);

    }

    public void playCharacterCard(int index, int posCharacterCard, Colour colour){
        Player p = this.players.get(index);

        CharacterCardInfo[] e = CharacterCardInfo.values();

        p.spendCoins(e[posCharacterCard].getPrice());

        board.playCharacterCard(posCharacterCard,colour);
    }


    public void playCharacterCard(int index, int posCharacterCard, Colour colour, int  noEntryTiles){
        Player p = this.players.get(index);

        CharacterCardInfo[] e = CharacterCardInfo.values();

        p.spendCoins(e[posCharacterCard].getPrice());

        board.playCharacterCard(posCharacterCard,colour,noEntryTiles);
    }

    public void playCharacterCard(int index, int posCharacterCard,  int  noEntryTiles){
        Player p = this.players.get(index);

        CharacterCardInfo[] e = CharacterCardInfo.values();

        p.spendCoins(e[posCharacterCard].getPrice());

        board.playCharacterCard(posCharacterCard,noEntryTiles);
    }




    public void playCharacterCard(int index, int posCharacterCard, StudentGroup studentGroupFrom, StudentGroup studentGroupTo){
        Player p = this.players.get(index);

        CharacterCardInfo[] e = CharacterCardInfo.values();

        p.spendCoins(e[posCharacterCard].getPrice());

        board.playCharacterCard(posCharacterCard,studentGroupFrom ,studentGroupTo);
    }

}
