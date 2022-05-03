package it.polimi.ingsw.server.network;

public class CheckDisconnection implements Runnable{
    private final ConnectionList sender;
    public CheckDisconnection(ConnectionList sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            while (true) {
                sender.setAllDisconnected();
                sender.sendToAll("ping");
                wait(5000);
                sender.eraseDisconnected();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
