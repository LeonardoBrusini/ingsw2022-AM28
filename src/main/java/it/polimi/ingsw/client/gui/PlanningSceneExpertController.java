package it.polimi.ingsw.client.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.server.controller.commands.CommandList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import javax.swing.text.html.ImageView;

public class PlanningSceneExpertController{
    public CurrentStatus cs;
    public PlanningSceneExpertController(CurrentStatus currentStatus) {
        cs = currentStatus;
    }

    public void playCard1(MouseEvent mouseEvent) {
        //NetworkManager.instance().send(generateCommand(1));
    }

    public void playCard2(MouseEvent mouseEvent) {
        //NetworkManager.instance().send(generateCommand(2));
    }

    public void playCard3(MouseEvent mouseEvent) {
        //NetworkManager.instance().send(generateCommand(3));
    }

    public void playCard4(MouseEvent mouseEvent) {
        //NetworkManager.instance().send(generateCommand(4));
    }

    public void playCard5(MouseEvent mouseEvent) {
        //NetworkManager.instance().send(generateCommand(5));
    }

    public void playCard6(MouseEvent mouseEvent) {
        //NetworkManager.instance().send(generateCommand(6));
    }

    public void playCard7(MouseEvent mouseEvent) {
        //NetworkManager.instance().send(generateCommand(7));
    }

    public void playCard8(MouseEvent mouseEvent) {
    }

    public void playCard9(MouseEvent mouseEvent) {
    }

    public void playCard10(MouseEvent mouseEvent) {
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
