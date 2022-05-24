package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Currency;

public class usernameSceneController {
    @FXML
    TextField usernameField;
    @FXML
    Button usernameButton;
    public void sendUsername(ActionEvent actionEvent) {
        //NetworkManager.instance().send(usernameField.getText());
    }
}
