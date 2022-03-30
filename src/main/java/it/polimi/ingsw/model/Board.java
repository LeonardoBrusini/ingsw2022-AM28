package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board {
    private int coins;
    private int numPlayers;
    private ArrayList<Cloud> clouds = new ArrayList<>();
    private ArrayList<AbstractIsland> islands = new ArrayList<>();
    private MotherNature motherNature;
    private Bag bag = new Bag();

    public Board(int numPlayers){
        this.coins = 20 - numPlayers;
        this.numPlayers = numPlayers;
        int i;
        for(i = 0 ;i<numPlayers ; i++){
            this.clouds.add(new Cloud());
            this.clouds.get(i).setGroup(bag.removeStudent(4));
        }

        this.motherNature = new MotherNature();

        for(i = 0; i < 12; i++){
            islands.add(new Island(i));
            islands.get(i).setPosIndex(i);
            if(i == motherNature.getIslandIndex() || (motherNature.getIslandIndex()+ 6 % 12)==i){
                islands.get(i).setPresenceMotherNature(i == motherNature.getIslandIndex());
                StudentGroup c = new StudentGroup(bag.removeStudent(1));
                islands.get(i).setStudentGroup(c);
            }else{
                StudentGroup c = new StudentGroup(bag.removeStudent(2));
                islands.get(i).setStudentGroup(c);
            }
        }
    }




}
