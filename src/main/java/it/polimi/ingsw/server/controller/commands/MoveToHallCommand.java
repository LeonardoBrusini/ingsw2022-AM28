package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;
import it.polimi.ingsw.server.model.StudentGroup;
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
     * @param command the command reference
     * @return Json message
     */
    @Override

    public String getUpdatedStatus(ExpertGameManager gameManager, Command command){
        Gson g = new Gson();
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        PlayerStatus[] ps = new PlayerStatus[1];

        ps[0].setIndex(command.getPlayerIndex());
        for(Colour c: Colour.values()){
            ps[1].setStudentsOnHallOfAColour(c.ordinal(), gameManager.getPlayers().get(command.getPlayerIndex()).getDashboard().getHall().getQuantityColour(c));
        }
        gs.setPlayers(ps);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }

    /* public String getUpdatedStatus(ExpertGameManager gameManager) {
        Gson g = new Gson();
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        PlayerStatus[] ps = new PlayerStatus[gameManager.getNumPlayers()];

        int i = 0;
        for(Player p: gameManager.getPlayers()){
            PlayerStatus player = new PlayerStatus();
            int j = 0;
            StudentGroup hall =p.getDashboard().getHall();
            for(Colour c: Colour.values()) {
                player.setStudentsOnHallOfAColour(j, hall.getQuantityColour(c));
                j++;
            }
            player.setIndex(i);
            ps[i] = player;
            i++;
        }
        gs.setPlayers(ps);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }*/
}

