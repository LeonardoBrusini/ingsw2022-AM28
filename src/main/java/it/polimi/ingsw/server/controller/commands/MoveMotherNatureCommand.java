package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;

/**
 * The class that resolves the command to move MotherNature
 */
public class MoveMotherNatureCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
        try {
            gameManager.moveMotherNature(command.getMotherNatureShifts());
        }catch (WrongPhaseException e){
            return StatusCode.WRONGPHASE;
        }
        return null;
    }

    /**
     * It creates the message with changes operated by the resolution of the command
     * @param gameManager gameManager reference
     * @return Json message
     */
    @Override
    public String getUpdatedStatus(ExpertGameManager gameManager) {
        Gson g = new Gson();
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        gs.setMotherNatureIndex(gameManager.getBoard().getMotherNature().getIslandIndex());
        return g.toJson(gs, CurrentStatus.class);
    }
}
