package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private int coins;
    private int numPlayers;
    private ArrayList<Cloud> clouds = new ArrayList<>();
    private ArrayList<AbstractIsland> islands = new ArrayList<>();
    private MotherNature motherNature;
    private Bag bag = new Bag();
    private ProfessorGroup professorGroup = new ProfessorGroup();
    private static Board instance=null;

    private Board(int numPlayers){

        this.coins = 20 - numPlayers;
        this.numPlayers = numPlayers;


        int i;
        for(i = 0 ;i<numPlayers ; i++){
            this.clouds.add(new Cloud());
            this.clouds.get(i).setGroup(bag.removeStudent(4));
        }

        this.motherNature = new MotherNature();

        for(i = 0; i < 12; i++) {
            islands.add(new Island(i));
            islands.get(i).setPosIndex(i);
            if(i == motherNature.getIslandIndex() || (motherNature.getIslandIndex()+ 6 % 12)==i){
                islands.get(i).setPresenceMotherNature(i == motherNature.getIslandIndex());
            }else{
                StudentGroup c = new StudentGroup(bag.removeStudent(1));
                islands.get(i).setStudentGroup(c);
            }
        }

    }

    public static Board instance(){
        if(Board.instance==null)
            instance = new Board(3);

        //NB per ora ho messo numPlayer = 3 in attesa della creazione dell'expertgamemanager

        return Board.instance;
    }

    public void fillClouds(){
        for(int i = 0; i < clouds.size(); i++){
            if(clouds.get(i).empty())
                clouds.get(i).setGroup(bag.removeStudent(4));
        }
    }

    public MotherNature getMotherNature(){
        return this.motherNature;
    }


    public Bag getBag() {
        return bag;
    }

    public int getCoins() {
        return coins;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public StudentGroup getStudentOnCloud(int pos){
        return this.clouds.get((pos%12)%(this.clouds.size())).getCloud();
    }

    public ProfessorGroup getProfessorGroup() {
        return professorGroup;
    }

    public void setProfessorGroup(Colour c, Tower t) {
        this.professorGroup.setTower(c,t);
    }

    public void setProfessorGroup(ProfessorGroup p){
        this.professorGroup = p;
    }

    public void reset(){
        bag.setStudents(new StudentGroup(26));
        for(Cloud c: this.clouds)
            c.setGroup(bag.removeStudent(4));

        for(Colour c: Colour.values()){
            this.professorGroup.setTower(c,null);
        }

        Random generator;
        generator = new Random();
        int pos = generator.nextInt(12);
        this.motherNature.setIsland(pos);

        for(int i = 0 ; i<12 ;i++){
            if(i!=pos || i!=(pos+6)%12)
                this.islands.get(i).setStudentGroup(bag.removeStudent(1));
        }

    }
}
