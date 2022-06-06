package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.handlers.CommandHandler;
import it.polimi.ingsw.client.gui.handlers.CommandSingleton;
import it.polimi.ingsw.client.gui.handlers.EntranceHandler;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ControllerUtils {
    private ArrayList<ImageView> myEntranceView,myHallView;
    private ArrayList<String> myEntranceCol, myHallCol;
    private static ControllerUtils instance;

    public static ControllerUtils instance() {
        if(instance==null) instance = new ControllerUtils();
        return instance;
    }

    public void setMyComponents(ArrayList<ImageView> myEntranceView, ArrayList<ImageView> myHallView, ArrayList<String> myEntranceCol, ArrayList<String> myHallCol) {
        this.myEntranceView = myEntranceView;
        this.myHallView = myHallView;
        this.myEntranceCol = myEntranceCol;
        this.myHallCol = myHallCol;
    }

    public static void addAssistantCards(HBox ACBox, ScrollPane ACPane, ClassLoader classLoader) {
        PlayerStatus ps = null;
        for (PlayerStatus p: StatusUpdater.instance().getCurrentStatus().getGame().getPlayers()) {
            if(StatusUpdater.instance().getCurrentStatus().getPlayerID().equals(p.getIndex())) {
                ps = p;
                break;
            }
        }
        String path;
        for(int i=0;i<ps.getAssistantCards().length;i++) {
            if(!ps.getAssistantCards()[i]) {
                path = "images/assistantCards/A";
                if(i<9) path+="0";
                path+=(i+1)+".png";
                Image image = new Image(classLoader.getResource(path).toString(),310,310,true,true);
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
        if(!StatusUpdater.instance().getCurrentStatus().getTurn().getPlayer().equals(StatusUpdater.instance().getCurrentStatus().getPlayerID())) {
            // ACBox.setOpacity(0);
            ACPane.setOpacity(0);
            ACPane.setDisable(true);
        }
    }

    public static void addPlanningCharacterCards(ArrayList<ImageView> characterCardsImages, ArrayList<GridPane> studentsOnCardPanes, ArrayList<ImageView> noEntryTileImages, ClassLoader classLoader) {
        ArrayList<CharacterCardStatus> cards = StatusUpdater.instance().getCurrentStatus().getGame().getCharacterCards();
        for(int i=0;i<cards.size();i++) {
            characterCardsImages.get(i).setImage(new Image(classLoader.getResource("images/characterCards/"+cards.get(i).getFileName()).toString(),100,100,true,true));
            if (cards.get(i).getNoEntryTiles()!=null) noEntryTileImages.get(i).setImage(new Image(classLoader.getResource("images/board/noEntryTile.png").toString(),100,100,true,true));
            for(int j=0;j<cards.get(i).getStudents().length;i++) {
                String col = Colour.values()[j].toString().toLowerCase();
                Image sImage = new Image(classLoader.getResource("images/wooden_pieces/student_"+col+".png").toString(),25,30,true,false);
                ImageView iView = new ImageView(sImage);
                studentsOnCardPanes.get(i).add(iView,0,i);
                studentsOnCardPanes.get(i).add(new Label(""+cards.get(i).getStudents()[j]),1,i);
            }
        }
    }

    public void setMyHall(GridPane hall, int[] statusHall, ClassLoader classLoader) {
        myHallCol = new ArrayList<>();
        myHallView = new ArrayList<>();
        while (!hall.getChildren().isEmpty()) hall.getChildren().remove(0);
        for(int i=0;i<statusHall.length;i++) {
            String col = Colour.values()[i].toString();
            Image sImageH = new Image(classLoader.getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),30,30,true,true);
            for (int j=0;j<statusHall[i];j++) {
                ImageView sH = new ImageView(sImageH);
                myHallView.add(sH);
                myHallCol.add(col);
                hall.add(sH,j,i);
            }
        }
        //update hall with listeners?
    }
    public void setMyEntrance(GridPane entrance, int[] statusEntrance,Label textMessage, ClassLoader classLoader) {
        myEntranceCol = new ArrayList<>();
        myEntranceView = new ArrayList<>();
        while (!entrance.getChildren().isEmpty()) entrance.getChildren().remove(0);
        int studentIndex = 0;
        for(int i=0;i<statusEntrance.length;i++) {
            String col = Colour.values()[i].toString();
            Image sImageE = new Image(classLoader.getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),33,33,true,false);
            for (int j=0;j<statusEntrance[i];j++) {
                ImageView sE = new ImageView(sImageE);
                myEntranceView.add(sE);
                myEntranceCol.add(col);
                entrance.add(sE,studentIndex%2,studentIndex/2);
                studentIndex++;
            }
        }
        for(int i=0;i<myEntranceView.size();i++) {
            myEntranceView.get(i).setOnMouseClicked(new EntranceHandler(myEntranceCol.get(i),textMessage));
        }
    }

    public static void fillIslands(ArrayList<ArchipelagoStatus> archipelagos, ArrayList<ImageView> bridgeImages, ArrayList<GridPane> studentPanes, ArrayList<ImageView> towersImages, ClassLoader classLoader) {
        for (ArchipelagoStatus as: archipelagos) {
            for(int i=0;i<as.getIslands().size();i++) {
                fillIsland(studentPanes.get(as.getIslands().get(i).getIslandIndex()-1), towersImages.get(as.getIslands().get(i).getIslandIndex()-1), as.getIslands().get(i), classLoader);
                if(i<as.getIslands().size()-1) {
                    bridgeImages.get(as.getIslands().get(i).getIslandIndex()-1).setOpacity(1);
                }
            }
        }
    }

    private static void fillIsland(GridPane studentPane, ImageView towersImage ,IslandStatus islandStatus, ClassLoader classLoader) {
        while (!studentPane.getChildren().isEmpty()) studentPane.getChildren().remove(0);
        for (int i=0;i<islandStatus.getStudents().length;i++) {
            String col = Colour.values()[i].toString().toLowerCase();
            Image sImage = new Image(classLoader.getResource("images/wooden_pieces/student_"+col+".png").toString(),25,25,true,true);
            ImageView imageView = new ImageView(sImage);
            Label sLabel = new Label(""+islandStatus.getStudents()[i]);
            studentPane.add(imageView,0,i);
            studentPane.add(sLabel,1,i);
            if(islandStatus.getTowerColour()!=null) {
                Image tower = new Image(classLoader.getResource("images/wooden_pieces/"+islandStatus.getTowerColour().toLowerCase()+"_tower.png").toString(),40,50,true,true);
                towersImage.setImage(tower);
            }
        }
    }

    public static void fillClouds(ArrayList<CloudStatus> clouds, ArrayList<GridPane> cloudPanes, ClassLoader classLoader) {
        for (CloudStatus c: clouds) {
            GridPane cloudPane = cloudPanes.get(c.getIndex());
            while (!cloudPane.getChildren().isEmpty()) cloudPane.getChildren().remove(0);
            for (int i=0;i<c.getStudents().length;i++) {
                String col = Colour.values()[i].toString().toLowerCase();
                Image sImage = new Image(classLoader.getResource("images/wooden_pieces/student_"+col+".png").toString(),25,25,true,true);
                ImageView imageView = new ImageView(sImage);
                Label sLabel = new Label(""+c.getStudents()[i]);
                cloudPane.add(imageView,0,i);
                cloudPane.add(sLabel,1,i);
            }
        }
    }

    public static void fillProfessors(ArrayList<PlayerStatus> ps, int[] profStatus, int currPlayer, Scene scene, ClassLoader classLoader) {
        boolean opp2 = false;
        for(int i=0;i<ps.size();i++) {
            if(ps.get(i).getIndex()==currPlayer) fillProfessors(ps.get(i),profStatus,(GridPane) scene.lookup("#myProfessors"),classLoader);
            else if (!opp2) {
                fillProfessors(ps.get(i),profStatus,(GridPane) scene.lookup("#opponentProfessors"),classLoader);
                opp2 = true;
            } else fillProfessors(ps.get(i),profStatus,(GridPane) scene.lookup("#opponent2Professors"),classLoader);
        }
    }

    private static void fillProfessors(PlayerStatus ps, int[] profStatus, GridPane profPane, ClassLoader classLoader) {
        while (!profPane.getChildren().isEmpty()) profPane.getChildren().remove(0);
        for(int i=0;i<profStatus.length;i++) {
            String col = Colour.values()[i].toString().toLowerCase();
            String path = "images/wooden_pieces/teacher_"+col+".png";
            if(profStatus[i]>=0 && ps.getTowerColour().equals(Tower.values()[profStatus[i]].toString())) {
                Image profImage = new Image(classLoader.getResource(path).toString(),44,44,true,true);
                ImageView profView = new ImageView(profImage);
                profPane.add(profView,0,i);
            }
        }
    }
    public static void moveToIsland(Label textMessage) {
        CommandSingleton.instance().newCommand("MOVETOISLAND");
        CommandSingleton.instance().getPhases().add(GamePhases.P_STUDENT_COLOUR);
        CommandSingleton.instance().getPhases().add(GamePhases.P_ISLAND_INDEX);
        CommandSingleton.instance().getPhases().add(GamePhases.SENDCOMMAND);
        System.out.println(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
        textMessage.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
    }

    public static void moveToHall(Label textMessage) {
        CommandSingleton.instance().newCommand("MOVETOHALL");
        CommandSingleton.instance().getPhases().add(GamePhases.P_STUDENT_COLOUR);
        CommandSingleton.instance().getPhases().add(GamePhases.SENDCOMMAND);
        System.out.println(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
        textMessage.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
    }

    public static void moveMotherNature(Label textMessage) {
        CommandSingleton.instance().newCommand("MOVEMOTHERNATURE");
        CommandSingleton.instance().getPhases().add(GamePhases.P_MNSHIFTS);
        CommandSingleton.instance().getPhases().add(GamePhases.SENDCOMMAND);
        textMessage.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
    }

    public static void takeFromCloud(Label textMessage) {
        CommandSingleton.instance().newCommand("TAKEFROMCLOUD");
        CommandSingleton.instance().getPhases().add(GamePhases.P_CLOUD_INDEX);
        CommandSingleton.instance().getPhases().add(GamePhases.SENDCOMMAND);
        textMessage.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
    }


    public ArrayList<ImageView> getMyEntranceView() {
        return myEntranceView;
    }
    public ArrayList<ImageView> getMyHallView() {
        return myHallView;
    }
    public ArrayList<String> getMyEntranceCol() {
        return myEntranceCol;
    }
    public ArrayList<String> getMyHallCol() {
        return myHallCol;
    }
}
