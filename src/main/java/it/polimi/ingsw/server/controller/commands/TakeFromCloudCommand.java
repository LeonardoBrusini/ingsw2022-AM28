package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.EndOfGameChecker;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;

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
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
       try{
           gameManager.takeStudentsFromCloud(command.getIndex(), command.getPlayerIndex());
       }catch (WrongPhaseException e){
           return StatusCode.WRONGPHASE;
       }catch(IllegalArgumentException f){
           return StatusCode.ILLEGALARGUMENT;
       }catch (WrongTurnException z){
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
        if(EndOfGameChecker.instance().isEndOfGame()) {
            //not implemented yet
            return null;
        } else {
            //CloudStatus[] clouds = new CloudStatus[gameManager.getBoard().getClouds().size()];
            GameStatus gs = new GameStatus();
            CurrentStatus cs = new CurrentStatus();
            cs.setTurn(gameManager.getTurnManager().getTurnStatus());
            /*for(int i = 0; i < clouds.length; i++) {
                clouds[i].setIndex(i);
                for (Colour c : Colour.values())
                    clouds[i].setStudents(c.ordinal(), gameManager.getBoard().getClouds().get(command.getIndex()).getStudentsOnCloud().getQuantityColour(c));
            }
            */
            gs.setClouds(gameManager.getBoard().getCloudsStatus());
            cs.setGame(gs);
            return g.toJson(cs, CurrentStatus.class);
        }
    }
}
