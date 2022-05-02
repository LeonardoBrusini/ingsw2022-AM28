package it.polimi.ingsw.server.controller.commands;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;

/**
 * The class that resolves the command to play the chosen CharacterCard
 */
public class PlayCharacterCardCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
        //not implemented yet
        return null;
    }

    /**
     * It creates the message with changes operated by the resolution of the command
     * @param gameManager gameManager reference
     * @return Json message
     */
    @Override
    public String getUpdatedStatus(ExpertGameManager gameManager) {
        //not implemented yet
        return null;
    }
}

