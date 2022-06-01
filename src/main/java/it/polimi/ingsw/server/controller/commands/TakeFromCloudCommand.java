package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.EndOfGameChecker;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.exceptions.NoStudentsException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;

import java.util.ArrayList;

/**
 * The class that resolves the command to take students from a cloud
 */
public class TakeFromCloudCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(GameManager gameManager, Command command) {
       try{
           gameManager.takeStudentsFromCloud(command.getIndex(), command.getPlayerIndex());
       }catch (WrongPhaseException e){
           return StatusCode.WRONGPHASE;
       }catch(IllegalArgumentException f){
           return StatusCode.ILLEGALARGUMENT;
       }catch (WrongTurnException z){
           return StatusCode.WRONGTURN;
       }catch (NoStudentsException h){
           return StatusCode.NOSTUDENTS;
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
    public String getUpdatedStatus(GameManager gameManager, Command command){
        Gson g = new Gson();
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        cs.setLastCommand("TAKEFROMCLOUD");
        cs.setTurn(gameManager.getTurnManager().getTurnStatus());
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(command.getPlayerIndex());
        ps0.setStudentsOnEntrance(gameManager.getPlayers().get(command.getPlayerIndex()).getDashboard().getEntrance().getStatus());
        ps.add(ps0);
        gs.setPlayers(ps);
        gs.setClouds(gameManager.getBoard().getCloudsStatus());
        cs.setGame(gs);
        if(EndOfGameChecker.instance().isEndOfGame()) {
            int winner = EndOfGameChecker.instance().getWinner();
            if(winner==-1) cs.setWinner("");
            else cs.setWinner(gameManager.getPlayers().get(EndOfGameChecker.instance().getWinner()).getNickname());
        }
        return g.toJson(cs, CurrentStatus.class);
    }
}
