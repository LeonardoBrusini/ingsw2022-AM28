package it.polimi.ingsw.server.controller.commands;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.server.controller.ExpertGameManager;

public interface CommandStrategy {
    String resolveCommand(ExpertGameManager gameManager, Command command);
}
