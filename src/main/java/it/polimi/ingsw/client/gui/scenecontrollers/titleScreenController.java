package it.polimi.ingsw.client.gui.scenecontrollers;

import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class titleScreenController {
    @FXML
    TextField serverIP, serverPort;
    @FXML
    Label connectionErrorLabel;
    @FXML
    ImageView exitImageView;

    public void connectToServer(ActionEvent actionEvent) {
        String ip = serverIP.getCharacters().toString();
        if(!isValidIPAddress(ip)) {
            connectionErrorLabel.setText("INVALID IP, try again");
            connectionErrorLabel.setOpacity(1);
            return;
        }

        try {
            int port = Integer.parseInt(serverPort.getCharacters().toString());
            if(NetworkManager.instance().startServer(ip,port)) {
                try {
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/usernameScene.fxml"));
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.getScene().setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                connectionErrorLabel.setText("ERROR CONNECTING TO SERVER");
                connectionErrorLabel.setOpacity(1);
            }
        } catch (NumberFormatException e) {
            connectionErrorLabel.setText("INVALID PORT NUMBER, try again");
            connectionErrorLabel.setOpacity(1);
        }
    }

    public static boolean isValidIPAddress(String ip)
    {
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";
        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;
        Pattern p = Pattern.compile(regex);
        if (ip == null) {
            return false;
        }
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    public void closeGame() {
        System.out.println("exit");
        System.exit(0);
    }
}
