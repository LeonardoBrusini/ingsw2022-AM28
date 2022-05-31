package it.polimi.ingsw.client.gui.scenecontrollers;

import com.sun.source.tree.LambdaExpressionTree;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.handlers.CommandHandler;
import it.polimi.ingsw.client.gui.handlers.CommandSingleton;
import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.PlayerStatus;
import it.polimi.ingsw.server.enumerations.Colour;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ControllerUtils {
    public static void addAssistantCards(HBox ACBox, ScrollPane ACPane, ClassLoader classLoader) {
        PlayerStatus ps = null;
        for (PlayerStatus p: StatusUpdater.instance().getCurrentStatus().getGame().getPlayers()) {
            if(StatusUpdater.instance().getCurrentStatus().getPlayerID().equals(p.getIndex())) {
                ps = p;
                break;
            }
        }
        String path;
        if(StatusUpdater.instance().getCurrentStatus().getTurn().getPlayer().equals(StatusUpdater.instance().getCurrentStatus().getPlayerID())) {
            for(int i=0;i<ps.getAssistantCards().length;i++) {
                if(!ps.getAssistantCards()[i]) {
                    path = "images/assistantCards/A";
                    if(i<9) path+="0";
                    path+=(i+1)+".png";
                    Image image = new Image(classLoader.getResource(path).toString(),250,243,true,true);
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


}
