package it.polimi.ingsw.model;

public class Archipelago extends AbstractIsland{
    public Archipelago(Island input, Island output){

        if(input.getTower().equals(output.getTower()) && (input.getPosIndex()==output.posIndex+1 || input.getPosIndex()+1==output.posIndex)){
            this.tower = input.getTower();
            this.numTower = input.getNumTower() + output.getNumTower();

            if(input.getPosIndex()<output.getPosIndex()) {
                this.posIndex = input.getPosIndex();
            }else{
                this.posIndex = output.getPosIndex();
            }

            if(input.getPresenceMotherNature() == true || output.getPresenceMotherNature() == true)
                this.presenceMotherNature = true;
            else
                this.presenceMotherNature = false;

            this.studentGroup = new StudentGroup(0);
            Colour[] e = Colour.values();

            for(Colour c : e){
                int val;
                val= input.getStudentGroup().getQuantityColour(c) + output.getStudentGroup().getQuantityColour(c);
                this.studentGroup.setNumStudents(val,c);
            }

        }
    }

    public Archipelago(Island input, Archipelago output){
        if(input.getTower().equals(output.getTower()) && (input.getPosIndex()==output.posIndex+1 || input.getPosIndex()+1==output.posIndex)){
            this.tower = input.getTower();
            this.numTower = input.getNumTower() + output.getNumTower();

            if(input.getPosIndex()<output.getPosIndex()) {
                this.posIndex = input.getPosIndex();
            }else{
                this.posIndex = output.getPosIndex();
            }

            if(input.getPresenceMotherNature() == true || output.getPresenceMotherNature() == true)
                this.presenceMotherNature = true;
            else
                this.presenceMotherNature = false;

            this.studentGroup = new StudentGroup(0);
            Colour[] e = Colour.values();

            for(Colour c : e){
                int val;
                val= input.getStudentGroup().getQuantityColour(c) + output.getStudentGroup().getQuantityColour(c);
                this.studentGroup.setNumStudents(val,c);
            }

        }
    }
}
