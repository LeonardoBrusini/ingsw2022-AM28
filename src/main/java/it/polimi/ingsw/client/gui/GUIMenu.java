package it.polimi.ingsw.client.gui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.client.ClientObserver;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.handlers.CCardHandler;
import it.polimi.ingsw.client.gui.handlers.IslandHandler;
import it.polimi.ingsw.client.gui.scenecontrollers.ControllerUtils;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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
        currentStatus = null;
    }

    @Override
    public void manageMessage(String line) {
        if (currentStatus!=null && checkWinner()) return;
        switch (currentScene) {
            case USERNAME -> manageAPR(line);
            case GAME_PARAMETERS -> manageGPR(line);
            case PLANNING_2_SIMPLE,PLANNING_2_EXPERT,PLANNING_3_EXPERT,PLANNING_3_SIMPLE -> manageCSPlanning(line);
            case ACTION_2_EXPERT,ACTION_3_EXPERT,ACTION_2_SIMPLE,ACTION_3_SIMPLE -> {
                Label textMessage = (Label) stage.getScene().lookup("#textMessage");
                manageCSAction(textMessage,line);
            }
        }
    }

    @Override
    public void manageDisconnection() {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Connection error, can't reach server.");
            a.initOwner(stage);
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

    private void manageCSAction(Label textMessage, String line) {
        CurrentStatus cs = parser.fromJson(line, CurrentStatus.class);
        if(cs.getStatus()!=0) {
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText(cs.getErrorMessage());
                a.initOwner(stage);
                a.show();
            });
            return;
        }
        StatusUpdater.instance().updateStatus(cs);
        currentStatus = StatusUpdater.instance().getCurrentStatus();
        if(currentStatus.getTurn().getPhase().equals("PLANNING")) {
            changePhaseScene();
        } else {
            if(cs.getLastCommand()==null) disconnectionUpdate(cs);
            else {
                switch (cs.getLastCommand()) {
                    case "MOVETOISLAND" -> updateMoveToIsland(cs);
                    case "MOVETOHALL" -> updateMoveToHall(cs);
                    case "MOVEMOTHERNATURE" -> updateMoveMotherNature(cs);
                    case "TAKEFROMCLOUD" -> updateTakeFromCloud(cs);
                    case "PLAYCHARACTERCARD" -> updatePlayCharacterCard(cs, textMessage);
                }
            }
        }
        checkWinner();
        Platform.runLater(() -> {
            if(currentStatus.getTurn().getPlayer().equals(currentStatus.getPlayerID())) textMessage.setText("Select one of the following moves:");
            else textMessage.setText(GamePhases.WAIT.getGUIPrompt());
        });
    }

    private void updatePlayCharacterCard(CurrentStatus cs, Label textMessage) {
        CharacterCardStatus ccs = cs.getGame().getCharacterCards().get(0);
        ImageView cardActivated = (ImageView) stage.getScene().lookup("#characterCard"+(ccs.getIndex()+1));
        GridPane studentsOnCard = (GridPane) stage.getScene().lookup("#ccStudents"+(ccs.getIndex()+1));
        ImageView coinOnCard = (ImageView) stage.getScene().lookup("#coc"+(ccs.getIndex()+1));
        ImageView net = (ImageView) stage.getScene().lookup("#netImage"+(ccs.getIndex()+1));
        Label numNET = (Label) stage.getScene().lookup("#numNET"+(ccs.getIndex()+1));
        updateCard(ccs,studentsOnCard,coinOnCard,net,numNET,textMessage);

        String fileName = cardActivated.getImage().getUrl().substring(cardActivated.getImage().getUrl().length()-7);
        System.out.println(fileName);
        switch (fileName) {
            case "P01.jpg" -> updateIsland(cs);
            case "P03.jpg" -> updateMoveMotherNature(cs);
            case "P05.jpg" -> updateNETOnIsland(cs);
            case "P07.jpg" -> {
                PlayerStatus ps = cs.getGame().getPlayers().get(0);
                updateEntrance(ps.getIndex(),ps.getStudentsOnEntrance());
            }
            case "P10.jpg" -> updateMoveToHall(cs);
            case "P11.jpg" -> {
                PlayerStatus ps = cs.getGame().getPlayers().get(0);
                updateHall(ps.getIndex(),ps.getStudentsOnHall());
            }
            case "P12.jpg" -> updateAllHalls(cs);
        }

        updateAllPlayersCoins(cs.getGame().getPlayers());
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("CARD "+(ccs.getIndex()+1)+" HAS BEEN ACTIVATED!");
            a.initOwner(stage);
            a.show();
        });
    }

    private void updateAllPlayersCoins(ArrayList<PlayerStatus> players) {
        for(PlayerStatus p: players) {
            updatePlayerCoins(p.getIndex());
        }
    }

    private void updatePlayerCoins(int playerIndex) {
        Label coins = null;
        if(currentStatus.getPlayerID().equals(playerIndex)) {
            coins = (Label) stage.getScene().lookup("#myCoins");
        } else {
            if(currentStatus.getPlayerID()>0) {
                if(playerIndex==0) {
                    coins = (Label) stage.getScene().lookup("#opponentCoins");
                } else {
                    coins = (Label) stage.getScene().lookup("#opponent2Coins");
                }
            } else {
                if(playerIndex==1) {
                    coins = (Label) stage.getScene().lookup("#opponentCoins");
                } else {
                    coins = (Label) stage.getScene().lookup("#opponent2Coins");
                }
            }
        }
        Label finalCoins = coins;
        Platform.runLater(() -> {
            if(currentStatus.getGameMode().equals("expert")) finalCoins.setText(""+currentStatus.getGame().getPlayers().get(playerIndex).getCoins());
        });
    }

    private void updateAllHalls(CurrentStatus cs) {
        Platform.runLater(() -> ControllerUtils.fillProfessors(currentStatus.getGame().getPlayers(),currentStatus.getGame().getProfessors(),currentStatus.getPlayerID(),stage.getScene(),getClass().getClassLoader()));
        for(PlayerStatus ps: cs.getGame().getPlayers()) {
            updateHall(ps.getIndex(),ps.getStudentsOnHall());
        }
    }

    private void updateHall(int playerIndex, int[] studentsOnHall) {
        GridPane hall;
        if(currentStatus.getPlayerID().equals(playerIndex)) {
            hall = (GridPane) stage.getScene().lookup("#myHall");
            Label textMessage = (Label) stage.getScene().lookup("#textMessage");
            Platform.runLater(() -> {
                ControllerUtils.instance().setMyHall(hall, studentsOnHall,getClass().getClassLoader());
            });
        } else {
            if(currentStatus.getPlayerID()>0) {
                if(playerIndex==0) {
                    hall = (GridPane) stage.getScene().lookup("#opponentHall");
                } else {
                    hall = (GridPane) stage.getScene().lookup("#opponent2Hall");
                }
            } else {
                if(playerIndex==1) {
                    hall = (GridPane) stage.getScene().lookup("#opponentHall");
                } else {
                    hall = (GridPane) stage.getScene().lookup("#opponent2Hall");
                }
            }
            updateOpponentHall(hall, studentsOnHall);
        }
    }

    private void updateNETOnIsland(CurrentStatus cs) {
        ArchipelagoStatus archipelago = currentStatus.getGame().getArchipelagos().get(cs.getGame().getArchipelagos().get(0).getIndex());
        int islandIndex = archipelago.getIslands().get(0).getIslandIndex();
        ImageView netImage = (ImageView) stage.getScene().lookup("#netIslandImage"+islandIndex);
        Label netLabel = (Label) stage.getScene().lookup("#netIslandLabel"+islandIndex);
        int oldValue = Integer.parseInt(netLabel.getText());
        Platform.runLater(() -> {
            netLabel.setText(""+(oldValue+1));
            netImage.setOpacity(1);
            netLabel.setOpacity(1);
        });
    }

    private void disconnectionUpdate(CurrentStatus cs) {
        if(currentStatus.getTurn().getPhase().equalsIgnoreCase("PLANNING")) {
            Platform.runLater(() -> changePhaseScene());
        } else {
            actionButtonsDebilitation();
        }
    }

    private void actionButtonsDebilitation() {
        boolean notMyTurn = !currentStatus.getTurn().getPlayer().equals(currentStatus.getPlayerID());
        stage.getScene().lookup("#moveToIslandButton").setDisable(notMyTurn);
        stage.getScene().lookup("#moveToHallButton").setDisable(notMyTurn);
        stage.getScene().lookup("#moveMotherNatureButton").setDisable(notMyTurn);
        stage.getScene().lookup("#takeFromCloudButton").setDisable(notMyTurn);
        if(currentStatus.getGameMode().equals("expert")) stage.getScene().lookup("#playCharacterCardButton").setDisable(notMyTurn);
    }

    private void updateTakeFromCloud(CurrentStatus cs) {
        ArrayList<GridPane> cloudPanes = new ArrayList<>();
        for(int i=0;i<cs.getGame().getClouds().size();i++) {
            cloudPanes.add((GridPane) stage.getScene().lookup("#cloud"+(i+1)));
        }
        Platform.runLater(() -> {
            ControllerUtils.fillClouds(cs.getGame().getClouds(),cloudPanes,getClass().getClassLoader());
        });
        int playerIndex = cs.getGame().getPlayers().get(0).getIndex();
        int[] statusEntrance = cs.getGame().getPlayers().get(0).getStudentsOnEntrance();
        updateEntrance(playerIndex, statusEntrance);
        actionButtonsDebilitation();
    }

    private void updateCard(CharacterCardStatus ccs, GridPane studentsOnCard, ImageView coin, ImageView net, Label netLabel, Label textMessage) {
        Platform.runLater(() -> {
            coin.setOpacity(ccs.isCoinOnIt() ? 1 : 0);
            if(ccs.getNoEntryTiles()!=null && ccs.getNoEntryTiles()>0) {
                net.setOpacity(1);
                netLabel.setText(""+ccs.getNoEntryTiles());
                netLabel.setOpacity(1);
            }
            int childrenIndex = 0;
            for(int i=0;i<ccs.getStudents().length;i++) {
                String col = Colour.values()[i].toString();
                Image sImageE = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),40,40,true,false);
                for (int j=0;j<ccs.getStudents()[i];j++) {
                    ImageView sE;
                    if(childrenIndex<studentsOnCard.getChildren().size()) {
                        sE = (ImageView) studentsOnCard.getChildren().get(childrenIndex);
                        sE.setImage(sImageE);
                    } else {
                        sE = new ImageView();
                        sE.setImage(sImageE);
                        studentsOnCard.add(sE,childrenIndex%2,childrenIndex/2);
                    }
                    sE.setOnMouseClicked(new CCardHandler(ccs.getIndex(),col,textMessage));
                    childrenIndex++;
                }
            }
        });
    }

    private void updateEntrance(int playerIndex, int[] statusEntrance) {
        GridPane entrance;
        if(currentStatus.getPlayerID().equals(playerIndex)) {
            entrance = (GridPane) stage.getScene().lookup("#myEntrance");
            Label textMessage = (Label) stage.getScene().lookup("#textMessage");
            Platform.runLater(() -> ControllerUtils.instance().setMyEntrance(entrance,statusEntrance,textMessage,getClass().getClassLoader()));
        } else {
            if(currentStatus.getPlayerID()>0) {
                if(playerIndex==0) entrance = (GridPane) stage.getScene().lookup("#opponentEntrance");
                else entrance = (GridPane) stage.getScene().lookup("#opponent2Entrance");
            } else {
                if(playerIndex==1) entrance = (GridPane) stage.getScene().lookup("#opponentEntrance");
                else entrance = (GridPane) stage.getScene().lookup("#opponent2Entrance");
            }
            updateOpponentEntrance(entrance, statusEntrance);
        }
    }

    private void updateMoveMotherNature(CurrentStatus cs) {
        ArrayList<ImageView> towersImages = new ArrayList<>();
        ArrayList<ImageView> motherNatureImages = new ArrayList<>();
        ArrayList<ImageView> bridgeImages = new ArrayList<>();
        ArrayList<GridPane> studentPanes = new ArrayList<>();
        Label textMessage = (Label) stage.getScene().lookup("#textMessage");
        for(int i=1;i<=12;i++) {
            towersImages.add((ImageView) stage.getScene().lookup("#tower"+i));
            motherNatureImages.add((ImageView) stage.getScene().lookup("#mn"+i));
            bridgeImages.add((ImageView) stage.getScene().lookup("#bridge"+i));
            studentPanes.add((GridPane) stage.getScene().lookup("#students"+i));
        }
        Platform.runLater(() -> {
            ControllerUtils.fillIslands(cs.getGame().getArchipelagos(),bridgeImages,studentPanes,towersImages,getClass().getClassLoader());
            for(int i=0;i<12;i++) {
                studentPanes.get(i).setOnMouseClicked(new IslandHandler(i+1,textMessage));
                motherNatureImages.get(i).setImage(null);
            }
            motherNatureImages.get(cs.getGame().getMotherNatureIndex()-1).setImage(new Image(getClass().getClassLoader().getResource("images/wooden_pieces/mother_nature.png").toString(),45,45,true,true));
        });
        boolean opp2 = false;
        for(int i=0;i<cs.getGame().getPlayers().size();i++) {
            GridPane towerPane;
            PlayerStatus ps = cs.getGame().getPlayers().get(i);
            if(currentStatus.getPlayerID().equals(ps.getIndex())) towerPane = (GridPane) stage.getScene().lookup("#myTowers");
            else if (!opp2) {
                towerPane = (GridPane) stage.getScene().lookup("#opponentTowers");
                opp2 = true;
            } else {
                towerPane = (GridPane) stage.getScene().lookup("#opponent2Towers");
            }
            int finalI = i;
            Platform.runLater(() -> {
                while (!towerPane.getChildren().isEmpty()) towerPane.getChildren().remove(0);
                String path = "images/wooden_pieces/"+currentStatus.getGame().getPlayers().get(cs.getGame().getPlayers().get(finalI).getIndex()).getTowerColour().toLowerCase()+"_tower.png";
                Image towerImage = new Image(getClass().getClassLoader().getResource(path).toString(),34,31,true,true);
                for(int j=0;j<ps.getNumTowers();j++) {
                    ImageView tower = new ImageView(towerImage);
                    towerPane.add(tower,j%2,j/2);
                }
            });
        }
        if(currentStatus.getGameMode().equals("expert")) updateNETOnAllIslands();
    }

    private void updateNETOnAllIslands() {
        ArrayList<ArchipelagoStatus> archipelagos = currentStatus.getGame().getArchipelagos();
        for(ArchipelagoStatus a: archipelagos) {
            int firstIslandIndex = a.getIslands().get(0).getIslandIndex();
            ImageView netImage = (ImageView) stage.getScene().lookup("#netIslandImage"+firstIslandIndex);
            Label netLabel = (Label) stage.getScene().lookup("#netIslandLabel"+firstIslandIndex);
            if(a.getNoEntryTiles()!=null && a.getNoEntryTiles()>0) {
                netImage.setOpacity(1);
                netLabel.setText(a.getNoEntryTiles()+"");
                netLabel.setOpacity(1);
            } else {
                netImage.setOpacity(0);
                netLabel.setText("0");
                netLabel.setOpacity(0);
            }
            for (int i=1;i<a.getIslands().size();i++) {
                netImage = (ImageView) stage.getScene().lookup("#netIslandImage"+a.getIslands().get(i).getIslandIndex());
                netLabel = (Label) stage.getScene().lookup("#netIslandLabel"+a.getIslands().get(i).getIslandIndex());
                netImage.setOpacity(0);
                netLabel.setText("0");
                netLabel.setOpacity(0);
            }
        }
    }

    private void updateMoveToHall(CurrentStatus cs) {
        //MUST BE UPDATED ON EXPERT MODE
        //GridPane entrance,hall,professors;
        PlayerStatus ps = null;
        for(int i=0;i<currentStatus.getGame().getPlayers().size();i++) {
            if(cs.getGame().getPlayers().get(0).getIndex()==currentStatus.getGame().getPlayers().get(i).getIndex()) {
                ps = currentStatus.getGame().getPlayers().get(i);
                break;
            }
        }
        Platform.runLater(() -> ControllerUtils.fillProfessors(currentStatus.getGame().getPlayers(),currentStatus.getGame().getProfessors(),currentStatus.getPlayerID(),stage.getScene(),getClass().getClassLoader()));
        updateHall(ps.getIndex(), ps.getStudentsOnHall());
        updateEntrance(ps.getIndex(),ps.getStudentsOnEntrance());
        if(currentStatus.getGameMode().equals("expert")) updatePlayerCoins(ps.getIndex());
        /*Label coins = null;
        int[] statusEntrance = cs.getGame().getPlayers().get(0).getStudentsOnEntrance();
        int[] statusHall = cs.getGame().getPlayers().get(0).getStudentsOnHall();
        int playerIndex = cs.getGame().getPlayers().get(0).getIndex();
        if(currentStatus.getPlayerID().equals(playerIndex)) {
            entrance = (GridPane) stage.getScene().lookup("#myEntrance");
            hall = (GridPane) stage.getScene().lookup("#myHall");
            //professors = (GridPane) stage.getScene().lookup("#myProfessors");
            if(currentStatus.getGameMode().equals("expert")) coins = (Label) stage.getScene().lookup("#myCoins");
            Label textMessage = (Label) stage.getScene().lookup("#textMessage");
            PlayerStatus finalPs1 = ps;
            Platform.runLater(() -> {
                ControllerUtils.instance().setMyEntrance(entrance, statusEntrance, textMessage, getClass().getClassLoader());
                ControllerUtils.instance().setMyHall(hall, statusHall,getClass().getClassLoader());
            });
        } else {
            if(currentStatus.getPlayerID()>0) {
                if(playerIndex==0) {
                    entrance = (GridPane) stage.getScene().lookup("#opponentEntrance");
                    hall = (GridPane) stage.getScene().lookup("#opponentHall");
                    //professors = (GridPane) stage.getScene().lookup("#opponentProfessors");
                    if(currentStatus.getGameMode().equals("expert")) coins = (Label) stage.getScene().lookup("opponentCoins");
                } else {
                    entrance = (GridPane) stage.getScene().lookup("#opponent2Entrance");
                    hall = (GridPane) stage.getScene().lookup("#opponent2Hall");
                    //professors = (GridPane) stage.getScene().lookup("#opponent2Professors");
                    if(currentStatus.getGameMode().equals("expert")) coins = (Label) stage.getScene().lookup("opponent2Coins");
                }
            } else {
                if(playerIndex==1) {
                    entrance = (GridPane) stage.getScene().lookup("#opponentEntrance");
                    hall = (GridPane) stage.getScene().lookup("#opponentHall");
                    //professors = (GridPane) stage.getScene().lookup("#opponentProfessors");
                    if(currentStatus.getGameMode().equals("expert")) coins = (Label) stage.getScene().lookup("opponentCoins");
                } else {
                    entrance = (GridPane) stage.getScene().lookup("#opponent2Entrance");
                    hall = (GridPane) stage.getScene().lookup("#opponent2Hall");
                    //professors = (GridPane) stage.getScene().lookup("#opponent2Professors");
                    if(currentStatus.getGameMode().equals("expert")) coins = (Label) stage.getScene().lookup("opponent2Coins");
                }
            }
            updateOpponentEntrance(entrance, statusEntrance);
            updateOpponentHall(hall, statusHall);
        }
        Label finalCoins = coins;
        //PlayerStatus finalPs = ps;
        Platform.runLater(() -> {
            if(currentStatus.getGameMode().equals("expert")) finalCoins.setText(""+cs.getGame().getPlayers().get(0).getCoins());
        });*/
    }

    private void updateOpponentHall(GridPane hall, int[] statusHall) {
        Platform.runLater(() ->{
            while (!hall.getChildren().isEmpty()) hall.getChildren().remove(0);
            for(int i=0;i<statusHall.length;i++) {
                String col = Colour.values()[i].toString();
                Image sImageH = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),30,30,true,true);
                for (int j=0;j<statusHall[i];j++) {
                    ImageView sH = new ImageView(sImageH);
                    hall.add(sH,j,i);
                }
            }
        });
    }
    private void updateOpponentEntrance(GridPane entrance, int[] statusEntrance) {
        Platform.runLater(() ->{
            int childrenIndex = 0;
            boolean addedStudent = false;
            for(int i=0;i<statusEntrance.length;i++) {
                String col = Colour.values()[i].toString();
                Image sImageE = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),33,33,true,false);
                for (int j=0;j<statusEntrance[i];j++) {
                    ImageView sE;
                    if(childrenIndex<entrance.getChildren().size()) {
                        sE = (ImageView) entrance.getChildren().get(childrenIndex);
                        sE.setImage(sImageE);
                    } else {
                        addedStudent = true;
                        sE = new ImageView();
                        sE.setImage(sImageE);
                        entrance.add(sE,childrenIndex%2,childrenIndex/2);
                    }
                    childrenIndex++;
                }
            }
            if(!addedStudent) entrance.getChildren().remove(childrenIndex);
        });
    }
    private void updateMoveToIsland(CurrentStatus cs) {
        int playerIndex = cs.getGame().getPlayers().get(0).getIndex();
        int[] statusEntrance = cs.getGame().getPlayers().get(0).getStudentsOnEntrance();
        updateEntrance(playerIndex, statusEntrance);
        updateIsland(cs);
    }

    private void updateIsland(CurrentStatus cs) {
        int islandIndex = cs.getGame().getArchipelagos().get(0).getIslands().get(0).getIslandIndex();
        int[] islandStudents = cs.getGame().getArchipelagos().get(0).getIslands().get(0).getStudents();
        GridPane studentPane = (GridPane) stage.getScene().lookup("#students"+islandIndex);
        Platform.runLater(() ->{
            int childrenIndex = 0;
            for(int i=0;i<islandStudents.length;i++) {
                String col = Colour.values()[i].toString();
                Image sImage = new Image(getClass().getClassLoader().getResource("images/wooden_pieces/student_"+col.toLowerCase()+".png").toString(),33,33,true,false);
                ImageView s = (ImageView) studentPane.getChildren().get(childrenIndex++);
                s.setImage(sImage);
                Label l = (Label) studentPane.getChildren().get(childrenIndex++);
                l.setText(""+islandStudents[i]);
            }
        });
    }

    private void manageCSPlanning(String line)  {
        CurrentStatus cs = parser.fromJson(line, CurrentStatus.class);
        if(cs.getStatus()!=0) {
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText(cs.getErrorMessage());
                a.initOwner(stage);
                a.show();
            });
            return;
        }
        StatusUpdater.instance().updateStatus(cs);
        currentStatus = StatusUpdater.instance().getCurrentStatus();
        if(currentStatus.getTurn().getPhase().equals("PLANNING")) {
            ImageView LAC;
            ScrollPane ACPane = (ScrollPane) stage.getScene().lookup("#ACPane");
            if(currentStatus.getTurn().getPlayer().equals(currentStatus.getPlayerID())) {
                Platform.runLater(() -> {
                    ACPane.setOpacity(1);
                    //for (Node x : ACPane.getChildrenUnmodifiable()) x.setOpacity(1);
                    ACPane.setDisable(false);
                });
            } else {
                Platform.runLater(() -> {
                    ACPane.setOpacity(0);
                    ACPane.setDisable(true);
                });
            }
            int playerIndex = cs.getGame().getPlayers().get(0).getIndex();
            if(currentStatus.getPlayerID().equals(playerIndex)) LAC = (ImageView) stage.getScene().lookup("#myLAC");
            else {
                if(currentStatus.getPlayerID()>0) {
                    if(playerIndex==0) LAC = (ImageView) stage.getScene().lookup("#opponentLAC");
                    else LAC = (ImageView) stage.getScene().lookup("#opponent2LAC");
                } else {
                    if(playerIndex==1) LAC = (ImageView) stage.getScene().lookup("#opponentLAC");
                    else LAC = (ImageView) stage.getScene().lookup("#opponent2LAC");
                }
            }
            int LACIndex = cs.getGame().getPlayers().get(0).getLastAssistantCardPlayed();
            String LACPath ="images/assistantCards/A";
            if(LACIndex<9) LACPath += "0";
            LACPath+=(LACIndex+1)+".png";
            String finalLACPath = LACPath;
            Platform.runLater(() -> LAC.setImage(new Image(getClass().getClassLoader().getResource(finalLACPath).toString(),150,200,true,true)));
        } else {
            changePhaseScene();
        }
        checkWinner();
    }
    private boolean checkForErrors(String line) {
        CurrentStatus cs = parser.fromJson(line, CurrentStatus.class);
        if(cs.getStatus()!=0) {
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText(cs.getErrorMessage());
                a.initOwner(stage);
                a.show();
            });
            return true;
        }
        StatusUpdater.instance().updateStatus(cs);
        currentStatus = StatusUpdater.instance().getCurrentStatus();
        return false;
    }
    private void changePhaseScene() {
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
    }
    private boolean checkWinner() {
        if(currentStatus.getWinner()!=null) {
            if(currentStatus.getWinner().equals("")) {
                Platform.runLater(() -> {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText(GamePhases.DRAW.getGUIPrompt());
                    a.initOwner(stage);
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
                    a.initOwner(stage);
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
        if(checkForErrors(line)) return;
        changePhaseScene();
    }

    public void toNextScene(GUIScene sceneType) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/"+sceneType.getFileName()));
            if(stage.getScene()!=null) stage.getScene().setRoot(root);
            else {
                Scene scene = new Scene(root);
                stage.setScene(scene);
            }
            if(sceneType==GUIScene.TITLE_SCREEN) currentScene = GUIScene.USERNAME;
            else {
                currentScene = sceneType;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
