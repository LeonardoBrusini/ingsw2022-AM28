package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.ExpertGameManager;
import it.polimi.ingsw.enumerations.CharacterCardInfo;
import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.board.Archipelago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class ComputeInfluenceEffectTest {
    private CharacterCard c = new CharacterCard(CharacterCardInfo.CARD3);

    /**
     * It inizialise with the need object the test environment
     */
   @BeforeEach
    void inizialise(){
        ExpertGameManager gm = new ExpertGameManager();
        c.setGameManager(gm);
        c.getGameManager().addPlayer("g1");
        c.getGameManager().addPlayer("g2");
        gm.newGame();
        c.setSelectedIsland(gm.getBoard().getIslandManager().getIslandByIndex(7));
        c.setBoard(gm.getBoard());
        gm.getBoard().getIslandManager().getIslandByIndex(gm.getBoard().getMotherNature().getIslandIndex()).setStudents(new StudentGroup(2));
        c.setPlayerThisTurn(c.getGameManager().getPlayers().get(0));
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        for(Colour color: Colour.values()) //this for is to be sure that the tests didn't depends on the bag variabily on extraction of students
            en.put(color,c.getGameManager().getPlayers().get(0).getTower());
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
        assertEquals(7,c.getPlayerThisTurn().getDashboard().getNumTowers());
        //assertEquals(c.getGameManager().getPlayers().get(0).getTower(),c.getSelectedIsland().getTower());
        assertEquals(8,c.getGameManager().getPlayers().get(1).getDashboard().getNumTowers());
        //assertFalse(c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).isPresenceMotherNature());
        //assertEquals(islandbefore,c.getBoard().getMotherNature().getIslandIndex());
    }
}