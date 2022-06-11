package it.polimi.ingsw.client.gui.scenecontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class Planning2ExpertController extends Planning2SimpleController{
    @FXML
    ArrayList<ImageView> characterCardsImages,netIslandImages;
    @FXML
    ArrayList<GridPane> studentsOnCardPanes;
    @FXML
    ArrayList<ImageView> noEntryTileImages,coinOnCards;
    @FXML
    ArrayList<Label> noEntryTileLabels,netIslandLabels;
    @FXML
    Label myCoins, opponentCoins;

    @FXML
    public void initialize() {
        super.initialize();
        ControllerUtils.addPlanningCharacterCards(characterCardsImages,studentsOnCardPanes,noEntryTileImages,noEntryTileLabels,coinOnCards,null,getClass().getClassLoader());
        ControllerUtils.addCoins(myCoins,opponentCoins);
        ControllerUtils.setNETOnIsland(netIslandImages,netIslandLabels);
    }
}
