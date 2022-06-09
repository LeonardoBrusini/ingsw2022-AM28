package it.polimi.ingsw.client.gui.scenecontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Action3ExpertController extends Action3SimpleController{
    @FXML
    ArrayList<ImageView> characterCardsImages;
    @FXML
    ArrayList<GridPane> studentsOnCardPanes;
    @FXML
    ArrayList<ImageView> noEntryTileImages,coinOnCards;
    @FXML
    ArrayList<Label> noEntryTileLabels;
    @FXML
    Label myCoins, opponentCoins, opponent2Coins;


    public void playCharacterCardClicked(MouseEvent mouseEvent) {

    }
}
