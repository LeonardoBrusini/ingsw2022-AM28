package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.network.CharacterCardStatus;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class Planning2ExpertController extends Planning2SimpleController{
    //public CurrentStatus cs;
    //public Planning2ExpertController(CurrentStatus currentStatus) {
    //    cs = currentStatus;
    //}
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

    /*public void playCard1(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(1).toString());
    }

    public void playCard2(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(2).toString());
    }

    public void playCard3(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(3).toString());
    }

    public void playCard4(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(4).toString());
    }

    public void playCard5(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(5).toString());
    }

    public void playCard6(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(6).toString());
    }

    public void playCard7(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(7).toString());
    }

    public void playCard8(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(8).toString());
    }

    public void playCard9(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(9).toString());
    }

    public void playCard10(MouseEvent mouseEvent) {
        NetworkManager.instance().send(generateCommand(10).toString());
    }

    private Gson generateCommand(int index){
        Gson gson = new Gson();
        Command command = new Command();
        command.setIndex(index);
        command.setPlayerIndex(cs.getPlayerID());
        command.setCmd("PLAYASSISTANTCARD");
        gson.toJson(command, Command.class);
        return gson;
    }*/
}
