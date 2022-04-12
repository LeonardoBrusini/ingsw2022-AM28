package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentToIslandEffectTest {
    private CharacterCard c = new CharacterCard(CharacterCardsInfo.CARD1);

    @BeforeEach
    /**
     * It inizialise with the need object the test environment
     */
    void inizialise(){
        IslandManager im = new IslandManager(2);
        c.setStudentsOnCard(new StudentGroup(10));
        c.setSelectedIsland(im.getIsland(3));
        c.setBoard(new Board(2));
        c.setSelectedColour(Colour.YELLOW);
        c.setPlayerThisTurn(new Player("g1", Tower.WHITE));
    }
    @Test
    void resolveEffect() {
        int before = c.getPlayerThisTurn().getCoins();
        c.getCardInfo().getEffect().resolveEffect(c);
        assertEquals(1, c.getSelectedIsland().getStudents().getQuantityColour(Colour.YELLOW));
        for(Colour colour: Colour.values()){
            assertFalse(c.getStudentsOnCard().getQuantityColour(colour)<9);
            assertFalse(c.getStudentsOnCard().getQuantityColour(colour)>11);
        }
    }
}