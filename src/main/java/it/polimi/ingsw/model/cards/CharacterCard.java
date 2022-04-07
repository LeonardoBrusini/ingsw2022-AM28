package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.players.Player;

public class CharacterCard {
    private String fileName;
    private int price;
    private boolean coinOnIt;
    private int noEntryTiles;
    private StudentGroup selectedStudentsFrom;
    private StudentGroup selectedStudentsTo;
    private StudentGroup studentsOnCard;
    private Island selectedIsland;
    private boolean isActivated;
    private Colour selectedColour;
    private Player playerThisTurn;
    private EffectStrategy effect;

    private Board board; //card effect 3 can't work otherwise
    private ExpertGameManager gameManager;
    
    /**
     * Card constructor.
     * @param s the name of the image of the card
     * @param e the effect strategy of the card
     * @param p the cost of the card
     */
    public CharacterCard(String s,EffectStrategy e,int p){
        effect = e;
        fileName = s;
        price = p;
        coinOnIt = false;
        noEntryTiles = 0;
        isActivated = false;
    }

    /**
     * the ExpertGameManager must call it at the beginning of the game. EXPERT MODE ONLY
     */
    public void initializeCards(Bag b) {
        switch (fileName) {
            case "P01.jpg":
            case "P11.jpg":
                studentsOnCard = new StudentGroup(b.removeStudents(4));
                break;
            case "P05.jpg":
                noEntryTiles = 4;
                break;
            case "P07.jpg":
                studentsOnCard = new StudentGroup(b.removeStudents(6));
                break;
        }
    }

    //getter and setter methods
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
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
    public EffectStrategy getEffect() {
        return effect;
    }
    public void setEffect(EffectStrategy effect) {
        this.effect = effect;
    }
    public String getFileName() {
        return fileName;
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
