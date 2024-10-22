package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * It verifies the correct number of NoEntryTiles both in the card and on the island
 */
class NoEntryTileEffectTest {
    @Test
    void resolveEffect() {
        CharacterCard c = new CharacterCard(CharacterCardInfo.CARD5);
        c.setBoard(new Board(2));
        c.setSelectedIsland(c.getBoard().getIslandManager().getIslandByIndex(3));
        Archipelago a = c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex());

        c.setNoEntryTiles(1);
        a.setNoEntryTiles(3);
        int anet = a.getNoEntryTiles();
        int cNet = c.getNoEntryTiles();
        c.getCardInfo().getEffect().resolveEffect(c);
        //removed if cycle to test all cases individually

        assertEquals(anet + 1, a.getNoEntryTiles());
        assertEquals(cNet - 1, c.getNoEntryTiles());

        c.setNoEntryTiles(2);
        a.setNoEntryTiles(3);
        int anet1 = a.getNoEntryTiles();
        int cNet1 = c.getNoEntryTiles();
        c.getCardInfo().getEffect().resolveEffect(c);
        //removed if cycle to test all cases individually

        assertEquals(anet1 + 1, a.getNoEntryTiles());
        assertEquals(cNet1 - 1, c.getNoEntryTiles());

        c.setNoEntryTiles(0);
        a.setNoEntryTiles(3);
        int anet2 = a.getNoEntryTiles();
        int cNet2 = c.getNoEntryTiles();
        try {
            c.getCardInfo().getEffect().resolveEffect(c);
        } catch (IllegalArgumentException e) {
            assertEquals(anet2 , a.getNoEntryTiles());
            assertEquals(cNet2 , c.getNoEntryTiles());
        }

    }
}