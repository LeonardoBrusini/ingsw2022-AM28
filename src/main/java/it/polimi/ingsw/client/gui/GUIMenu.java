package it.polimi.ingsw.client.gui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.client.ClientObserver;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.cli.CLIPhases;
import it.polimi.ingsw.network.AddPlayerResponse;
import it.polimi.ingsw.network.CurrentStatus;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIMenu implements ClientObserver {
    private GUIScene currentScene;
    private final Stage stage;
    private CurrentStatus currentStatus;
    private AddPlayerResponse addPlayerResponse;
    private final Gson parser;

    public GUIMenu(Stage stage) {
        this.stage = stage;
        parser = new Gson();
    }

    @Override
    public void manageMessage(String line) {
        switch (currentScene) {
            case USERNAME -> manageAPR(line);
            case GAME_PARAMETERS -> manageGPR(line);
        }
    }

    private void manageGPR(String line) {
        Label parametersErrorLabel = (Label) stage.getScene().lookup("#parametersErrorLabel");
        Button parametersButton = (Button) stage.getScene().lookup("#parametersButton");
        try {
            CurrentStatus cs = parser.fromJson(line,CurrentStatus.class);
            if(cs.getStatus()==105) {
                Platform.runLater(() -> {
                    parametersErrorLabel.setText("WAITING FOR OTHER PLAYERS TO START THE GAME");
                    parametersErrorLabel.setOpacity(1);
                    parametersButton.setDisable(true);
                });
                return;
            }
            if(cs.getStatus()!=0) {
                Platform.runLater(() -> {
                    parametersErrorLabel.setText("ERROR: "+cs.getStatus()+", "+cs.getErrorMessage());
                    parametersErrorLabel.setOpacity(1);
                });
                return;
            }
            manageFirstCS(cs);
        } catch (JsonSyntaxException e) {
            Platform.runLater(() -> {
                parametersErrorLabel.setText("ERROR: received unreadable message");
                parametersErrorLabel.setOpacity(1);
            });
        }
    }

    private void manageAPR(String line) {
        Label usernameErrorLabel = (Label) stage.getScene().lookup("#usernameErrorLabel");
        Button usernameButton = (Button) stage.getScene().lookup("#usernameButton");
        try {
            addPlayerResponse = parser.fromJson(line,AddPlayerResponse.class);
            if(addPlayerResponse.isFirst()==null && addPlayerResponse.getErrorMessage()==null) {
                CurrentStatus cs = parser.fromJson(line,CurrentStatus.class);
                manageFirstCS(cs);
                return;
            }
            if(addPlayerResponse.getStatus()!=0) {
                Platform.runLater(() -> {
                    usernameErrorLabel.setText("Error "+addPlayerResponse.getStatus()+", "+addPlayerResponse.getErrorMessage());
                    usernameErrorLabel.setOpacity(1);
                });
                return;
            }
            if(addPlayerResponse.isFirst()) {
                Platform.runLater(() -> toNextScene(GUIScene.GAME_PARAMETERS));
            } else {
                Platform.runLater(() -> {
                    usernameButton.setDisable(true);
                    usernameErrorLabel.setText("ADDED! WAITING FOR OTHER PLAYERS...");
                    usernameErrorLabel.setOpacity(1);
                });
            }
        } catch (JsonSyntaxException e) {
            Platform.runLater(() -> {
                usernameErrorLabel.setText("ERROR: received unreadable message");
                usernameErrorLabel.setOpacity(1);
            });
        }
    }

    private void manageFirstCS(CurrentStatus cs) {
        StatusUpdater.instance().updateStatus(cs);
        currentStatus = StatusUpdater.instance().getCurrentStatus();
        if(currentStatus.getGameMode().equals("expert")) {
            if (currentStatus.getGame().getPlayers().size()==2) Platform.runLater(() -> toNextScene(GUIScene.PLANNING_2_EXPERT));
            else Platform.runLater(() -> toNextScene(GUIScene.PLANNING_3_EXPERT));
        } else {
            if (currentStatus.getGame().getPlayers().size()==2) Platform.runLater(() -> toNextScene(GUIScene.PLANNING_2_SIMPLE));
            else Platform.runLater(() -> toNextScene(GUIScene.PLANNING_3_SIMPLE));
        }
    }
    private void manageCS(String line) {


    }

    public void toNextScene(GUIScene sceneType) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/"+sceneType.getFileName()));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            if(sceneType==GUIScene.TITLE_SCREEN) currentScene = GUIScene.USERNAME;
            else {
                currentScene = sceneType;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
