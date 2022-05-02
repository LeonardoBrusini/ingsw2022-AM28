package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;

/**
 * The class that resolves the command to move students to a specific Island
 */
public class MoveToIslandCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
        try{
            System.out.println("tentativo di esecuzione");
            //System.out.println(Colour.valueOf(command.getStudentColour()));
            gameManager.moveStudentToIsland(command.getPlayerIndex(), Colour.valueOf(command.getStudentColour()), command.getIndex());
            System.out.println("andato a buon fine");
        }catch(WrongTurnException e){
            return StatusCode.WRONGTURN;
        }catch(WrongPhaseException z){
            return StatusCode.WRONGPHASE;
        }catch(IllegalArgumentException h){
            return StatusCode.ILLEGALARGUMENT;
        }
        return null;
    }

    /**
     * It creates the message with changes operated by the resolution of the command
     * @param gameManager gameManager reference
     * @param command the command reference
     * @return Json message
     */
    @Override
    public String getUpdatedStatus(ExpertGameManager gameManager, Command command){
        System.out.println("generazione risposta");
        Gson g = new Gson();
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArchipelagoStatus[] ac = new ArchipelagoStatus[1];
        IslandStatus[] is = new IslandStatus[1];
        ac[0].setIndex(gameManager.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(command.getIndex()));
        is[0].setIslandIndex(command.getIndex());
        is[0].setStudents(gameManager.getBoard().getIslandManager().getIslandByIndex(command.getIndex()).getStudents().getStatus());
        ac[0].setIslands(is);
        gs.setArchipelagos(ac);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }
}
