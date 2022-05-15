package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;
import java.util.ArrayList;

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
            System.out.println("trying to resolve MOVETOHALL COMMAND");
            gameManager.moveStudentsToHall(command.getPlayerIndex(), Colour.valueOf(command.getStudentColour()));
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
     * @param command the command reference
     * @return Json message
     */
    @Override
    public String getUpdatedStatus(ExpertGameManager gameManager, Command command){
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(command.getPlayerIndex());
        ps0.setStudentsOnHall(gameManager.getPlayers().get(command.getPlayerIndex()).getDashboard().getHall().getStatus());
        ps0.setStudentsOnEntrance(gameManager.getPlayers().get(command.getPlayerIndex()).getDashboard().getEntrance().getStatus());
        ps.add(ps0);
        gs.setPlayers(ps);
        gs.setProfessors(gameManager.getBoard().getProfessorGroup().getStatus());
        cs.setGame(gs);
        return new Gson().toJson(cs, CurrentStatus.class);
    }
}

