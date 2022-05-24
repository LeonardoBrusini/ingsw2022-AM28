package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.network.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class titleScreenController {
    @FXML
    TextField serverIP;
    @FXML
    Label connectionErrorLabel;
    public void connectToServer(ActionEvent actionEvent) {
        String ip = serverIP.getCharacters().toString();
        if(!isValidIPAddress(ip)) {
            connectionErrorLabel.setText("INVALID IP");
            connectionErrorLabel.setOpacity(1);
            return;
        }

        if(NetworkManager.instance().startServer(ip,1234)) {
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/usernameScene.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            connectionErrorLabel.setText("ERROR CONNECTING TO SERVER");
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
}
