package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class GenericSceneController {
    protected ArrayList<ImageView> myEntranceView,myHallView;
    protected ArrayList<String> myEntranceCol, myHallCol;
    @FXML
    GridPane myEntrance,opponentEntrance,myHall,myProfessors,opponentHall,myTowers,opponentTowers,opponentProfessors;
    @FXML
    ImageView myLAC,opponentLAC;
    @FXML
    ArrayList<GridPane> cloudList,studentPanes;
    @FXML
    ArrayList<ImageView> towersImages,motherNatureImages,bridgeImages,islandImages,cloudImages;
    @FXML
    Label opponentName,myName;

    @FXML
    public void initialize() {
        myEntranceView = new ArrayList<>();
        myHallView = new ArrayList<>();
        myEntranceCol = new ArrayList<>();
        myHallCol = new ArrayList<>();
        CurrentStatus cs = StatusUpdater.instance().getCurrentStatus();
        //Players
        ArrayList<PlayerStatus> players = cs.getGame().getPlayers();
        if(players.get(0).getIndex()==cs.getPlayerID()) {
            playerStatus(players.get(0),cs.getGame().getProfessors(), myName, myLAC, myTowers, myEntrance, myHall, myProfessors);
            playerStatus(players.get(1), cs.getGame().getProfessors(), opponentName, opponentLAC, opponentTowers, opponentEntrance, opponentHall, opponentProfessors);
        } else playerStatus(players.get(0), cs.getGame().getProfessors(), opponentName, opponentLAC, opponentTowers, opponentEntrance, opponentHall, opponentProfessors);
        if(players.get(1).getIndex()==cs.getPlayerID()) playerStatus(players.get(1), cs.getGame().getProfessors(), myName, myLAC, myTowers, myEntrance, myHall, myProfessors);
        //else opponentStatus(players.get(1));
        ControllerUtils.fillClouds(cs.getGame().getClouds(),cloudList,getClass().getClassLoader());
        ControllerUtils.fillIslands(cs.getGame().getArchipelagos(), bridgeImages, studentPanes, towersImages, getClass().getClassLoader());
        motherNatureImages.get(cs.getGame().getMotherNatureIndex()-1).setImage(new Image(getClass().getClassLoader().getResource("images/wooden_pieces/mother_nature.png").toString(),45,45,true,true));
    }

    /*private void fillIslands(ArrayList<ArchipelagoStatus> archipelagos) {
        for (ArchipelagoStatus as: archipelagos) {
            for(int i=0;i<as.getIslands().size();i++) {
                fillIsland(as.getIslands().get(i));
                if(i<as.getIslands().size()-1) {
                    bridgeImages.get(as.getIslands().get(i).getIslandIndex()-1).setOpacity(1);
                }
            }
        }
    }*/

    /*private void fillIsland(IslandStatus islandStatus) {
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
    }*/

    /*private void fillClouds(ArrayList<CloudStatus> clouds) {
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
    }*/

    protected void playerStatus(PlayerStatus ps, int[] professorStatus, Label name, ImageView LAC, GridPane towers, GridPane entrance, GridPane hall, GridPane professors) {
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
            String col = Colour.values()[i].toString();
            Image sImageH = new Image(classLoader.getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),30,30,true,true);
            Image sImageE = new Image(classLoader.getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),33,33,true,false);
            for (int j=0;j<ps.getStudentsOnEntrance()[i];j++) {
                ImageView sE = new ImageView(sImageE);
                myEntranceView.add(sE);
                myEntranceCol.add(col);
                entrance.add(sE,studentIndex%2,studentIndex/2);
                studentIndex++;
            }
            for (int j=0;j<ps.getStudentsOnHall()[i];j++) {
                ImageView sH = new ImageView(sImageH);
                myHallView.add(sH);
                myHallCol.add(col);
                hall.add(sH,j,i);
            }
        }
        ControllerUtils.instance().setMyComponents(myEntranceView,myHallView,myEntranceCol,myHallCol);
        for(int i=0;i<professorStatus.length;i++) {
            String col = Colour.values()[i].toString().toLowerCase();
            path = "images/wooden_pieces/teacher_"+col+".png";
            if(professorStatus[i]>=0 && ps.getTowerColour().equals(Tower.values()[professorStatus[i]].toString())) {
                Image profImage = new Image(classLoader.getResource(path).toString(),44,44,true,true);
                ImageView profView = new ImageView(profImage);
                professors.add(profView,0,i);
            }
        }
        //ControllerUtils.setHall(hall, ps.getStudentsOnEntrance());
    }
}
