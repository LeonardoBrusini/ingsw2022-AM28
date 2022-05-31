package it.polimi.ingsw.client.gui.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class HallHandler implements EventHandler<Event> {
    private String col;
    private Label textLabel;

    public HallHandler(String s, Label textMessage) {
        col=s;
        textLabel = textMessage;
    }

    @Override
    public void handle(Event event) {
        //only for character card
    }
}
