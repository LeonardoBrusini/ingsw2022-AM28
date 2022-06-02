package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.ArchipelagoStatus;
import it.polimi.ingsw.network.IslandStatus;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

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
                System.out.println("getting shifts: island selected = "+islandIndex+", mnIndex = "+mnIndex);
                ArrayList<ArchipelagoStatus> archipelagos = StatusUpdater.instance().getCurrentStatus().getGame().getArchipelagos();
                int aSize = archipelagos.size();
                int oldIndex = 0,newIndex = 0;
                for(int i=0; i<aSize;i++) {
                    ArrayList<IslandStatus> islands = archipelagos.get(i).getIslands();
                    System.out.println("archipelago "+archipelagos.get(i).getIndex());
                    for (int j=0;j<islands.size();j++) {
                        System.out.println("island "+islands.get(j).getIslandIndex());
                        if(islands.get(j).getIslandIndex()==mnIndex) oldIndex = archipelagos.get(i).getIndex();
                        if(islands.get(j).getIslandIndex()==islandIndex) newIndex = archipelagos.get(i).getIndex();
                    }
                }
                int shifts = newIndex >= oldIndex ? newIndex-oldIndex : (aSize-oldIndex+newIndex);
                System.out.println("shifts = "+shifts);
                CommandSingleton.instance().getCommand().setMotherNatureShifts(shifts);

                /*if(islandIndex>=mnIndex) CommandSingleton.instance().getCommand().setMotherNatureShifts(islandIndex-mnIndex);
                else CommandSingleton.instance().getCommand().setMotherNatureShifts(12-mnIndex+islandIndex);*/
                CommandSingleton.instance().nextPhase();
                if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND)
                    NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
                textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
            }
        }
    }
}
