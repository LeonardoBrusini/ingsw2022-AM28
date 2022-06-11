package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class CCardHandler implements EventHandler<Event> {
    private int cardIndex;
    private String col;
    private Label textLabel;

    public CCardHandler(int cardIndex,String col, Label textLabel) {
        this.cardIndex = cardIndex;
        this.col = col.toUpperCase();
        this.textLabel = textLabel;
    }

    @Override
    public void handle(Event event) {
        if(CommandSingleton.instance().getCommand().getIndex()==null || cardIndex!=CommandSingleton.instance().getCommand().getIndex()) return;
        if(CommandSingleton.instance().getPhases().get(0)==GamePhases.GUI_STUDENT_ON_CARD) {
            CommandSingleton.instance().getCommand().setPColour(col);
            CommandSingleton.instance().nextPhase();
            if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND)
                NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
            textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
        } else if (CommandSingleton.instance().getPhases().get(0)==GamePhases.GUI_GROUPS_CARD_ENTRANCE) {
            if(((Node) event.getSource()).getOpacity()!=1) {
                CommandSingleton.instance().getSgFrom()[Colour.valueOf(col).ordinal()]--;
                ((Node) event.getSource()).setOpacity(1);
            } else {
                CommandSingleton.instance().getSgFrom()[Colour.valueOf(col).ordinal()]++;
                ((Node) event.getSource()).setOpacity(0.5);
            }
        }
    }
}
