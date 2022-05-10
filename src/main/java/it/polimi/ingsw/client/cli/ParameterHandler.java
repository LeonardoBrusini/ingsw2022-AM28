package it.polimi.ingsw.client.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.network.Command;

import java.util.Scanner;

public class ParameterHandler {
    private int playerID;
    
    public ParameterHandler(int playerID) {
        this.playerID = playerID;
    }
    
    public String askMoveMotherNatureParameters(int max){
        Scanner in = new Scanner(System.in);
        int i;
        do{
            System.out.println("Choose number of steps (1 to "+max+"): ");
            i=in.nextInt();
        } while (i<1 || i>max);
        Command c = new Command();
        c.setCmd("MOVEMOTHERNATURE");
        c.setMotherNatureShifts(i);
        c.setPlayerIndex(playerID);
        return new Gson().toJson(c);
    }
    
    public String askAssistantCardParameters() {
        Scanner in = new Scanner(System.in);
        int i;
        do{
            System.out.println("Choose the assistant card to play (0 to 9)");
            i=in.nextInt();
        } while (i<0 || i>9);
        Command c = new Command();
        c.setCmd("PLAYASSISTANTCARD");
        c.setPlayerIndex(playerID);
        c.setIndex(i);
        return new Gson().toJson(c);
    }

    public String askMoveStudentToIslandParameters() {
        Scanner in = new Scanner(System.in);
        String colour;
        do{
            System.out.println("Choose student colour (YELLOW, BLUE, GREEN, PINK, RED):");
            colour = in.nextLine().toUpperCase();
        } while (!colour.equals("YELLOW") && !colour.equals("BLUE") && !colour.equals("GREEN") && !colour.equals("PINK") && !colour.equals("RED"));
        int i;
        do{
            System.out.println("Choose island (1 to 12):");
            i=in.nextInt();
        } while (i<1 || i>12);
        Command c = new Command();
        c.setCmd("MOVESTUDENTTOISLAND");
        c.setPlayerIndex(playerID);
        c.setStudentColour(colour);
        c.setIndex(i);
        return new Gson().toJson(c);
    }


    public String askMoveStudentToHallParameters() {
        return null;
    }

    public String askTakeStudentsFromCloudParameters(int numPlayers) {
        return null;
    }

    public String askPlayCharacterCardStatus() {
        return null;
    }
}
