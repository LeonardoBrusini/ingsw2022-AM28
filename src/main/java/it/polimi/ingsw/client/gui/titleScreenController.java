package it.polimi.ingsw.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class titleScreenController {
    @FXML
    TextField serverIP;
    @FXML
    Label connectionErrorLabel;
    public void connectToServer(ActionEvent actionEvent) {
        String ip = serverIP.getCharacters().toString();
        if(isValidIPAddress(ip)) {
            try (
                    Socket echoSocket = new Socket(ip, Integer.parseInt("1234"));
                    PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()))
            ) {
                new Thread(() -> {
                    try {
                        String line;
                        while ((line = in.readLine())!=null) {
                            if(line.equals("ping")) out.println("pong");
                            else {
                               //GUIUpdater.instance().manageReceivedLine(line);
                            }
                        }
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/usernameScene.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                connectionErrorLabel.setText("Don't know about host " + ip);
                connectionErrorLabel.setOpacity(1);
            }
        } else {
            connectionErrorLabel.setText("INVALID IP");
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
