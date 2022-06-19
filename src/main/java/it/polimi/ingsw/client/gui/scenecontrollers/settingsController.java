package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.GUIMenu;
import it.polimi.ingsw.client.gui.GUIScene;
import it.polimi.ingsw.client.network.NetworkManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class settingsController {
    @FXML
    Button quitButton,titleScreenButton,backToGameButton;

    public void closeGame() {
        System.exit(0);
    }

    public void goToTitleScreen() {
        StatusUpdater.reset();
        NetworkManager.instance().close();
        ((GUIMenu) NetworkManager.instance().getObserver()).toNextScene(GUIScene.TITLE_SCREEN);
        backToGame();
    }

    public void backToGame() {
        ((Stage) backToGameButton.getScene().getWindow()).close();
    }

}
