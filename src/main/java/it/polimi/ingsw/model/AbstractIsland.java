package it.polimi.ingsw.model;

abstract class AbstractIsland {
    protected boolean presenceMotherNature;
    protected Tower tower;
    protected StudentGroup studentGroup;
    protected int numTower;
    protected int posIndex;

    /*MANCA CALCOLO DELL'INFLUENZA DEL PLAYER */

    public AbstractIsland(int pos){
        this.studentGroup = new StudentGroup();
        this.numTower = 0;
        this.posIndex = pos;
    }

    public void setTower(Tower tower) {
        if(this.numTower == 0 ){
            this.tower = tower;
            this.numTower = 1;
        }else{
            if(!this.tower. equals(tower)){
                this.tower = tower;
            }
        }
    }

    public void setPosIndex(int pos){
        this.posIndex = pos;
    }

    public void addStudent(Colour colour,int quantity){
        this.studentGroup.setNumStudents(this.studentGroup.getQuantityColour(colour)+quantity,colour);
    }

    public StudentGroup getStudentGroup(){
        return new StudentGroup(this.studentGroup);
    }

    public Tower getTower(){
        return this.tower;
    }

    public Boolean getPresenceMotherNature(){
        return this.presenceMotherNature;
    }

    public int getNumTower(){
        return this.numTower;
    }

    public int getPosIndex(){
        return this.posIndex;
    }
}
