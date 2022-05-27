package it.polimi.ingsw.client.gui.scenecontrollers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Planning3ExpertController extends GenericScene3Controller{
    @FXML
    ArrayList<ImageView> characterCardsImages;
    @FXML
    ArrayList<GridPane> studentsOnCardPanes;
    @FXML
    ArrayList<ImageView> noEntryTileImages;

    @FXML
    public void initialize() {
        super.initialize();
        ControllerUtils.addPlanningCharacterCards(characterCardsImages,studentsOnCardPanes,noEntryTileImages,getClass().getClassLoader());
    }
}
