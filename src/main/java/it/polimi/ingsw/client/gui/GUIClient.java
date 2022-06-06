package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.network.NetworkManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;


public class GUIClient extends Application {

    public static void main( String[] args ){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Eriantys");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
        primaryStage.setOnCloseRequest(t -> System.exit(0));
        GUIMenu menu = new GUIMenu(primaryStage);
        NetworkManager.instance().setObserver(menu);
        menu.toNextScene(GUIScene.TITLE_SCREEN);
        primaryStage.show();
    }

}
