package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;

/**
 * The class that resolves the command to play a specific AssistantCard
 */
public class PlayAssistantCardCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
        try{
            gameManager.playAssistantCard(command.getPlayerIndex(), command.getIndex());
        }catch(IllegalArgumentException e){
            return StatusCode.ILLEGALARGUMENT;
        }catch(WrongTurnException h){
            return StatusCode.WRONGTURN;
        }catch(WrongPhaseException z){
            return StatusCode.WRONGPHASE;
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
        PlayerStatus[] ps = new PlayerStatus[1];

        boolean[] asc = new boolean[10];
        for(int j = 0; j < 10; j++)
            asc[j] = gameManager.getPlayers().get(command.getPlayerIndex()).getAssistantCard(j).isPlayed();
        ps[0].setAssistantCard(asc);
        ps[0].setIndex(command.getPlayerIndex());
        ps[0].setLastAssistantCardPlayed(gameManager.getPlayers().get(command.getPlayerIndex()).getLastPlayedCard().getInfo().ordinal());
        gs.setPlayers(ps);
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }
}
