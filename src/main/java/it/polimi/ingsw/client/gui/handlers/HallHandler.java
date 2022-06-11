package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class HallHandler implements EventHandler<Event> {
    private String col;

    public HallHandler(String s) {
        col=s;
    }

    @Override
    public void handle(Event event) {
        //only for character card
        if(CommandSingleton.instance().getPhases().get(0)== GamePhases.GUI_GROUPS_ENTRANCE_HALL) {
            if(((Node) event.getSource()).getOpacity()!=1) {
                CommandSingleton.instance().getSgTo()[Colour.valueOf(col).ordinal()]--;
                ((Node) event.getSource()).setOpacity(1);
            } else {
                CommandSingleton.instance().getSgTo()[Colour.valueOf(col).ordinal()]++;
                ((Node) event.getSource()).setOpacity(0.5);
            }
        }
    }
}
