package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.handlers.*;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerUtils {
    private ArrayList<ImageView> myEntranceView,myHallView;
    private ArrayList<String> myEntranceCol, myHallCol;
    private static ControllerUtils instance;

    public static ControllerUtils instance() {
        if(instance==null) instance = new ControllerUtils();
        return instance;
    }

    public static void addCoins(Label myCoins, Label opponentCoins) {
        ArrayList<PlayerStatus> ps = StatusUpdater.instance().getCurrentStatus().getGame().getPlayers();
        int myID = StatusUpdater.instance().getCurrentStatus().getPlayerID();
        if(myID==ps.get(0).getIndex()) {
            myCoins.setText(ps.get(0).getCoins()+"");
            opponentCoins.setText(ps.get(1).getCoins()+"");
        } else {
            if(myID==ps.get(1).getIndex()) {
                myCoins.setText(ps.get(1).getCoins()+"");
            }
            opponentCoins.setText(ps.get(0).getCoins()+"");
        }
    }

    public static void addCoins(Label myCoins, Label opponentCoins, Label opponent2Coins) {
        addCoins(myCoins, opponentCoins);
        ArrayList<PlayerStatus> ps = StatusUpdater.instance().getCurrentStatus().getGame().getPlayers();
        int myID = StatusUpdater.instance().getCurrentStatus().getPlayerID();
        if(myID==ps.get(2).getIndex()) {
            opponent2Coins.setText(ps.get(1).getCoins()+"");
            myCoins.setText(ps.get(2).getCoins()+"");
        } else {
            opponent2Coins.setText(ps.get(2).getCoins()+"");
        }
    }

    public static void setCardEffect(ImageView cardIV, Label textMessage, Button confirm, ClassLoader classLoader) {
        String cardName = cardIV.getImage().getUrl().substring(cardIV.getImage().getUrl().length()-7);
        ArrayList<GamePhases> phases = CommandSingleton.instance().getPhases();
        switch (cardName) {
            case "P01.jpg" -> {
                phases.add(GamePhases.GUI_STUDENT_ON_CARD);
                phases.add(GamePhases.PCC_ISLAND_INDEX);
                phases.add(GamePhases.SENDCOMMAND);
            }
            case "P03.jpg","P05.jpg" -> {
                phases.add(GamePhases.PCC_ISLAND_INDEX);
                phases.add(GamePhases.SENDCOMMAND);
            }
            case "P07.jpg" -> {
                phases.add(GamePhases.GUI_GROUPS_CARD_ENTRANCE);
                confirm.setOpacity(1);
                confirm.setDisable(false);
            }
            case "P10.jpg" -> {
                phases.add(GamePhases.GUI_GROUPS_ENTRANCE_HALL);
                confirm.setOpacity(1);
                confirm.setDisable(false);
            }
            case "P11.jpg" -> {
                phases.add(GamePhases.GUI_STUDENT_ON_CARD);
                phases.add(GamePhases.SENDCOMMAND);
            }
            case "P09.jpg","P12.jpg" -> {
                phases.add(GamePhases.PCC_STUDENT_COLOUR);
                Stage stage = new Stage();
                Parent root;
                try {
                    root = FXMLLoader.load(classLoader.getResource("fxml/ColourChoiceScene.fxml"));
                    stage.setScene(new Scene(root));
                    stage.initOwner(confirm.getScene().getWindow());
                    ChoiceBox colourBox = (ChoiceBox) stage.getScene().lookup("#colourChoice");
                    ObservableList<String> colours = FXCollections.observableArrayList("YELLOW","GREEN","BLUE","PINK","RED");
                    colourBox.setItems(colours);
                    colourBox.setValue("YELLOW");
                    Button colourConfirm = (Button) stage.getScene().lookup("#colourConfirm");
                    colourConfirm.setOnMouseClicked(new ColourConfirmHandler(colourBox));
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> phases.add(GamePhases.SENDCOMMAND);
        }
        CommandSingleton.instance().nextPhase();
        textMessage.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
        if(CommandSingleton.instance().getPhases().get(0) == GamePhases.SENDCOMMAND) NetworkManager.instance().sendJSON(CommandSingleton.instance().getCommand());
    }

    public static void setNETOnIsland(ArrayList<ImageView> netIslandImages, ArrayList<Label> netIslandLabels) {
        for (ImageView netIslandImage : netIslandImages) netIslandImage.setOpacity(0);
        for (Label netIslandLabel : netIslandLabels){
            netIslandLabel.setText("0");
            netIslandLabel.setOpacity(0);
        }
        ArrayList<ArchipelagoStatus> archipelagos = StatusUpdater.instance().getCurrentStatus().getGame().getArchipelagos();
        for (ArchipelagoStatus archipelago : archipelagos) {
            ArrayList<IslandStatus> islands = archipelago.getIslands();
            if (archipelago.getNoEntryTiles() != null && archipelago.getNoEntryTiles() > 0) {
                int islandIndex = islands.get(0).getIslandIndex();
                netIslandImages.get(islandIndex - 1).setOpacity(1);
                netIslandLabels.get(islandIndex - 1).setText(""+archipelago.getNoEntryTiles());
                netIslandLabels.get(islandIndex - 1).setOpacity(1);
            }
        }
    }

    public static void resetOpacity(GridPane myEntrance, GridPane myHall, ArrayList<GridPane> studentsOnCardPanes) {
        for (Node child: myEntrance.getChildren()) {
            child.setOpacity(1);
        }
        for (Node child: myHall.getChildren()) {
            child.setOpacity(1);
        }
        for(GridPane gp: studentsOnCardPanes) {
            for (Node child: gp.getChildren()) {
                child.setOpacity(1);
            }
        }
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

    public static void addPlanningCharacterCards(ArrayList<ImageView> characterCardsImages, ArrayList<GridPane> studentsOnCardPanes, ArrayList<ImageView> noEntryTileImages, ArrayList<Label> noEntryTileLabels, ArrayList<ImageView> coinOnCards, Label textLabel,ClassLoader classLoader) {
        addCommonCharacterCards(25,30,characterCardsImages,studentsOnCardPanes,noEntryTileImages,noEntryTileLabels,coinOnCards,textLabel,classLoader);
    }

    public static void addActionCharacterCards(ArrayList<ImageView> characterCardsImages, ArrayList<GridPane> studentsOnCardPanes, ArrayList<ImageView> noEntryTileImages, ArrayList<Label> noEntryTileLabels, ArrayList<ImageView> coinOnCards,Label textLabel, ClassLoader classLoader) {
        addCommonCharacterCards(40,40,characterCardsImages,studentsOnCardPanes,noEntryTileImages,noEntryTileLabels,coinOnCards,textLabel,classLoader);
    }

    public static void addCommonCharacterCards(int sWidth, int sHeight, ArrayList<ImageView> characterCardsImages, ArrayList<GridPane> studentsOnCardPanes, ArrayList<ImageView> noEntryTileImages, ArrayList<Label> noEntryTileLabels, ArrayList<ImageView> coinOnCards, Label textLabel, ClassLoader classLoader) {
        ArrayList<CharacterCardStatus> cards = StatusUpdater.instance().getCurrentStatus().getGame().getCharacterCards();
        for(int i=0;i<cards.size();i++) {
            coinOnCards.get(i).setOpacity(cards.get(i).isCoinOnIt() ? 1 : 0);
            characterCardsImages.get(i).setImage(new Image(classLoader.getResource("images/characterCards/"+cards.get(i).getFileName()).toString(),characterCardsImages.get(i).getFitWidth(),characterCardsImages.get(i).getFitHeight(),true,true));
            if (cards.get(i).getFileName().equals("P05.jpg")) {
                noEntryTileLabels.get(i).setText(""+cards.get(i).getNoEntryTiles());
                noEntryTileLabels.get(i).setOpacity(1);
                noEntryTileImages.get(i).setOpacity(1);
            } else {
                noEntryTileLabels.get(i).setOpacity(0);
                noEntryTileImages.get(i).setOpacity(0);
            }
            int numStudents=0;
            for(int j=0;j<cards.get(i).getStudents().length;j++) {
                String col = Colour.values()[j].toString().toLowerCase();
                for(int k=0;k<cards.get(i).getStudents()[j];k++) {
                    Image sImage = new Image(classLoader.getResource("images/wooden_pieces/student_"+col+".png").toString(),sWidth,sHeight,true,true);
                    ImageView iView = new ImageView(sImage);
                    if(StatusUpdater.instance().getCurrentStatus().getTurn().getPhase().equals("ACTION")) iView.setOnMouseClicked(new CCardHandler(i,col,textLabel));
                    studentsOnCardPanes.get(i).add(iView,numStudents%2,numStudents/2);
                    numStudents++;
                }
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
                sH.setOnMouseClicked(new HallHandler(col));
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
            sLabel.setFont(new Font(18));
            studentPane.add(imageView,0,i);
            studentPane.add(sLabel,1,i);
            if(islandStatus.getTowerColour()!=null) {
                Image tower = new Image(classLoader.getResource("images/wooden_pieces/"+islandStatus.getTowerColour().toLowerCase()+"_tower.png").toString(),50,50,true,true);
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
                sLabel.setFont(new Font(18));
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

    public static void playCharacterCard(Label textMessage) {
        CommandSingleton.instance().newCommand("PLAYCHARACTERCARD");
        CommandSingleton.instance().getPhases().add(GamePhases.P_CCARD_INDEX);
        System.out.println(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
        textMessage.setText(CommandSingleton.instance().getPhases().get(0).getGUIPrompt());
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
