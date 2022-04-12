package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.IslandManager;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class ComputeInfluenceEffectTest {
    private CharacterCard c = new CharacterCard(CharacterCardsInfo.CARD3);

    /**
     * It inizialise with the need object the test environment
     */
    @BeforeEach
    void inizialise(){
        ExpertGameManager gm = new ExpertGameManager();
        c.setGameManager(gm);
        gm.newGame();
        c.setSelectedIsland(gm.getBoard().getIslandManager().getIsland(7));
        c.setBoard(gm.getBoard());
        gm.getBoard().getIslandManager().getIsland(gm.getBoard().getMotherNature().getIslandIndex()).setStudents(new StudentGroup(2));
        c.getGameManager().addPlayer("g1");
        c.getGameManager().addPlayer("g2");
        c.setPlayerThisTurn(c.getGameManager().getPlayers().get(0));
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        for(Colour color: Colour.values()) //this for is to be sure that the tests didn't depends on the bag variabily on extraction of students
            en.put(color,c.getGameManager().getPlayers().get(0).whatTower());
        ProfessorGroup pg = new ProfessorGroup();
        pg.setProfessors(en);
        c.getBoard().setProfessorGroup(pg);
    }

    /**
     * It verifies the correct execution of the card's effect and that it leaves the environment as expected
     */
    @Test
    void resolveEffect() {
        int islandbefore = c.getBoard().getMotherNature().getIslandIndex();
        assertEquals(7, c.getSelectedIsland().getIslandIndex());
        c.getCardInfo().getEffect().resolveEffect(c);
        assertEquals(7,c.getPlayerThisTurn().getDashboard().getNumTower());
        assertEquals(c.getGameManager().getPlayers().get(0).whatTower(),c.getSelectedIsland().getTower());
        assertEquals(8,c.getGameManager().getPlayers().get(1).getDashboard().getNumTower());
        assertFalse(c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).isPresenceMotherNature());
        assertEquals(islandbefore,c.getBoard().getMotherNature().getIslandIndex());
    }
}