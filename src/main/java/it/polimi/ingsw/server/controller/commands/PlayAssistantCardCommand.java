package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.controller.Phase;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;

import java.util.ArrayList;

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
    public StatusCode resolveCommand(GameManager gameManager, Command command) {
        try{
            gameManager.playAssistantCard(command.getPlayerIndex(), command.getIndex());
            System.out.println("assistant card played");
        }catch(IllegalArgumentException e){
            return StatusCode.ILLEGALARGUMENT;
        }catch(WrongTurnException h){
            return StatusCode.WRONGTURN;
        }catch(WrongPhaseException z){
            return StatusCode.WRONGPHASE;
        }catch (AlreadyPlayedException k) {
            return StatusCode.ALREADYPLAYEDAC;
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
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        TurnStatus ts = new TurnStatus();
        ts.setPlayer(gameManager.getTurnManager().getCurrentPlayer());
        if(gameManager.getTurnManager().getPhase()==Phase.ACTION) ts.setPhase(Phase.ACTION.name());
        cs.setTurn(ts);
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        boolean[] asc = new boolean[10];
        for(int j = 0; j < 10; j++)
            asc[j] = gameManager.getPlayers().get(command.getPlayerIndex()).getAssistantCard(j).isPlayed();
        ps0.setAssistantCards(asc);
        ps0.setIndex(command.getPlayerIndex());
        ps0.setLastAssistantCardPlayed(gameManager.getPlayers().get(command.getPlayerIndex()).getLastPlayedCard().getInfo().ordinal());
        ps.add(ps0);
        gs.setPlayers(ps);
        cs.setGame(gs);
        System.out.println("status returning");
        return g.toJson(cs, CurrentStatus.class);
    }
}
