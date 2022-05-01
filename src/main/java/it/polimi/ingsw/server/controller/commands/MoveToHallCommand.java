package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;
import it.polimi.ingsw.server.model.players.Player;

/**
 * The class that resolves the command to move students to the corresponding Hall
 */
public class MoveToHallCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
        try{
           gameManager.moveStudentsToHall(command.getPlayerIndex(), Colour.valueOf(command.getPColour()));
        }catch (FullHallException e){
            return StatusCode.FULLHALL;
        }catch (NoStudentsException f){
            return StatusCode.NOSTUDENTS;
        }catch(IllegalArgumentException z){
            return StatusCode.ILLEGALARGUMENT;
        }catch(WrongPhaseException h){
            return StatusCode.WRONGPHASE;
        }catch (WrongTurnException t){
            return StatusCode.WRONGTURN;
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
        String status = "{ \"players\" : \n [";
        int i = 0;
        for(Player p: gameManager.getPlayers()) {
           // status += "index":0, "assistantCardsLeft": [0,1,2,3,4,5,6,7,8,9], "coins":1, "studentsEntrance":[x,x,x,x,x], "studentsHall":[x,x,x,x,x], "numTowwer";
        }
        return null;
    }
}
