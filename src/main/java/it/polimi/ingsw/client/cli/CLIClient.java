package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.network.NetworkManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLIClient {
    public static void start(String ip, int port){
        CLIMenu menu = new CLIMenu();
        NetworkManager.instance().setObserver(menu);
        if(NetworkManager.instance().startServer(ip,port)) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            menu.printMenu();
            try {
                while ((userInput = stdIn.readLine())!=null) {
                    System.out.println("input letto");
                    String output = menu.manageInputLine(userInput);
                    if(output != null) {
                        System.out.println("SENDING...");
                        NetworkManager.instance().send(output);
                    }
                }
            } catch (IOException e) {
                System.out.println("I/O ERROR");
            }
        } else {
            System.out.println("ERROR TRYING TO CONNECT TO SERVER");
        }
    }

    public static void main( String[] args ){
        CLIClient.start("127.0.0.1",1234);
    }
}
