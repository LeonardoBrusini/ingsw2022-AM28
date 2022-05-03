package it.polimi.ingsw.server.controller.commands;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;

/**
 * The interface that command classes will implement
 */
public interface CommandStrategy {
    /**
     * The method that will call the model to change its status
     * @param gameManager ExpertGameManager references
     * @param command given Command references
     * @return Corresponding StatusCode in case of thrown Exceptions or errors
     */
    StatusCode resolveCommand(ExpertGameManager gameManager, Command command);

    /**
     * The method that generates the Json String with the changes of the model operated by the command
     * @param gameManager ExpertGameManager references
     * @return Json String
     */
    String getUpdatedStatus(ExpertGameManager gameManager, Command command);
}
