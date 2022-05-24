package it.polimi.ingsw.client.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.network.AddPlayerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PreGameController {
    int portNumber = 1234;
    PrintWriter out;
    BufferedReader in;
    @FXML
    TextField serverIP;
    @FXML
    Button play;
    @FXML
    Label connectionErrorLabel;
    public void connectToServer(ActionEvent actionEvent) {
        try {
            Socket echoSocket = new Socket(serverIP.getText(), portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            changeScene(actionEvent, "fxml/usernameScene.fxml");
        } catch (IOException e) {
            connectionErrorLabel.setOpacity(1);
        }
    }

    @FXML
    TextField usernameField;
    @FXML
    Label usernameErrorLabel;
    @FXML
    Button usernameButton;
    public void sendUsername(ActionEvent actionEvent) throws IOException {
        Gson gson = new Gson();
        if(usernameField.getText() != null)
            out.println(usernameField.getText());
        else
            usernameErrorLabel.setOpacity(1);
       while((in.readLine()) != null){
           AddPlayerResponse response = gson.fromJson((in.readLine()), AddPlayerResponse.class);
           int status = response.getStatus();
           boolean first = response.isFirst();
           if(status != 0) {
               usernameErrorLabel.setText(response.getErrorMessage());
           }else if(!first) {
               usernameErrorLabel.setText("Waiting for other players");
           }else{
               changeScene(actionEvent, "fxml/getParametersScene.fxml");
           }
           usernameErrorLabel.setOpacity(1);
       }

    }

    //Maybe to do a strategy pattern to handle this class
    private void changeScene(ActionEvent actionEvent,String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxml));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

