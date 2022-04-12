package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.players.Player;

public class CharacterCard {
    private CharacterCardsInfo cardInfo;
    private boolean coinOnIt;
    private int noEntryTiles;
    private StudentGroup selectedStudentsFrom;
    private StudentGroup selectedStudentsTo;
    private StudentGroup studentsOnCard;
    private Island selectedIsland;
    private boolean isActivated;
    private Colour selectedColour;
    private Player playerThisTurn;

    private Board board; //card effect 3 can't work otherwise
    private ExpertGameManager gameManager;

    /**
     *
     * @param info basic information of the card: file name, price, effect
     */
    public CharacterCard(CharacterCardsInfo info){
        cardInfo = info;
        coinOnIt = false;
        noEntryTiles = 0;
        isActivated = false;
    }

    /**
     * the ExpertGameManager must call it at the beginning of the game. EXPERT MODE ONLY
     */
    public void initializeCards(Bag b) {
        switch (cardInfo) {
            case CARD1:
            case CARD11:
                studentsOnCard = new StudentGroup(b.removeStudents(4));
                break;
            case CARD5:
                noEntryTiles = 4;
                studentsOnCard = new StudentGroup();
                break;
            case CARD7:
                studentsOnCard = new StudentGroup(b.removeStudents(6));
                break;
            default:
                studentsOnCard = new StudentGroup();
                break;
        }
    }

    //getter and setter methods
    public CharacterCardsInfo getCardInfo() {
        return cardInfo;
    }
    public void setCardInfo(CharacterCardsInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
    public boolean isCoinOnIt() {
        return coinOnIt;
    }
    public void setCoinOnIt(boolean coinOnIt) {
        this.coinOnIt = coinOnIt;
    }
    public int getNoEntryTiles() {
        return noEntryTiles;
    }
    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }
    public StudentGroup getSelectedStudentsFrom() {
        return selectedStudentsFrom;
    }
    public void setSelectedStudentsFrom(StudentGroup selectedStudentsFrom) { this.selectedStudentsFrom = selectedStudentsFrom; }
    public StudentGroup getSelectedStudentsTo() {
        return selectedStudentsTo;
    }
    public void setSelectedStudentsTo(StudentGroup selectedStudentsTo) {
        this.selectedStudentsTo = selectedStudentsTo;
    }
    public StudentGroup getStudentsOnCard() {
        return studentsOnCard;
    }
    public void setStudentsOnCard(StudentGroup studentsOnCard) {
        this.studentsOnCard = studentsOnCard;
    }
    public Island getSelectedIsland() {
        return selectedIsland;
    }
    public void setSelectedIsland(Island selectedIsland) {
        this.selectedIsland = selectedIsland;
    }
    public boolean isActivated() {
        return isActivated;
    }
    public void setActivated(boolean activated) {
        isActivated = activated;
    }
    public Colour getSelectedColour() {
        return selectedColour;
    }
    public void setSelectedColour(Colour selectedColour) {
        this.selectedColour = selectedColour;
    }
    public Player getPlayerThisTurn() {
        return playerThisTurn;
    }
    public void setPlayerThisTurn(Player playerThisTurn) {
        this.playerThisTurn = playerThisTurn;
    }

    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public ExpertGameManager getGameManager() {
        return gameManager;
    }
    public void setGameManager(ExpertGameManager gameManager) {
        this.gameManager = gameManager;
    }
}
