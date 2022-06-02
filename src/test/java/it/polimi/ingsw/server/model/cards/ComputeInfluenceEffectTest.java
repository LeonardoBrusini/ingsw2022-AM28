package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.model.ProfessorGroup;
import it.polimi.ingsw.server.model.StudentGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class ComputeInfluenceEffectTest {
    private final CharacterCard c = new CharacterCard(CharacterCardInfo.CARD3);

    /**
     * It initialises with the need object the test environment
     */
   @BeforeEach
    void initialise(){
        GameManager gm = new GameManager();
        c.setGameManager(gm);
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        gm.getPlayers().get(0).setNickname("p1");
        gm.getPlayers().get(1).setNickname("p2");
        c.setSelectedIsland(gm.getBoard().getIslandManager().getIslandByIndex(7));
        c.setBoard(gm.getBoard());
        gm.getBoard().getIslandManager().getIslandByIndex(gm.getBoard().getMotherNature().getIslandIndex()).setStudents(new StudentGroup(2));
        c.setPlayerThisTurn(c.getGameManager().getPlayers().get(0));
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        for(Colour color: Colour.values()) {
            //this for is to be sure that the tests didn't depend on the bag variability on extraction of students
            en.put(color,c.getGameManager().getPlayers().get(0).getTower());
            System.out.print(en.get(color)+" ");
        }
        System.out.println();
        ProfessorGroup pg = new ProfessorGroup();
        pg.setProfessors(en);
        for(int p: pg.getStatus()) System.out.print(p+" ");
       System.out.println();
        c.getBoard().setProfessorGroup(pg);
        for(int p: c.getBoard().getProfessorGroup().getStatus()) System.out.print(p+" ");
        System.out.println();
    }

    /**
     * It verifies the correct execution of the card's effect and that it leaves the environment as expected
     */
   @Test
    void resolveEffect() {
        int islandBefore = c.getBoard().getMotherNature().getIslandIndex();
        assertEquals(7, c.getSelectedIsland().getIslandIndex());
        c.getCardInfo().getEffect().resolveEffect(c);
        if(c.getBoard().getIslandManager().getIslandByIndex(7).getStudents().empty()) {
            assertEquals(8,c.getPlayerThisTurn().getDashboard().getNumTowers());
        } else {
            assertEquals(7,c.getPlayerThisTurn().getDashboard().getNumTowers());
            assertEquals(c.getGameManager().getPlayers().get(0).getTower(),c.getSelectedIsland().getTower());
        }
        assertEquals(8,c.getGameManager().getPlayers().get(1).getDashboard().getNumTowers());
        if(islandBefore==c.getSelectedIsland().getIslandIndex()) {
            assertTrue(c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).isPresenceMotherNature());
        } else {
            assertFalse(c.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).isPresenceMotherNature());
        }
        assertEquals(islandBefore,c.getBoard().getMotherNature().getIslandIndex());
        assertTrue(c.getBoard().getIslandManager().getArchipelagoByIslandIndex(islandBefore).isPresenceMotherNature());
    }
}