package it.polimi.ingsw.client.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.NetworkManager;
import it.polimi.ingsw.network.Command;
import javafx.event.Event;
import javafx.event.EventHandler;

public abstract class CommandHandler implements EventHandler<Event> {
    private String cmd;
    private int playerIndex;
    private int index;
    private Gson parser;

    public CommandHandler(String cmd, int playerIndex, int index) {
        this.cmd = cmd;
        this.playerIndex = playerIndex;
        this.index = index;
    }

    public void sendAssistantCardCommand() {
        Command c = new Command();
        c.setCmd(cmd);
        c.setPlayerIndex(playerIndex);
        c.setIndex(index);
        NetworkManager.instance().sendJSON(c);
    }
}
