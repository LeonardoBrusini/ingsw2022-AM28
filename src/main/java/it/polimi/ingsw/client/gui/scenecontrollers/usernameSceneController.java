package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.GUIMenu;
import it.polimi.ingsw.client.gui.GUIScene;
import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class usernameSceneController {
    @FXML
    TextField usernameField;
    @FXML
    Button usernameButton;
    @FXML
    ImageView backImage;
    public void sendUsername(ActionEvent actionEvent) {
        NetworkManager.instance().send(usernameField.getText());
    }

    public void backToTitle() {
        StatusUpdater.reset();
        NetworkManager.instance().close();
        ((GUIMenu) NetworkManager.instance().getObserver()).toNextScene(GUIScene.TITLE_SCREEN);
    }
}
