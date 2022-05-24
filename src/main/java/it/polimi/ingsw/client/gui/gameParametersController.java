package it.polimi.ingsw.client.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class gameParametersController {
    @FXML
    Button parametersButton;
    @FXML
    ChoiceBox numPlayersMenu;
    @FXML
    ChoiceBox gameModeMenu;

    public void sendGameParameters(ActionEvent actionEvent) {
        Gson gson = new Gson();
        gson.toJson(numPlayersMenu.getSelectionModel().toString() + gameModeMenu.getSelectionModel().toString(), String.class);
        //NetworkManager.instance().send(gson);
    }
}
