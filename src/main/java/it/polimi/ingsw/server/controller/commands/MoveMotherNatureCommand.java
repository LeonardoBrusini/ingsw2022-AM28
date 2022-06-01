package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.EndOfGameChecker;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.controller.Phase;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;

/**
 * The class that resolves the command to move MotherNature
 */
public class MoveMotherNatureCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(GameManager gameManager, Command command) {
        if(command.getPlayerIndex()!=gameManager.getTurnManager().getCurrentPlayer()) return StatusCode.WRONGTURN;
        try {
            gameManager.moveMotherNature(command.getMotherNatureShifts());
        }catch (WrongPhaseException e){
            return StatusCode.WRONGPHASE;
        }catch(IllegalArgumentException f){
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
    public String getUpdatedStatus(GameManager gameManager, Command command) {
        Gson g = new Gson();
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        cs.setLastCommand("MOVEMOTHERNATURE");
        gs.setMotherNatureIndex(gameManager.getBoard().getMotherNature().getIslandIndex());
        gs.setArchipelagos(gameManager.getBoard().getIslandManager().getFullArchipelagosStatus());
        ArrayList<Player> players = gameManager.getPlayers();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        for(int i=0;i<players.size();i++) {
            PlayerStatus psi = new PlayerStatus();
            psi.setIndex(i);
            psi.setNumTowers(players.get(i).getDashboard().getNumTowers());
            ps.add(psi);
        }
        gs.setPlayers(ps);
        cs.setGame(gs);
        if(EndOfGameChecker.instance().isEndOfGame()) {
            int winner = EndOfGameChecker.instance().getWinner();
            if(winner==-1) cs.setWinner("");
            else cs.setWinner(gameManager.getPlayers().get(EndOfGameChecker.instance().getWinner()).getNickname());
        }
        return g.toJson(cs, CurrentStatus.class);
    }
}
