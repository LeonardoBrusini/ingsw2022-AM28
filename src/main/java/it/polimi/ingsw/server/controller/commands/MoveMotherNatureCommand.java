package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.controller.Phase;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.model.board.IslandManager;
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
    public StatusCode resolveCommand(ExpertGameManager gameManager, Command command) {
        if(command.getPlayerIndex()!=gameManager.getTurnManager().getCurrentPlayer()) return StatusCode.WRONGTURN;
        if(gameManager.getTurnManager().getPhase()!= Phase.ACTION) return StatusCode.WRONGPHASE;
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
    public String getUpdatedStatus(ExpertGameManager gameManager, Command command) {
        Gson g = new Gson();
        GameStatus gs = new GameStatus();
        CurrentStatus cs = new CurrentStatus();
        gs.setMotherNatureIndex(gameManager.getBoard().getMotherNature().getIslandIndex());
        ArrayList<Archipelago> archipelagos = gameManager.getBoard().getIslandManager().getArchipelagos();
        ArchipelagoStatus[] as = new ArchipelagoStatus[archipelagos.size()];
        for(int i=0;i<archipelagos.size();i++) {
            as[i] = new ArchipelagoStatus();
            as[i].setIndex(i);
            ArrayList<Island> islands = archipelagos.get(i).getIslands();
            IslandStatus[] is = new IslandStatus[archipelagos.get(i).getIslands().size()];
            for(int j=0;j<archipelagos.get(i).getIslands().size();j++) {
                is[j] = new IslandStatus();
                is[j].setIslandIndex(islands.get(j).getIslandIndex());
                is[j].setStudents(islands.get(j).getStudents().getStatus());
                if(islands.get(j).getTower()!=null) is[j].setTowerColour(islands.get(j).getTower().toString());
            }
            as[i].setIslands(is);
        }
        gs.setArchipelagos(as);
        ArrayList<Player> players = gameManager.getPlayers();
        PlayerStatus[] ps = new PlayerStatus[gameManager.getPlayers().size()];
        for(int i=0;i<players.size();i++) {
            ps[i] = new PlayerStatus();
            ps[i].setIndex(i);
            ps[i].setNumTowers(players.get(i).getDashboard().getNumTowers());
        }
        cs.setGame(gs);
        return g.toJson(cs, CurrentStatus.class);
    }
}
