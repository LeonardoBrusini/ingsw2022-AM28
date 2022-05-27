package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.PlayerStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;

public class GenericScene3Controller extends GenericSceneController{
    @FXML
    GridPane opponent2Entrance,opponent2Hall,opponent2Towers;
    @FXML
    ImageView opponent2LAC;
    @FXML
    Label opponent2Name;

    @FXML
    public void initialize() {
        super.initialize();
        ArrayList<PlayerStatus> players = StatusUpdater.instance().getCurrentStatus().getGame().getPlayers();
        int myPlayerIndex = StatusUpdater.instance().getCurrentStatus().getPlayerID();
        if(players.get(1).getIndex()!=myPlayerIndex) {
            playerStatus(players.get(1),opponent2Name, opponent2LAC, opponent2Towers, opponent2Entrance, opponent2Hall);
        } else {
            playerStatus(players.get(1), myName, myLAC, myTowers, myEntrance, myHall);
            playerStatus(players.get(2), opponent2Name, opponent2LAC, opponent2Towers, opponent2Entrance, opponent2Hall);
        }
    }
}
