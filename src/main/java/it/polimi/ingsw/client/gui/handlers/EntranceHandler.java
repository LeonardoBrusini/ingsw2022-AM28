package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class EntranceHandler implements EventHandler<Event> {
    private String col;
    private Label textLabel;

    public EntranceHandler(String col, Label textLabel) {
        this.col = col.toUpperCase();
        this.textLabel = textLabel;
    }

    @Override
    public void handle(Event event) {
        switch (CommandSingleton.instance().getPhases().get(0)) {
            case P_STUDENT_COLOUR -> {
                CommandSingleton.instance().getCommand().setStudentColour(col);
                CommandSingleton.instance().nextPhase();
                if(CommandSingleton.instance().getPhases().get(0)==GamePhases.SENDCOMMAND)
                    NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
                textLabel.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
            }
            case GUI_GROUPS_CARD_ENTRANCE -> {
                if(((Node) event.getSource()).getOpacity()!=1) {
                    CommandSingleton.instance().getSgTo()[Colour.valueOf(col).ordinal()]--;
                    ((Node) event.getSource()).setOpacity(1);
                } else {
                    CommandSingleton.instance().getSgTo()[Colour.valueOf(col).ordinal()]++;
                    ((Node) event.getSource()).setOpacity(0.5);
                }
            }
            case GUI_GROUPS_ENTRANCE_HALL -> {
                if(((Node) event.getSource()).getOpacity()!=1){
                    CommandSingleton.instance().getSgFrom()[Colour.valueOf(col).ordinal()]--;
                    ((Node) event.getSource()).setOpacity(1);
                } else {
                    CommandSingleton.instance().getSgFrom()[Colour.valueOf(col).ordinal()]++;
                    ((Node) event.getSource()).setOpacity(0.5);
                }
            }
        }
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public Label getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(Label textLabel) {
        this.textLabel = textLabel;
    }
}
