package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class CloudHandler implements EventHandler<Event> {
    private int cloudIndex;
    private Label textLabel;
    public CloudHandler(int i, Label textLabel) {
        cloudIndex = i;
        this.textLabel = textLabel;
    }

    @Override
    public void handle(Event event) {
        if (CommandSingleton.instance().getPhases().get(0)==GamePhases.P_CLOUD_INDEX) {
            CommandSingleton.instance().getCommand().setIndex(cloudIndex);
            CommandSingleton.instance().nextPhase();
            if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND) NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
            textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
        }
    }
}
