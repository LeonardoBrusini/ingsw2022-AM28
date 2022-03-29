package it.polimi.ingsw.model;

public class Archipelago extends AbstractIsland{
    /*public Archipelago(Island input, Island output){
        super(input.getPosIndex());
        if(input.getTower().equals(output.getTower()) && (input.getPosIndex()==output.posIndex+1 || input.getPosIndex()+1==output.posIndex)){
            if(input.getPosIndex()>output.getPosIndex()) {
                setPosIndex(output.getPosIndex());
            }



            this.tower = input.getTower();
            this.numTower = input.getNumTower() + output.getNumTower();


            this.presenceMotherNature = input.getPresenceMotherNature() || output.getPresenceMotherNature();


            Colour[] e = Colour.values();

            for(Colour c : e){
                int val;
                val= input.getStudentGroup().getQuantityColour(c) + output.getStudentGroup().getQuantityColour(c);
                this.studentGroup.setNumStudents(val,c);
            }

        }
    }
    */
    public Archipelago(AbstractIsland input, AbstractIsland output){
        super();
        if(input.getTower().equals(output.getTower()) && (input.getPosIndex()==output.posIndex+1 || input.getPosIndex()+1==output.posIndex)){
            setPosIndex(input.getPosIndex());

            if(input.getPosIndex()>output.getPosIndex()) {
                setPosIndex(output.getPosIndex());
            }


            setTower(input.getTower());
            this.numTower = input.getNumTower() + output.getNumTower();

            this.presenceMotherNature = input.getPresenceMotherNature() || output.getPresenceMotherNature();

            Colour[] e = Colour.values();

            for(Colour c : e){
                int val;
                val= input.getStudentGroup().getQuantityColour(c) + output.getStudentGroup().getQuantityColour(c);
                this.studentGroup.setNumStudents(val,c);
            }
        }
    }
}
