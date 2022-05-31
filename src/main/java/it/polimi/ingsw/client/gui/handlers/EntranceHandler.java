package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class EntranceHandler implements EventHandler<Event> {
    private String col;
    private Label textLabel;

    public EntranceHandler(String col, Label textLabel) {
        this.col = col;
        this.textLabel = textLabel;
    }

    @Override
    public void handle(Event event) {
        switch (CommandSingleton.instance().getPhases().get(0)) {
            case P_STUDENT_COLOUR -> {
                CommandSingleton.instance().getCommand().setStudentColour(col.toUpperCase());
                CommandSingleton.instance().nextPhase();
                if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND)
                    NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
                textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
            }
            case PCC_STUDENT_COLOUR -> {
                /*CommandSingleton.instance().getStudentGroup()[Colour.valueOf(col).ordinal()]++;
                CommandSingleton.instance().nextPhase();

                if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND) {
                    NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
                }*/
            }
        }
    }
}
