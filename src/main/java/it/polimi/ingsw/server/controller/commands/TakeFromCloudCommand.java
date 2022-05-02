package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;
import it.polimi.ingsw.server.model.board.Cloud;

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
     * @return Json message
     */
    @Override
    public String getUpdatedStatus(ExpertGameManager gameManager) {
        Gson g = new Gson();
        CloudStatus[] clouds = new CloudStatus[gameManager.getBoard().getClouds().size()];
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        int i = 0;
        for(Cloud c: gameManager.getBoard().getClouds()){
            clouds[i].setIndex(i);
            for(Colour colour: Colour.values()){
                clouds[i].setStudents(colour.ordinal(), c.getStudentsOnCloud().getQuantityColour(colour));
            }
        }
        gs.setClouds(clouds);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }
}
