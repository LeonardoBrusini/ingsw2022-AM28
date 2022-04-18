package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.cards.FactoryCharacterCards;

import java.util.ArrayList;

public class Board {
    private int coins;
    private ArrayList<Cloud> clouds;
    private IslandManager islandManager;
    private MotherNature motherNature;
    private Bag bag;
    private ProfessorGroup professorGroup;
    private ArrayList<CharacterCard> characterCards;

    /**
     * Board constructor. Initialize coins, clouds, motherNature, islands (via IslandManager), bag and professorGroup
     */
    public Board(int numPlayers) {
        coins = 20-numPlayers;
        clouds = new ArrayList<>();
        for(int i=0; i<numPlayers; i++){
            clouds.add(new Cloud());
        }
        motherNature = new MotherNature();
        islandManager = new IslandManager(motherNature.getIslandIndex());
        professorGroup = new ProfessorGroup();
        bag = new Bag();
        bag.initializeIslands(motherNature.getIslandIndex(), islandManager);
        characterCards = FactoryCharacterCards.getCards(bag);
    }

    /**
     * fills the clouds with n students where n is the number of players + 1
     */
    public void fillClouds(){
        for(Cloud c : clouds){
            c.addGroup(new StudentGroup(bag.removeStudents(clouds.size()+1)));
        }
    }

    /**
     * assign a professor to a player (by the colour of his tower)
     * @param c the colour of the professor
     * @param t the tower of the player we want to assign the professor
     */
    public void assignProfessor(Colour c, Tower t) throws IllegalArgumentException{
        if(c==null || t==null) throw new IllegalArgumentException();
        professorGroup.setTower(c,t);
    }

    /**
     * moves MotherNature to the position given by ExpertGameManager
     * @param shifts position where to move MotherNature
     */
    public void moveMotherNature(int shifts){
        Archipelago old = islandManager.getArchipelagoByIslandIndex(motherNature.getIslandIndex());
        old.setPresenceMotherNature(false);
        motherNature.setIsland((motherNature.getIslandIndex()+shifts>12 ? (motherNature.getIslandIndex()+shifts)%12 : motherNature.getIslandIndex()+shifts));
        Archipelago nw = islandManager.getArchipelagoByIslandIndex(motherNature.getIslandIndex());
        nw.setPresenceMotherNature(true);
        //game manager must check the player with most influence and build tower if needed (in upper method)
    }

    //getters & setters for testing
    public IslandManager getIslandManager() {
        return islandManager;
    }
    public MotherNature getMotherNature(){
        return motherNature;
    }
    public Bag getBag() {
        return bag;
    }
    public int getCoins() {
        return coins;
    }
    public void setCoins(int coins) {
        this.coins = coins;
    }
    public ArrayList<Cloud> getClouds() {
        return clouds;
    }
    public void setClouds(ArrayList<Cloud> clouds) {
        this.clouds = clouds;
    }
    public void setIslandManager(IslandManager islandManager) {
        this.islandManager = islandManager;
    }
    public void setMotherNature(MotherNature motherNature) {
        this.motherNature = motherNature;
    }
    public void setBag(Bag bag) {
        this.bag = bag;
    }
    public ProfessorGroup getProfessorGroup() {
        return professorGroup;
    }
    public void setProfessorGroup(ProfessorGroup professorGroup) {
        this.professorGroup = professorGroup;
    }
    public ArrayList<CharacterCard> getCharacterCards() {
        return characterCards;
    }
    public void setCharacterCards(ArrayList<CharacterCard> characterCards) {
        this.characterCards = characterCards;
    }


    /*public void playCharacterCard(int posCharacterCard){
        CharacterCardInfo[] e = CharacterCardInfo.values();
        e[posCharacterCard].getEffect().resolveEffect(characterCards.get(posCharacterCard));
    }

    public void playCharacterCard( int posCharacterCard, Colour colour){
        CharacterCardInfo[] e = CharacterCardInfo.values();

        characterCards.get(posCharacterCard).setSelectedColour(colour);

        e[posCharacterCard].getEffect().resolveEffect(characterCards.get(posCharacterCard));
    }

    public void playCharacterCard(int posCharacterCard, Colour colour, int noEntryTiles){
        CharacterCardInfo[] e = CharacterCardInfo.values();

        characterCards.get(posCharacterCard).setSelectedColour(colour);
        characterCards.get(posCharacterCard).setNoEntryTiles(noEntryTiles);

        e[posCharacterCard].getEffect().resolveEffect(characterCards.get(posCharacterCard));
    }

    public void playCharacterCard(int posCharacterCard, int noEntryTiles){
        CharacterCardInfo[] e = CharacterCardInfo.values();

        characterCards.get(posCharacterCard).setNoEntryTiles(noEntryTiles);

        e[posCharacterCard].getEffect().resolveEffect(characterCards.get(posCharacterCard));
    }

    public void playCharacterCard(int posCharacterCard, StudentGroup studentGroupFrom , StudentGroup studentGroupTo){
        CharacterCardInfo[] e = CharacterCardInfo.values();

        characterCards.get(posCharacterCard).setSelectedStudentsFrom(studentGroupFrom);
        characterCards.get(posCharacterCard).setSelectedStudentsFrom(studentGroupTo);

        e[posCharacterCard].getEffect().resolveEffect(characterCards.get(posCharacterCard));

    }*/

}
