package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.enumerations.AssistantCardInfo;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import it.polimi.ingsw.server.model.players.Player;

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
        } catch (NoStudentsException | FullHallException e) {
            //what happens?
        }
        checkProfessors(c);
        turnManager.nextPhase(board,players);
    }

    /**
     * checks which player gets the professor of colour c (modifies behaviour if CARD2 is activated)
     * @param c the colour of the professor
     */
    public void checkProfessors(Colour c) {
        int maxStudents = 0;
        Player maxStudentsPlayer = null;
        for(Player p: players) {
            if(p.getDashboard().getHall().getQuantityColour(c)>maxStudents) {
                maxStudents = p.getDashboard().getHall().getQuantityColour(c);
                maxStudentsPlayer = p;
            } else if (p.getDashboard().getHall().getQuantityColour(c) == maxStudents) {
                //look for CARD2
                CharacterCard card2 = null;
                for(CharacterCard card : board.getCharacterCards()) {
                    if(card.getCardInfo()==CharacterCardInfo.CARD2) {
                        card2 = card;
                    }
                }
                if(card2!=null && card2.isActivated()) {
                    if(p == card2.getPlayerThisTurn()) maxStudentsPlayer = p;
                } else  {
                    maxStudentsPlayer = null;
                }
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
        CharacterCard card = null;
        Archipelago a = board.getIslandManager().getArchipelagoByIslandIndex(board.getMotherNature().getIslandIndex());
        if(a.getNoEntryTiles()>0) {
            for (CharacterCard c: board.getCharacterCards()) {
                if (c.getCardInfo() == CharacterCardInfo.CARD5) c.setNoEntryTiles(c.getNoEntryTiles()+1);
            }
            a.setNoEntryTiles(a.getNoEntryTiles()-1);
        }
        Player p = board.getMotherNature().playerWithMostInfluence(players,board.getIslandManager(),board.getProfessorGroup(),board.getCharacterCards());
        if(p!=null) {
            for(Island i: a.getIslands()) {
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
        players.get(playerIndex).fillDashboardEntrance(sg);
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

    /**
     * method used to play one of the following cards: CARD2, CARD4, CARD6, CARD8
     * @param index player index
     * @param posCharacterCard character card index
     */
    public void playCharacterCard(int index, int posCharacterCard){
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3) return;
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMoveStudentsPhase()) return;

        Player p = players.get(index);
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);
        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard);
            card.setPlayerThisTurn(p);
            card.setGameManager(this);
            card.setBoard(board);
            card.getCardInfo().getEffect().resolveEffect(card);
        } catch (IllegalArgumentException exception) {
            //error, player does not have enough coins
        }
    }

    /**
     * method used to play one of the following cards: CARD9, CARD11, CARD12
     * @param index player index
     * @param posCharacterCard character card index
     * @param colour colour of the student
     */
    public void playCharacterCard(int index, int posCharacterCard, Colour colour){
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || colour==null) return;
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMoveStudentsPhase()) return;

        Player p = players.get(index);
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);

        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard, colour);
            card.setPlayerThisTurn(p);
            card.setGameManager(this);
            card.setBoard(board);
            card.setSelectedColour(colour);
            card.getCardInfo().getEffect().resolveEffect(card);
        } catch (IllegalArgumentException exception) {
            //error, player does not have enough coins
        }
    }

    /**
     * method used to play one of the following cards: CARD1
     * @param index player index
     * @param posCharacterCard character card index
     * @param colour colour of the student
     * @param islandIndex index of the island
     */
    public void playCharacterCard(int index, int posCharacterCard, Colour colour, int  islandIndex){
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || colour==null || islandIndex<1 || islandIndex>12) return;
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMoveStudentsPhase()) return;
        Player p = players.get(index);
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);

        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard,colour,islandIndex);
            card.setPlayerThisTurn(p);
            card.setBoard(board);
            card.setGameManager(this);
            card.setSelectedColour(colour);
            card.setSelectedIsland(board.getIslandManager().getIslandByIndex(islandIndex));
            card.getCardInfo().getEffect().resolveEffect(card);
        } catch (IllegalArgumentException exception) {
            //error, player does not have enough coins
        }
    }

    /**
     * method used to play one of the following cards: CARD3, CARD5
     * @param index player index
     * @param posCharacterCard character card index
     * @param islandIndex index of the island
     */
    public void playCharacterCard(int index, int posCharacterCard,  int  islandIndex){
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || islandIndex<1 || islandIndex>12) return;
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMoveStudentsPhase()) return;
        Player p = players.get(index);
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);
        if(card.getCardInfo()==CharacterCardInfo.CARD5 && card.getNoEntryTiles()==0) return;

        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard,islandIndex);
            card.setGameManager(this);
            card.setPlayerThisTurn(p);
            card.setBoard(board);
            card.setSelectedIsland(board.getIslandManager().getIslandByIndex(islandIndex));
            card.getCardInfo().getEffect().resolveEffect(card);
        } catch (IllegalArgumentException exception) {
            //error, player does not have enough coins
        }
    }

    /**
     * method used to play one of the following cards: CARD7, CARD10
     * @param index player index
     * @param posCharacterCard character card index
     * @param studentGroupFrom first group of students
     * @param studentGroupTo second group of students
     */
    public void playCharacterCard(int index, int posCharacterCard, StudentGroup studentGroupFrom, StudentGroup studentGroupTo){
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || studentGroupFrom==null || studentGroupTo==null || studentGroupFrom.getTotalStudents()!=studentGroupTo.getTotalStudents()) return;
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMoveStudentsPhase()) return;
        Player p = players.get(index);
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);
        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard,studentGroupFrom,studentGroupTo);
            card.setGameManager(this);
            card.setPlayerThisTurn(p);
            card.setBoard(board);
            card.setSelectedStudentsFrom(studentGroupFrom);
            card.setSelectedStudentsTo(studentGroupTo);
            card.getCardInfo().getEffect().resolveEffect(card);
        } catch (IllegalArgumentException exception) {
            //error, player does not have enough coins
        }
    }
}