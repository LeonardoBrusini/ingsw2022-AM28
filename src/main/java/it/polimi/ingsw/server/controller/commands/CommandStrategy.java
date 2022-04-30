package it.polimi.ingsw.server.controller.commands;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;

public interface CommandStrategy {
    StatusCode resolveCommand(ExpertGameManager gameManager, Command command);
    String getUpdatedStatus(ExpertGameManager gameManager);
}
