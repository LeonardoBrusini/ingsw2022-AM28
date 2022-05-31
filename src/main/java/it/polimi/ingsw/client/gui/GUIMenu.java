package it.polimi.ingsw.client.gui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.client.ClientObserver;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.AddPlayerResponse;
import it.polimi.ingsw.network.CurrentStatus;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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
            case PLANNING_2_SIMPLE,PLANNING_2_EXPERT,PLANNING_3_EXPERT,PLANNING_3_SIMPLE -> manageCSPlanning(line);
            case ACTION_2_EXPERT,ACTION_3_EXPERT,ACTION_2_SIMPLE,ACTION_3_SIMPLE -> manageCSAction(line);
        }
    }

    private void manageCSAction(String line) {
        changePhaseScene(line);
    }

    private void changePhaseScene(String line) {
        if (currentStatus!=null && checkWinner()) return;
        CurrentStatus cs = parser.fromJson(line, CurrentStatus.class);
        if(cs.getStatus()!=0) {
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText(cs.getErrorMessage());
                a.show();
            });
            return;
        }
        StatusUpdater.instance().updateStatus(cs);
        currentStatus = StatusUpdater.instance().getCurrentStatus();

        if(currentStatus.getTurn().getPhase().equalsIgnoreCase("action")) {
            if(currentStatus.getGameMode().equals("expert")) {
                if (currentStatus.getGame().getPlayers().size()==2) Platform.runLater(() -> toNextScene(GUIScene.ACTION_2_EXPERT));
                else Platform.runLater(() -> toNextScene(GUIScene.ACTION_3_EXPERT));
            } else {
                if (currentStatus.getGame().getPlayers().size()==2) Platform.runLater(() -> toNextScene(GUIScene.ACTION_2_SIMPLE));
                else Platform.runLater(() -> toNextScene(GUIScene.ACTION_3_SIMPLE));
            }
        } else {
            if(currentStatus.getGameMode().equals("expert")) {
                if (currentStatus.getGame().getPlayers().size()==2) Platform.runLater(() -> toNextScene(GUIScene.PLANNING_2_EXPERT));
                else Platform.runLater(() -> toNextScene(GUIScene.PLANNING_3_EXPERT));
            } else {
                if (currentStatus.getGame().getPlayers().size()==2) Platform.runLater(() -> toNextScene(GUIScene.PLANNING_2_SIMPLE));
                else Platform.runLater(() -> toNextScene(GUIScene.PLANNING_3_SIMPLE));
            }
        }
        checkWinner();
    }

    private boolean checkWinner() {
        if(currentStatus.getWinner()!=null) {
            if(currentStatus.getWinner().equals("")) {
                Platform.runLater(() -> {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText(GamePhases.DRAW.getGUIPrompt());
                    Optional<ButtonType> result = a.showAndWait();
                    if(result.isPresent()) {
                        if(result.get() == ButtonType.OK) {
                            StatusUpdater.reset();
                            NetworkManager.instance().close();
                            toNextScene(GUIScene.TITLE_SCREEN);
                        }
                    }
                });
            } else {
                Platform.runLater(() -> {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Player "+currentStatus.getWinner()+" just WON THE GAME!");
                    Optional<ButtonType> result = a.showAndWait();
                    if(result.isPresent()) {
                        if(result.get() == ButtonType.OK) {
                            StatusUpdater.reset();
                            NetworkManager.instance().close();
                            toNextScene(GUIScene.TITLE_SCREEN);
                        }
                    }
                });
            }
            return true;
        }
        return false;
    }

    private void manageCSPlanning(String line) {

        changePhaseScene(line);
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
            manageFirstCS(line);
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
                manageFirstCS(line);
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

    private void manageFirstCS(String line) {
        changePhaseScene(line);
    }
    private void manageCS(String line) {


    }

    public void toNextScene(GUIScene sceneType) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/"+sceneType.getFileName()));
            Scene scene = new Scene(root);
            stage.setScene(scene);

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
