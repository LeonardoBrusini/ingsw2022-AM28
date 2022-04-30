package it.polimi.ingsw.server.controller.commands;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;

public class MoveToHallCommand implements CommandStrategy{
    @Override
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
        //not implemented yet
        return null;
    }

    @Override
    public String getUpdatedStatus(ExpertGameManager gameManager) {
        //not implemented yet
        return null;
    }
}
