package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.enumerations.Tower;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpertGameManagerTest {
    private ExpertGameManager gm = new ExpertGameManager();

    /**
     * The tests verifies the correct inizialization of players
     */
    @Test
    void addPlayer(){
        gm.addPlayer("g1");
        assertEquals("g1", gm.getPlayers().get(0).getNickname());
        assertEquals(Tower.WHITE, gm.getPlayers().get(0).getTower());
        assertEquals(1, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(0).getCoins());
        gm.addPlayer("g2");
        assertEquals("g2", gm.getPlayers().get(1).getNickname());
        assertEquals("g1", gm.getPlayers().get(0).getNickname());
        assertEquals(Tower.BLACK, gm.getPlayers().get(1).getTower());
        assertEquals(2, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(1).getCoins());
        gm.addPlayer("g3");
        assertEquals("g3", gm.getPlayers().get(2).getNickname());
        assertEquals("g2", gm.getPlayers().get(1).getNickname());
        assertEquals("g1", gm.getPlayers().get(0).getNickname());
        assertEquals(Tower.GRAY, gm.getPlayers().get(2).getTower());
        assertEquals(3, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(2).getCoins());
    }
}