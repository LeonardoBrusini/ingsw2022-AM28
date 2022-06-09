package it.polimi.ingsw.client.gui.scenecontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Planning3ExpertController extends Planning3SimpleController{
    @FXML
    ArrayList<ImageView> characterCardsImages;
    @FXML
    ArrayList<GridPane> studentsOnCardPanes;
    @FXML
    ArrayList<ImageView> noEntryTileImages, coinOnCards;
    @FXML
    ArrayList<Label> noEntryTileLabels;
    @FXML
    Label myCoins, opponentCoins, opponent2Coins;

    @FXML
    public void initialize() {
        super.initialize();
        ControllerUtils.addPlanningCharacterCards(characterCardsImages,studentsOnCardPanes,noEntryTileImages, noEntryTileLabels, coinOnCards, getClass().getClassLoader());
        ControllerUtils.addCoins(myCoins,opponentCoins,opponent2Coins);
    }
}
