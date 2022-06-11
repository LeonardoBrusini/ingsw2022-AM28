package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.handlers.CloudHandler;
import it.polimi.ingsw.client.gui.handlers.EntranceHandler;
import it.polimi.ingsw.client.gui.handlers.HallHandler;
import it.polimi.ingsw.client.gui.handlers.IslandHandler;
import it.polimi.ingsw.network.CurrentStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Action2SimpleController extends GenericSceneController{
    @FXML
    Label textMessage;
    @FXML
    Button moveToIslandButton,moveToHallButton,moveMotherNatureButton,takeFromCloudButton;

    @FXML
    public void initialize() {
        super.initialize();
        CurrentStatus cs = StatusUpdater.instance().getCurrentStatus();
        if(!cs.getTurn().getPlayer().equals(cs.getPlayerID())) {
            textMessage.setText(GamePhases.WAIT.getGUIPrompt());
            moveMotherNatureButton.setDisable(true);
            moveToHallButton.setDisable(true);
            moveToIslandButton.setDisable(true);
            takeFromCloudButton.setDisable(true);
        }
        //add listeners for commands
        for(int i=0;i<myEntranceView.size();i++) {
            myEntranceView.get(i).setOnMouseClicked(new EntranceHandler(myEntranceCol.get(i),textMessage));
        }
        for(int i=0;i<studentPanes.size();i++) {
            studentPanes.get(i).setOnMouseClicked(new IslandHandler(i+1,textMessage));
        }
        for(int i=0;i<cloudList.size();i++) {
            cloudList.get(i).setOnMouseClicked(new CloudHandler(i,textMessage));
        }
        for(int i=0;i<myHallView.size();i++) {
            myHallView.get(i).setOnMouseClicked(new HallHandler(myHallCol.get(i)));
        }
    }

    public void moveToIslandClicked() {
        ControllerUtils.moveToIsland(textMessage);
    }
    public void moveToHallClicked() {
        ControllerUtils.moveToHall(textMessage);
    }
    public void moveMotherNatureClicked() {
        ControllerUtils.moveMotherNature(textMessage);
    }
    public void takeFromCloudClicked() {
        ControllerUtils.takeFromCloud(textMessage);
    }
}
