package it.polimi.ingsw.model;

import java.util.Random;

public class MotherNature {
    private int island;

    public MotherNature(){
        Random generator = new Random();
        int island = generator.nextInt(12)+1;
    }

    public int computeInfluence(Player player){
        ProfessorGroup p = Board.instance().getProfessorGroup();
        Tower t= player.getTower();
        StudentGroup s;
        Island i = Board.instance().getIslandManager().getIsland(this.island);
        s = i.getStudents();

        int res = 0;
        for(Colour c: Colour.values()){
            if(p.getTower(c).equals(t))
                res+=s.getQuantityColour(c);
        }

        return res;
    }

    public int getIslandIndex(){
        return island;
    }

    public void setIsland(int pos){
        island = pos;
    }
}
