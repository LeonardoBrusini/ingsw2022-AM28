package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.client.gui.CommandHandler;
import it.polimi.ingsw.network.PlayerStatus;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Planning2SimpleController extends GenericSceneController {
    @FXML
    HBox ACBox;
    @FXML
    ScrollPane ACPane;

    @FXML
    public void initialize() {
        super.initialize();
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
}
