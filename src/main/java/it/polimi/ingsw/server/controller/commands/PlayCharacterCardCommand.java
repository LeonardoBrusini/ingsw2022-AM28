package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.StatusCode;
import it.polimi.ingsw.server.controller.EndOfGameChecker;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.cards.CharacterCard;

/**
 * The class that resolves the command to play the chosen CharacterCard
 */
public class PlayCharacterCardCommand implements CommandStrategy{
    /**
     * It resolves the command given by the client
     * @param gameManager gameManager reference
     * @param command command given by the client
     * @return null if no Exception thrown, corresponding StatusCode otherwise
     */
    @Override
    public StatusCode resolveCommand(GameManager gameManager, Command command) {
        try {
            int pIndex = command.getPlayerIndex();
            int cIndex = command.getIndex();
            if(command.getPStudentsFrom()!=null) {
                StudentGroup sf = new StudentGroup(command.getPStudentsFrom());
                StudentGroup st = new StudentGroup(command.getPStudentsTo());
                gameManager.playCharacterCard(pIndex,cIndex,sf,st);
            } else if(command.getPIndex()!=null && command.getPColour()!=null) {
                gameManager.playCharacterCard(pIndex,cIndex, Colour.valueOf(command.getPColour()),command.getPIndex());
            } else if(command.getPIndex()!=null) {
                gameManager.playCharacterCard(pIndex,cIndex,command.getPIndex());
            } else if(command.getPColour()!=null) {
                gameManager.playCharacterCard(pIndex,cIndex,Colour.valueOf(command.getPColour()));
            } else {
                gameManager.playCharacterCard(pIndex,cIndex);
            }
        } catch (IllegalArgumentException e) {
            return StatusCode.ILLEGALARGUMENT;
        }catch (WrongPhaseException f){
            return StatusCode.WRONGPHASE;
        }catch (NotEnoughCoinsException r){
            return StatusCode.NOTENOUGHCOINS;
        }catch(AlreadyPlayedException h){
            return StatusCode.ALREADYPLAYEDCC;
        } catch (WrongTurnException i) {
            return StatusCode.WRONGTURN;
        }
        //other ERRORS
        return null;
    }

    /**
     * It creates the message with changes operated by the resolution of the command
     * @param gameManager gameManager reference
     * @return Json message
     */
    @Override
    public String getUpdatedStatus(GameManager gameManager, Command command) {
        CharacterCard card = gameManager.getBoard().getCharacterCards().get(command.getIndex());
        CurrentStatus cs = card.getCardInfo().getEffect().getUpdatedStatus(card,gameManager);
        cs.setLastCommand("PLAYCHARACTERCARD");
        if(EndOfGameChecker.instance().isEndOfGame()){
            int winner = EndOfGameChecker.instance().getWinner();
            if(winner==-1) cs.setWinner("");
            else cs.setWinner(gameManager.getPlayers().get(EndOfGameChecker.instance().getWinner()).getNickname());
        }
        return new Gson().toJson(cs);
    }
}

