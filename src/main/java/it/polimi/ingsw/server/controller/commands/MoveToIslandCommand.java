package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.Island;

import java.util.ArrayList;

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
            gameManager.moveStudentToIsland(command.getPlayerIndex(), Colour.valueOf(command.getPColour()), command.getIndex());
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
        Gson g = new Gson();
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArchipelagoStatus[] ac = new ArchipelagoStatus[1];
        IslandStatus[] is = new IslandStatus[1];

        is[1].setIslandIndex(command.getIndex());
        for(Colour c: Colour.values())
            is[1].setStudentsOfAColour(c.ordinal(), gameManager.getBoard().getIslandManager().getIslandByIndex(command.getIndex()).getStudents().getQuantityColour(c));
        ac[1].setIslands(is);
        gs.setArchipelagos(ac);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }
    /* public String getUpdatedStatus(ExpertGameManager gameManager) {
        Gson g = new Gson();
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArchipelagoStatus[] ac = new ArchipelagoStatus[gameManager.getBoard().getIslandManager().getNumArchipelagos()];
        int z = 0;
        for(Archipelago a: gameManager.getBoard().getIslandManager().getArchipelagos()) {
            ArrayList<Island> islands = a.getIslands();
            IslandStatus[] is = new IslandStatus[islands.size()];
            for (int i = 0; i < islands.size(); i++) {
                is[i].setIslandIndex(i);
                int j = 0;
                StudentGroup students = gameManager.getBoard().getIslandManager().getIslandByIndex(i).getStudents();
                for (Colour c : Colour.values()) {
                    is[i].setStudentsOfAColour(j, students.getQuantityColour(c));
                    j++;
                }
            }
            ac[z].setIslands(is);
            z++;
        }
        gs.setArchipelagos(ac);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }*/
}
