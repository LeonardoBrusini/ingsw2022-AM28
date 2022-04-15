package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.model.players.AssistantCard;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AddMotherNatureShiftsEffectTest {
    /*
    /**
     * It tests the correct execution of the card's effect
     */
    /*@Test
    void resolveEffect() {
        ExpertGameManager gm = new ExpertGameManager();
        int moves;
        gm.newGame();
        gm.addPlayer("g1");
        gm.addPlayer("g1");
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD4);
        c.setPlayerThisTurn(gm.getPlayers().get(0));
        for(int i = 0; i < 10; i++){
            c.getPlayerThisTurn().playCard(i);
            moves = c.getPlayerThisTurn().getLastPlayedCard().getMotherNatureShifts();
            assertEquals(i,c.getPlayerThisTurn().getLastPlayedCard());
            c.getCardInfo().getEffect().resolveEffect(c);
            moves += 2;
            assertEquals(moves, c.getPlayerThisTurn().getLastPlayedCard().getMotherNatureShifts());
        }
    }*/

}