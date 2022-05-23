package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.cli.CLIMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CLIClient {
    public static void start(){
        CLIMenu menu = new CLIMenu();
        String hostName = "127.0.0.1";
        int portNumber = 1234;
        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine())!=null) {
                        if(line.equals("ping")) out.println("pong");
                        else {
                            menu.manageReceivedLine(line);
                        }
                    }
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            String userInput;
            menu.printMenu();
            while ((userInput = stdIn.readLine())!=null) {
                System.out.println("input letto");
                String output = menu.manageInputLine(userInput);
                if(output != null) {
                    System.out.println("SENDING...");
                    out.println(output);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    public static void main( String[] args ){
        CLIClient.start();
    }
}
