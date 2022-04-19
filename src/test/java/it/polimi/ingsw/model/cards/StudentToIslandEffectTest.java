package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.CharacterCardInfo;
import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentToIslandEffectTest {
    private final CharacterCard c = new CharacterCard(CharacterCardInfo.CARD1);

    @BeforeEach
    /**
     * It initialises with the need object the test environment
     */
    void initialise(){
        IslandManager im = new IslandManager(2);
        c.setStudentsOnCard(new StudentGroup(10));
        c.setSelectedIsland(im.getIslandByIndex(3));
        c.setBoard(new Board(2));
        c.setSelectedColour(Colour.YELLOW);
        c.setPlayerThisTurn(new Player("g1", Tower.WHITE));
    }
    @Test
    /**
     * It verifies the correct execution of the card's effect
     */
    void resolveEffect() {
        c.getCardInfo().getEffect().resolveEffect(c);
        assertEquals(1, c.getSelectedIsland().getStudents().getQuantityColour(Colour.YELLOW));
        for(Colour colour: Colour.values()){
            assertFalse(c.getStudentsOnCard().getQuantityColour(colour)<9);
            assertFalse(c.getStudentsOnCard().getQuantityColour(colour)>11);
        }
    }
}