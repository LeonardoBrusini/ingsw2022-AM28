package it.polimi.ingsw.client.gui.scenecontrollers.expertmode.planningscenes;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.CurrentStatus;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class PlanningSceneExpertController{
    public CurrentStatus cs;
    public PlanningSceneExpertController(CurrentStatus currentStatus) {
        cs = currentStatus;
    }


    public void playCard1(MouseEvent mouseEvent) {
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
    }
}
