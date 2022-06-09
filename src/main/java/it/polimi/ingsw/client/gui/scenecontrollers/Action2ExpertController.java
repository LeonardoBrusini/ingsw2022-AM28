package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.network.CurrentStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.lang.management.BufferPoolMXBean;
import java.util.ArrayList;
import java.util.Currency;

public class Action2ExpertController extends Action2SimpleController {
    @FXML
    ArrayList<ImageView> characterCardsImages;
    @FXML
    ArrayList<GridPane> studentsOnCardPanes;
    @FXML
    ArrayList<ImageView> noEntryTileImages,coinOnCards;
    @FXML
    ArrayList<Label> noEntryTileLabels;
    @FXML
    Label myCoins, opponentCoins;
    @FXML
    Button playCharacterCardButton;

    @FXML
    public void initialize() {
        super.initialize();
        ControllerUtils.addCoins(myCoins,opponentCoins);
        ControllerUtils.addActionCharacterCards(characterCardsImages,studentsOnCardPanes,noEntryTileImages,noEntryTileLabels,coinOnCards,getClass().getClassLoader());
        CurrentStatus cs = StatusUpdater.instance().getCurrentStatus();
        if(!cs.getTurn().getPlayer().equals(cs.getPlayerID())) {
            playCharacterCardButton.setDisable(true);
        }
    }

    public void playCharacterCardClicked() {

    }
}
