package it.polimi.ingsw.model;

public class Island {
    private Tower tower;
    private StudentGroup students;
    private int islandIndex;

    public Island(int index){
        tower = null;
        islandIndex = index;
        students = new StudentGroup();
    }

    public void addStudent(Colour c) {
        students.addStudent(c);
    }

    public void setTower(Tower t) {
        tower = t;
    }

    public int playerInfluence(Player p) {
        int influence = 0;
        for(Colour c : Colour.values()) {
            if(Board.instance().getProfessorGroup().getTower(c).equals(p.getTower())) {
                influence += students.getQuantityColour(c);
            }
        }
        if(tower!=null && tower.equals(p.getTower())) influence += 1;
        return influence;
    }

    public Tower getTower() {
        return tower;
    }

    public int getIslandIndex() {
        return islandIndex;
    }
}
