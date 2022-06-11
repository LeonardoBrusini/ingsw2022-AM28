package it.polimi.ingsw.client.gui.handlers;

import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class ColourConfirmHandler implements EventHandler<Event> {
    private ChoiceBox choiceBox;

    public ColourConfirmHandler(ChoiceBox choiceBox) {
        this.choiceBox = choiceBox;
    }

    @Override
    public void handle(Event event) {
        CommandSingleton.instance().getCommand().setPColour(choiceBox.getSelectionModel().getSelectedItem().toString());
        NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
