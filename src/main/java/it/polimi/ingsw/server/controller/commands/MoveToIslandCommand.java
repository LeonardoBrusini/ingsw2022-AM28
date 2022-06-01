package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;

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
    public StatusCode resolveCommand(GameManager gameManager, Command command) {
        try{
            gameManager.moveStudentToIsland(command.getPlayerIndex(), Colour.valueOf(command.getStudentColour()), command.getIndex());
        }catch(WrongTurnException e){
            return StatusCode.WRONGTURN;
        }catch(WrongPhaseException z){
            return StatusCode.WRONGPHASE;
        }catch(IllegalArgumentException h){
            return StatusCode.ILLEGALARGUMENT;
        } catch (NoStudentsException e) {
            return StatusCode.NOSTUDENTS;
        }
        return null;
    }

    /**
     *  It creates the message with changes operated by the resolution of the command
     *  @param gameManager gameManager reference
     *  @param command the command reference
     *  @return Json message
     */
    @Override
    public String getUpdatedStatus(GameManager gameManager, Command command){
        System.out.println("generazione risposta");
        Gson g = new Gson();
        CurrentStatus cs = new CurrentStatus();
        cs.setLastCommand("MOVETOISLAND");
        GameStatus gs = new GameStatus();
        ArrayList<ArchipelagoStatus> ac = new ArrayList<>();
        ArrayList<IslandStatus> is = new ArrayList<>();
        ArchipelagoStatus ac0 = new ArchipelagoStatus();
        ac0.setIndex(gameManager.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(command.getIndex()));
        IslandStatus is0 = new IslandStatus();
        is0.setIslandIndex(command.getIndex());
        is0.setStudents(gameManager.getBoard().getIslandManager().getIslandByIndex(command.getIndex()).getStudents().getStatus());
        is.add(is0);
        ac0.setIslands(is);
        ac.add(ac0);
        gs.setArchipelagos(ac);
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(command.getPlayerIndex());
        ps0.setStudentsOnEntrance(gameManager.getPlayers().get(command.getPlayerIndex()).getDashboard().getEntrance().getStatus());
        ps.add(ps0);
        gs.setPlayers(ps);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }
}
