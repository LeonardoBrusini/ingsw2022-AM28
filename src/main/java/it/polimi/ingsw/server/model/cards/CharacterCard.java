package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.model.players.Player;
import it.polimi.ingsw.server.model.board.Bag;
import it.polimi.ingsw.server.model.board.Board;

public class CharacterCard {
    private final CharacterCardInfo cardInfo;
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
     * @param info basic information of the card: file name, price
     */
    public CharacterCard(CharacterCardInfo info){
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
            case CARD1, CARD11 -> studentsOnCard = new StudentGroup(b.removeStudents(4));
            case CARD5 -> {
                noEntryTiles = 4;
                studentsOnCard = new StudentGroup();
            }
            case CARD7 -> studentsOnCard = new StudentGroup(b.removeStudents(6));
            default -> studentsOnCard = new StudentGroup();
        }
    }

    /**
     * It shows the price of the CharacterCard, that could change during the Game
     * @return CharacterCard's price
     */
    public int getPrice() {
        int price = cardInfo.getPrice() + (coinOnIt ? 1 : 0);
        coinOnIt = true;
        return price;
    }

    //getter and setter methods
    public CharacterCardInfo getCardInfo() {
        return cardInfo;
    }
    public boolean isCoinOnIt() {
        return coinOnIt;
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
    public boolean getCoinOnIt(){return coinOnIt;}

    //not sure about this
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
