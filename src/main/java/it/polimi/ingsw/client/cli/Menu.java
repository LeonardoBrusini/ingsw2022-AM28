package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.CurrentStatus;

public class Menu {
    private CurrentStatus currentStatus;
    private Command command;
    public synchronized String generateCommand(String userInput) {



        return userInput;
    }

    public synchronized void printResult(String line) {

        System.out.println(line);
    }
}
