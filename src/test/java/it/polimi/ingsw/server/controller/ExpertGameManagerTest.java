package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.exceptions.WrongPhaseException;
import it.polimi.ingsw.server.exceptions.WrongTurnException;
import it.polimi.ingsw.server.model.ProfessorGroup;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.Cloud;
import it.polimi.ingsw.server.model.board.MotherNature;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import it.polimi.ingsw.server.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpertGameManagerTest {
    private final ExpertGameManager gm = new ExpertGameManager();

    /**
     * The tests verify the correct initialization of players
     */
    @Test
    void addPlayer(){
        gm.addPlayer();
        assertEquals(Tower.WHITE, gm.getPlayers().get(0).getTower());
        assertEquals(1, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(0).getCoins());
        gm.addPlayer();
        assertEquals(Tower.BLACK, gm.getPlayers().get(1).getTower());
        assertEquals(2, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(1).getCoins());
        gm.addPlayer();
        assertEquals(Tower.GRAY, gm.getPlayers().get(2).getTower());
        assertEquals(3, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(2).getCoins());
    }

    /**
     * It tests that the Game is ready with all right  parameters
     */
    @Test
    void newGame(){
        ExpertGameManager gm1 = new ExpertGameManager();
        CurrentStatus cs;
        assertFalse(gm1.isGameStarted());
        gm1.addPlayer();
        gm1.addPlayer();
        gm1.newGame(true, 2);
        assertEquals(2, gm1.getPlayers().size());
        assertTrue(gm1.isGameStarted());
        ArrayList<Player> players1 = gm1.getPlayers();
        for(Player p: players1) {
            assertEquals(8, p.getDashboard().getNumTowers());
            assertEquals(0, p.getDashboard().getHall().getTotalStudents());
            assertEquals(0, p.getDashboard().getEntrance().getTotalStudents());
            for (int i = 0; i < 10; i++)
                assertFalse(p.getAssistantCard(i).isPlayed());
            assertEquals(1, p.getCoins());
            for (Colour c : Colour.values()) {
                assertEquals(0, p.getDashboard().getHall().getQuantityColour(c));
                assertEquals(0, p.getDashboard().getEntrance().getQuantityColour(c));
            }
        }
        for(CharacterCard c: gm1.getBoard().getCharacterCards()) {
            assertFalse(c.isActivated());
            assertFalse(c.getCoinOnIt());
            if (c.getCardInfo().equals(CharacterCardInfo.CARD1) || c.getCardInfo().equals(CharacterCardInfo.CARD11)){
                assertEquals(4, c.getStudentsOnCard().getTotalStudents());
            }else if(c.getCardInfo().equals(CharacterCardInfo.CARD7)){
                assertEquals(6, c.getStudentsOnCard().getTotalStudents());
            }else{
                assertEquals(0,c.getStudentsOnCard().getTotalStudents());
            }
            assertNull(c.getSelectedStudentsFrom());
            assertNull(c.getSelectedStudentsTo());
            if(c.getCardInfo().equals(CharacterCardInfo.CARD5))
                assertEquals(4, c.getNoEntryTiles());
            else assertEquals(0, c.getNoEntryTiles());
            assertNull(c.getSelectedColour());
            assertNull(c.getSelectedIsland());
            assertEquals(c.getPrice(), c.getCardInfo().getPrice());
        }

        cs = gm1.getFullCurrentStatus();
        assertEquals("expert", cs.getGameMode());
        ArrayList<Archipelago> archipelagos = gm1.getBoard().getIslandManager().getArchipelagos();
        assertEquals(12, archipelagos.size());
        int i = 1;
        int mnIndex = gm1.getBoard().getMotherNature().getIslandIndex();
        ProfessorGroup pf = gm1.getBoard().getProfessorGroup();
        for(Colour c: Colour.values())
            assertNull(pf.getTower(c));
        for(Tower t: Tower.values())
            assertEquals(0, pf.getColours(t).size());
        for(Cloud cloud: gm1.getBoard().getClouds())
            assertEquals(3, cloud.getStudentsOnCloud().getTotalStudents());
        for(Archipelago a: archipelagos){
            if(i == mnIndex)
                assertTrue(a.isPresenceMotherNature());
            assertEquals(i, a.getFirstIslandIndex());
            assertEquals(i, a.getIslands().get(0).getIslandIndex());
            assertEquals(1, a.getIslands().size());
            assertTrue(a.getIslands().get(0).getStudents().getTotalStudents()>=0);
            for(Colour c: Colour.values())
                assertTrue(a.getIslands().get(0).getStudents().getQuantityColour(c)>=0);
            assertNull(a.getIslands().get(0).getTower());
            i++;
        }

        ExpertGameManager gm2 = new ExpertGameManager();
        assertFalse(gm2.isGameStarted());
        gm2.addPlayer();
        gm2.addPlayer();
        gm2.addPlayer();
        gm2.newGame(true, 3);
        assertEquals(3, gm2.getPlayers().size());
        assertTrue(gm2.isGameStarted());
        ArrayList<Player> players2 = gm2.getPlayers();
        assertEquals(3, gm2.getBoard().getClouds().size());
        for(Cloud cloud: gm2.getBoard().getClouds()) {
           assertEquals(4, cloud.getStudentsOnCloud().getTotalStudents());
        }
        for(Player p: players2){
            assertEquals(6,p.getDashboard().getNumTowers());
            assertEquals(0, p.getDashboard().getHall().getTotalStudents());
            assertEquals(0, p.getDashboard().getEntrance().getTotalStudents());
            for(int j = 0; i < 10; i++)
                assertFalse(p.getAssistantCard(j).isPlayed());
            assertEquals(1, p.getCoins());
            for(Colour c: Colour.values()){
                assertEquals(0, p.getDashboard().getHall().getQuantityColour(c));
                assertEquals(0, p.getDashboard().getEntrance().getQuantityColour(c));
            }
        }
        cs = gm2.getFullCurrentStatus();
        assertEquals("expert", cs.getGameMode());


        ExpertGameManager gm3 = new ExpertGameManager();
        assertFalse(gm3.isGameStarted());
        gm3.addPlayer();
        gm3.addPlayer();
        gm3.addPlayer();
        gm3.newGame(false, 2);
        assertEquals(2, gm3.getPlayers().size());
        assertTrue(gm3.isGameStarted());
        cs = gm3.getFullCurrentStatus();
        ArrayList<Player> players3 = gm3.getPlayers();
        for(Player p: players3) {
            assertEquals(8, p.getDashboard().getNumTowers());
            assertEquals(0, p.getDashboard().getHall().getTotalStudents());
            assertEquals(0, p.getDashboard().getEntrance().getTotalStudents());
            for (int j = 0; j < 10; j++)
                assertFalse(p.getAssistantCard(j).isPlayed());
            assertEquals(1, p.getCoins());
            for (Colour c : Colour.values()) {
                assertEquals(0, p.getDashboard().getHall().getQuantityColour(c));
                assertEquals(0, p.getDashboard().getEntrance().getQuantityColour(c));
            }
        }
        assertEquals("simple", cs.getGameMode());
    }

    void playAssistantCard(){
        ExpertGameManager gm1 = new ExpertGameManager();
        gm1.addPlayer();
        gm1.addPlayer();
        gm1.newGame(true, 2);
        for(int j = 0; j < gm1.getPlayers().size(); j++) {
            for (int i = 0; i < 10; i++) {
                assertFalse(gm1.getPlayers().get(j).getAssistantCard(i).isPlayed());
                try {
                    gm1.playAssistantCard(j, i);
                }catch (WrongPhaseException e){
                    e.printStackTrace();
                }catch (WrongTurnException f){
                    f.printStackTrace();
                }catch (IllegalArgumentException h){
                    h.printStackTrace();
                }catch (AlreadyPlayedException z){
                    z.printStackTrace();
                }
                assertTrue(gm1.getPlayers().get(j).getAssistantCard(i).isPlayed());
            }
        }
        //To test AlreadyPlayedException's catching
        for(int j = 0; j < gm1.getPlayers().size(); j++) {
            for (int i = 0; i < 10; i++) {
                assertFalse(gm1.getPlayers().get(j).getAssistantCard(i).isPlayed());
                try {
                    gm1.playAssistantCard(j, i);
                } catch (WrongPhaseException e) {
                    e.printStackTrace();
                } catch (WrongTurnException f) {
                    f.printStackTrace();
                } catch (IllegalArgumentException h) {
                    h.printStackTrace();
                } catch (AlreadyPlayedException z) {
                    z.printStackTrace();
                }
                assertTrue(gm1.getPlayers().get(j).getAssistantCard(i).isPlayed());
            }
        }

        //To test WrongTurnExcpetion's catching
        gm1.getTurnManager().setCurrentPlayer(1);
        for(int i = 0; i < 10; i++) {
            try {
                gm1.playAssistantCard(0, i);
            }catch (WrongPhaseException e){
                e.printStackTrace();
            }catch (WrongTurnException f){
                f.printStackTrace();
            }catch (IllegalArgumentException h){
                h.printStackTrace();
            }catch (AlreadyPlayedException z){
                z.printStackTrace();
            }
        }

        //To test WrongPhaseException's catching
        gm1.getTurnManager().setPhase(Phase.ACTION);
        for(int i = 0; i < 10; i++) {
            try {
                gm1.playAssistantCard(0, i);
            }catch (WrongPhaseException e){
                e.printStackTrace();
            }catch (WrongTurnException f){
                f.printStackTrace();
            }catch (IllegalArgumentException h){
                h.printStackTrace();
            }catch (AlreadyPlayedException z){
                z.printStackTrace();
            }
        }

        //To test IllegalArgumentException's catching
        try {
            gm1.playAssistantCard(0, 11);
        }catch (WrongPhaseException e){
            e.printStackTrace();
        }catch (WrongTurnException f){
            f.printStackTrace();
        }catch (IllegalArgumentException h){
            h.printStackTrace();
        }catch (AlreadyPlayedException z){
            z.printStackTrace();
        }
    }
}