package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private int coins;
    private ArrayList<Cloud> clouds;
    private IslandManager islandManager;
    private MotherNature motherNature;
    private Bag bag;
    private ProfessorGroup professorGroup;
    private static Board instance;

    /**
     * Board constructor. Initializes coins, clouds, motherNature, islands (via IslandManager), bag and professorGroup
     */
    private Board(){
        coins = 20;
        for(int i = 0 ;i<ExpertGameManager.instance().getNumPlayers() ; i++){
            this.clouds.add(new Cloud());
        }
        motherNature = new MotherNature();
        islandManager = new IslandManager(motherNature.getIslandIndex());
        professorGroup = new ProfessorGroup();
        bag = new Bag();
    }

    /**
     * returns the singleton board object
     * @return the instance of the Board if it is exists, otherwise it calls the Board's constructor
     */
    public static Board instance(){
        if(Board.instance==null)
            instance = new Board();
        return instance;
    }

    /**
     * fills the clouds with n students where n is the number of players + 1
     */
    public void fillClouds(){
        for(Cloud c : this.clouds){
            StudentGroup studentList = bag.removeStudents(ExpertGameManager.instance().getNumPlayers()+1);
            c.addGroup(new StudentGroup(studentList));
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

    public ProfessorGroup getProfessorGroup() {
        return professorGroup;
    }

    /**
     * assign a professor to a player (by the colour of his tower)
     * @param c the colour of the professor
     * @param t the tower of the player we want to assign the professor
     */
    public void assignProfessor(Colour c, Tower t) {
        professorGroup.setTower(c,t);
    }

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

    //is it better to reset the attributes or just creating new objects?
    public void reset(){
        bag.setStudents(new StudentGroup(26));
        for(Cloud c: this.clouds) {
            c.clearStudents();
            c.addGroup(this.bag.removeStudents(4));
        }

        for(Colour c: Colour.values()){
            this.professorGroup.setTower(c,null);
        }
        for(int i = 0; i <= 12; i++)
            this.islandManager.getIsland(i).clearStudents();
        this.bag.inizializeIslands();
        Random generator;
        generator = new Random();
        int pos = generator.nextInt(12);
        this.motherNature.setIsland(pos);

       /* for(int i = 0 ; i<12 ;i++){
            if(i!=pos || i!=(pos+6)%12)
                this.islands.get(i).setStudentGroup(this.bag.removeStudents(1));
        }*/
    }
}