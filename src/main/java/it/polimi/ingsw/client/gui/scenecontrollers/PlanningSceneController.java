package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.network.CloudStatus;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;

public class PlanningSceneController {
    @FXML
    GridPane myEntrance,opponentEntrance,myHall,opponentHall,myTowers,opponentTowers;
    //@FXML
    //ArrayList<GridPane> cloudList;
    @FXML
    ImageView myLAC,opponentLAC;
    @FXML
    HBox ACBox;
    @FXML
    Label opponentName,myName;

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
        //fillClouds(cs.getGame().getClouds());
    }

    /*private void fillClouds(ArrayList<CloudStatus> clouds) {
        for (CloudStatus c: clouds) {
            GridPane cloudPane = cloudList.get(c.getIndex());
            for (int i=0;i<c.getStudents().length;i++) {
                String col = Colour.values()[i].toString().toLowerCase();
                Image sImage = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/student_"+col+".png").toString(),30,20,true,true);
                ImageView imageView = new ImageView(sImage);
                Label sLabel = new Label(""+c.getStudents()[i]);
                cloudPane.add(imageView,0,i);
                cloudPane.add(sLabel,1,i);
            }
        }
    }*/

    private void myStatus(PlayerStatus ps) {
        playerStatus(ps, myName, myLAC, myTowers, myEntrance, myHall);
        String path;
        for(int i=0;i<ps.getAssistantCards().length;i++) {
            if(!ps.getAssistantCards()[i]) {
                path = "images/assistantCards/A";
                if(i<9) path+="0";
                path+=(i+1)+".png";
                Image image = new Image(getClass().getClassLoader().getResource(path).toString(),201,200,true,true);
                ImageView iView = new ImageView(image);
                ACBox.getChildren().add(iView);
            }
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
