package it.polimi.ingsw.client.gui.scenecontrollers;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.GameParameters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.util.Locale;

public class gameParametersController {
    private final ObservableList<String> numPlayers = FXCollections.observableArrayList("2","3");
    private final ObservableList<String> gameMode = FXCollections.observableArrayList("SIMPLE","EXPERT");
    @FXML
    Button parametersButton;
    @FXML
    ChoiceBox numPlayersMenu;
    @FXML
    ChoiceBox gameModeMenu;

    @FXML
    private void initialize() {
        numPlayersMenu.setValue("2");
        numPlayersMenu.setItems(numPlayers);
        gameModeMenu.setValue("SIMPLE");
        gameModeMenu.setItems(gameMode);
    }

    public void sendGameParameters(ActionEvent actionEvent) {
        Gson gson = new Gson();
        GameParameters gm = new GameParameters();
        gm.setGameMode(gameModeMenu.getSelectionModel().getSelectedItem().toString().toLowerCase());
        gm.setNumPlayers(Integer.parseInt(numPlayersMenu.getSelectionModel().getSelectedItem().toString()));
        String command = gson.toJson(gm);
        NetworkManager.instance().send(command);
    }


}