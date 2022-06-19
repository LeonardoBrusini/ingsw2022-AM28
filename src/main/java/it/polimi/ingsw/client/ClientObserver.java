package it.polimi.ingsw.client;

public interface ClientObserver {
    void manageMessage(String line);

    void manageDisconnection();
}
