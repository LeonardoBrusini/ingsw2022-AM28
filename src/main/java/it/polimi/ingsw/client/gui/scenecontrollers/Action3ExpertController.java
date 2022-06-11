package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.handlers.CommandSingleton;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.CurrentStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    ArrayList<ImageView> noEntryTileImages,coinOnCards, netIslandImages;
    @FXML
    ArrayList<Label> noEntryTileLabels,netIslandLabels;
    @FXML
    Label myCoins, opponentCoins, opponent2Coins;
    @FXML
    Button confirmCCParams, playCharacterCardButton;

    @FXML
    public void initialize() {
        super.initialize();
        ControllerUtils.addCoins(myCoins,opponentCoins,opponent2Coins);
        ControllerUtils.addActionCharacterCards(characterCardsImages,studentsOnCardPanes,noEntryTileImages,noEntryTileLabels,coinOnCards,textMessage,getClass().getClassLoader());
        ControllerUtils.setNETOnIsland(netIslandImages,netIslandLabels);
        CurrentStatus cs = StatusUpdater.instance().getCurrentStatus();
        if(!cs.getTurn().getPlayer().equals(cs.getPlayerID())) {
            playCharacterCardButton.setDisable(true);
        }
        confirmCCParams.setDisable(true);
        confirmCCParams.setOpacity(0);
    }

    public void playCharacterCardClicked(MouseEvent mouseEvent) {
        ControllerUtils.playCharacterCard(textMessage);
    }

    public void playCC1(MouseEvent mouseEvent) {
        if(CommandSingleton.instance().getPhases().get(0)== GamePhases.P_CCARD_INDEX) {
            CommandSingleton.instance().getCommand().setIndex(0);
            ControllerUtils.setCardEffect(characterCardsImages.get(0), textMessage, confirmCCParams, getClass().getClassLoader());
        }
    }

    public void playCC2(MouseEvent mouseEvent) {
        if(CommandSingleton.instance().getPhases().get(0)== GamePhases.P_CCARD_INDEX) {
            CommandSingleton.instance().getCommand().setIndex(1);
            ControllerUtils.setCardEffect(characterCardsImages.get(1), textMessage, confirmCCParams, getClass().getClassLoader());
        }
    }

    public void playCC3(MouseEvent mouseEvent) {
        if(CommandSingleton.instance().getPhases().get(0)== GamePhases.P_CCARD_INDEX) {
            CommandSingleton.instance().getCommand().setIndex(2);
            ControllerUtils.setCardEffect(characterCardsImages.get(2), textMessage, confirmCCParams, getClass().getClassLoader());
        }
    }

    public void sendCCCommand(MouseEvent mouseEvent) {
        CommandSingleton c = CommandSingleton.instance();
        c.getCommand().setPStudentsFrom(c.getSgFrom());
        c.getCommand().setPStudentsTo(c.getSgTo());
        NetworkManager.instance().sendJSON(c.getCommand());
        ControllerUtils.resetOpacity(myEntrance,myHall,studentsOnCardPanes);
        confirmCCParams.setDisable(true);
        confirmCCParams.setOpacity(0);
    }
}
