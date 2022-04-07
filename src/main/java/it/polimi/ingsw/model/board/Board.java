package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CharacterCard;
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
    public Board(int numPlayers){
        coins = 20;
        clouds = new ArrayList<>();
        for(int i=0; i<numPlayers; i++){
            clouds.add(new Cloud());
        }
        motherNature = new MotherNature();
        islandManager = new IslandManager(motherNature.getIslandIndex());
        professorGroup = new ProfessorGroup();
        bag = new Bag();
        bag.initializeIslands(motherNature.getIslandIndex(), islandManager);
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
     * removes the students from the clouds and returns them
     * @param pos the index of the cloud we want to take the students from
     * @return a StudentGroup of the students who were in the clouds
     */
    public StudentGroup takeStudentsFromCloud(int pos){
        return clouds.get(pos).clearStudents();
    }

    /**
     * assign a professor to a player (by the colour of his tower)
     * @param c the colour of the professor
     * @param t the tower of the player we want to assign the professor
     */
    public void assignProfessor(Colour c, Tower t) {
        professorGroup.setTower(c,t);
    }

    //is it better to reset the attributes or just creating new objects?
    /*public void reset(){
        bag = new Bag();

        for(Cloud c: clouds) {
            c.clearStudents();
            c.addGroup(new StudentGroup(bag.removeStudents(4)));
        }

        for(Colour c: Colour.values()){
            professorGroup.setTower(c,null);
        }
        for(int i = 0; i <= 12; i++)
            islandManager.getIsland(i).clearStudents();
        bag.initializeIslands();
        Random generator;
        generator = new Random();
        int pos = generator.nextInt(12);
        motherNature.setIsland(pos);

       /* for(int i = 0 ; i<12 ;i++){
            if(i!=pos || i!=(pos+6)%12)
                this.islands.get(i).setStudentGroup(this.bag.removeStudents(1));
        }
    }*/


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
}
