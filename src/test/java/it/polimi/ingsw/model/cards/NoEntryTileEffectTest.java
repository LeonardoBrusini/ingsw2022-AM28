package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.Archipelago;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoEntryTileEffectTest {
    @Test
    void resolveEffect() {
        CharacterCard c = new CharacterCard(CharacterCardsInfo.CARD5);
        c.setBoard(new Board(2));
        c.setSelectedIsland(c.getBoard().getIslandManager().getIsland(3));
        Archipelago a = c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex());

        c.setNoEntryTiles(1);
        a.setNoEntryTiles(3);
        int anet = a.getNoEntryTiles();
        int cnet = c.getNoEntryTiles();
        c.getCardInfo().getEffect().resolveEffect(c);
        //removed if cycle to test all cases indivually

        assertEquals(anet + 1, a.getNoEntryTiles());
        assertEquals(cnet - 1, c.getNoEntryTiles());

        c.setNoEntryTiles(2);
        a.setNoEntryTiles(3);
        int anet1 = a.getNoEntryTiles();
        int cnet1 = c.getNoEntryTiles();
        c.getCardInfo().getEffect().resolveEffect(c);
        //removed if cycle to test all cases individually

        assertEquals(anet1 + 1, a.getNoEntryTiles());
        assertEquals(cnet1 - 1, c.getNoEntryTiles());

        c.setNoEntryTiles(0);
        a.setNoEntryTiles(3);
        int anet2 = a.getNoEntryTiles();
        int cnet2 = c.getNoEntryTiles();
        c.getCardInfo().getEffect().resolveEffect(c);
        //removed if cycle to test all cases indivually

        assertEquals(anet2 , a.getNoEntryTiles());
        assertEquals(cnet2 , c.getNoEntryTiles());

    }
}