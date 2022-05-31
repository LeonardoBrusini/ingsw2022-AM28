package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class IslandHandler implements EventHandler<Event> {
    private int islandIndex;
    private Label textLabel;
    public IslandHandler(int i, Label textLabel) {
        islandIndex = i;
        this.textLabel = textLabel;
    }

    @Override
    public void handle(Event event) {
        switch (CommandSingleton.instance().getPhases().get(0)) {
            case P_ISLAND_INDEX -> {
                CommandSingleton.instance().getCommand().setIndex(islandIndex);
                CommandSingleton.instance().nextPhase();
                if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND)
                    NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
                textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
            }
            case PCC_ISLAND_INDEX -> {
                CommandSingleton.instance().getCommand().setPIndex(islandIndex);
                CommandSingleton.instance().nextPhase();
                if(CommandSingleton.instance().getPhases().get(0)== GamePhases.SENDCOMMAND)
                    NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
                textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
            }
            case P_MNSHIFTS -> {
                int mnIndex = StatusUpdater.instance().getCurrentStatus().getGame().getMotherNatureIndex();
                if(islandIndex>=mnIndex) CommandSingleton.instance().getCommand().setMotherNatureShifts(islandIndex-mnIndex);
                else CommandSingleton.instance().getCommand().setMotherNatureShifts(12-mnIndex+islandIndex);
                CommandSingleton.instance().nextPhase();
                if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND)
                    NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
                textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
            }
        }
    }
}
