package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.server.enumerations.Colour;

import java.util.ArrayList;

public class CommandSingleton {
    private ArrayList<GamePhases> phases;
    private Command command;
    private int[] studentGroup;
    private static CommandSingleton instance;

    private CommandSingleton() {
        command = new Command();
        phases = new ArrayList<>();
        phases.add(GamePhases.WAIT);
        studentGroup = new int[Colour.values().length];
    }

    public static CommandSingleton instance() {
        if(instance==null) instance = new CommandSingleton();
        return instance;
    }

    public Command getCommand() {
        return command;
    }

    public ArrayList<GamePhases> getPhases() {
        return phases;
    }

    public int[] getStudentGroup() {
        return studentGroup;
    }

    public void newCommand(String cmd) {
        phases = new ArrayList<>();
        studentGroup = new int[5];
        command = new Command();
        command.setPlayerIndex(StatusUpdater.instance().getCurrentStatus().getPlayerID());
        command.setCmd(cmd);
    }

    public void nextPhase() {
        phases.remove(0);
    }
}
