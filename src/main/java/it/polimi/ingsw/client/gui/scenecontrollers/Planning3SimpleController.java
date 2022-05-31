package it.polimi.ingsw.client.gui.scenecontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
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
