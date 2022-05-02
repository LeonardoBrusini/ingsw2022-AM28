package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.enumerations.Tower;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpertGameManagerTest {
    private final ExpertGameManager gm = new ExpertGameManager();

    /**
     * The tests verifies the correct inizialization of players
     */
    @Test
    void addPlayer(){
        gm.addPlayer();
        assertEquals(Tower.WHITE, gm.getPlayers().get(0).getTower());
        assertEquals(1, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(0).getCoins());
        assertEquals(Tower.BLACK, gm.getPlayers().get(1).getTower());
        assertEquals(2, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(1).getCoins());
        gm.addPlayer();
        assertEquals(Tower.GRAY, gm.getPlayers().get(2).getTower());
        assertEquals(3, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(2).getCoins());
    }
}