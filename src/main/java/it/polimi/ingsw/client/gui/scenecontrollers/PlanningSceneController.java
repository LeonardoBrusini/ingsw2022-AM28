package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.CommandHandler;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;

public class PlanningSceneController {
    @FXML
    GridPane myEntrance,opponentEntrance,myHall,opponentHall,myTowers,opponentTowers;
    @FXML
    ImageView myLAC,opponentLAC;
    @FXML
    HBox ACBox;
    @FXML
    ScrollPane ACPane;
    @FXML
    Label opponentName,myName;
    @FXML
    ArrayList<GridPane> cloudList,studentPanes;
    @FXML
    ArrayList<ImageView> towersImages,motherNatureImages,bridgeImages;

    @FXML
    private void initialize(){
        CurrentStatus cs = StatusUpdater.instance().getCurrentStatus();
        //Players
        ArrayList<PlayerStatus> players = cs.getGame().getPlayers();
        if(players.get(0).getIndex()==cs.getPlayerID()) myStatus(players.get(0));
        else opponentStatus(players.get(0));
        if(players.get(1).getIndex()==cs.getPlayerID()) myStatus(players.get(1));
        else opponentStatus(players.get(1));
        //Board
        fillClouds(cs.getGame().getClouds());
        fillIslands(cs.getGame().getArchipelagos());
        motherNatureImages.get(cs.getGame().getMotherNatureIndex()-1).setImage(new Image(getClass().getClassLoader().getResource("images/wooden_pieces/mother_nature.png").toString(),45,45,true,true));
    }

    private void fillIslands(ArrayList<ArchipelagoStatus> archipelagos) {
        for (ArchipelagoStatus as: archipelagos) {
            for(int i=0;i<as.getIslands().size();i++) {
                fillIsland(as.getIslands().get(i));
                if(i<as.getIslands().size()-1) {
                    bridgeImages.get(as.getIslands().get(i).getIslandIndex()-1).setOpacity(1);
                }
            }
        }
    }

    private void fillIsland(IslandStatus islandStatus) {
        GridPane studentPane = studentPanes.get(islandStatus.getIslandIndex()-1);
        for (int i=0;i<islandStatus.getStudents().length;i++) {
            String col = Colour.values()[i].toString().toLowerCase();
            Image sImage = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/student_"+col+".png").toString(),25,25,true,true);
            ImageView imageView = new ImageView(sImage);
            Label sLabel = new Label(""+islandStatus.getStudents()[i]);
            studentPane.add(imageView,0,i);
            studentPane.add(sLabel,1,i);
            if(islandStatus.getTowerColour()!=null) {
                Image tower = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/"+islandStatus.getTowerColour().toLowerCase()+"_tower.png").toString(),40,50,true,true);
                towersImages.get(islandStatus.getIslandIndex()-1).setImage(tower);
            }
        }
    }

    private void fillClouds(ArrayList<CloudStatus> clouds) {
        for (CloudStatus c: clouds) {
            GridPane cloudPane = cloudList.get(c.getIndex());
            for (int i=0;i<c.getStudents().length;i++) {
                String col = Colour.values()[i].toString().toLowerCase();
                Image sImage = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/student_"+col+".png").toString(),25,25,true,true);
                ImageView imageView = new ImageView(sImage);
                Label sLabel = new Label(""+c.getStudents()[i]);
                cloudPane.add(imageView,0,i);
                cloudPane.add(sLabel,1,i);
            }
        }
    }

    private void myStatus(PlayerStatus ps) {
        playerStatus(ps, myName, myLAC, myTowers, myEntrance, myHall);
        String path;
        if(StatusUpdater.instance().getCurrentStatus().getTurn().getPlayer().equals(StatusUpdater.instance().getCurrentStatus().getPlayerID())) {
            for(int i=0;i<ps.getAssistantCards().length;i++) {
                if(!ps.getAssistantCards()[i]) {
                    path = "images/assistantCards/A";
                    if(i<9) path+="0";
                    path+=(i+1)+".png";
                    Image image = new Image(getClass().getClassLoader().getResource(path).toString(),250,243,true,true);
                    ImageView iView = new ImageView(image);
                    iView.setOnMousePressed(new CommandHandler("PLAYASSISTANTCARD",StatusUpdater.instance().getCurrentStatus().getPlayerID(),i) {
                        @Override
                        public void handle(Event event) {
                            sendAssistantCardCommand();
                        }
                    });
                    ACBox.getChildren().add(iView);
                }
            }
        } else {
            ACBox.setOpacity(0);
            ACPane.setOpacity(0);
        }

    }

    private void opponentStatus(PlayerStatus ps){
        playerStatus(ps, opponentName, opponentLAC, opponentTowers, opponentEntrance, opponentHall);
    }

    private void playerStatus(PlayerStatus ps, Label name, ImageView LAC, GridPane towers, GridPane entrance, GridPane hall) {
        ClassLoader classLoader = getClass().getClassLoader();
        name.setText(ps.getNickName());
        String path;
        if(ps.getLastAssistantCardPlayed()!=null) {
            path = "images/assistantCards/A";
            if(ps.getLastAssistantCardPlayed()<9) path+="0";
            path+=(ps.getLastAssistantCardPlayed()+1)+".png";
            LAC.setImage(new Image(classLoader.getResource(path).toString(),150,193,true,true));
        }
        path = "images/wooden_pieces/"+ps.getTowerColour().toLowerCase()+"_tower.png";
        Image towerImage = new Image(classLoader.getResource(path).toString(),34,31,true,true);
        for(int i=0;i<ps.getNumTowers();i++) {
            ImageView tower = new ImageView(towerImage);
            towers.add(tower,i%2,i/2);
        }
        int studentIndex=0;
        for(int i=0;i<ps.getStudentsOnEntrance().length;i++) {
            String col = Colour.values()[i].toString().toLowerCase();
            Image sImageH = new Image(classLoader.getResource("images/wooden_pieces/student_"+col+".png").toString(),21,30,true,true);
            Image sImageE = new Image(classLoader.getResource("images/wooden_pieces/student_"+col+".png").toString(),25,30,true,false);
            for (int j=0;j<ps.getStudentsOnEntrance()[i];j++) {
                ImageView sE = new ImageView(sImageE);
                entrance.add(sE,studentIndex%2,studentIndex/2);
                studentIndex++;
            }
            for (int j=0;j<ps.getStudentsOnHall()[i];j++) {
                ImageView sH = new ImageView(sImageH);
                hall.add(sH,i,j);
            }
        }
    }



}
