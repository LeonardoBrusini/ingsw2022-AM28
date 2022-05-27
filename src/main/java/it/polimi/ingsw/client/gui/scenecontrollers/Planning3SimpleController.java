package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.CommandHandler;
import it.polimi.ingsw.network.PlayerStatus;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Planning3SimpleController extends GenericScene3Controller{
    @FXML
    HBox ACBox;
    @FXML
    ScrollPane ACPane;

    @FXML
    public void initialize() {
        super.initialize();
        ControllerUtils.addAssistantCards(ACBox,ACPane,getClass().getClassLoader());
    }
}
