package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.server.enumerations.AssistantCardInfo;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.ProfessorGroup;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.Cloud;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import it.polimi.ingsw.server.model.players.AssistantCard;
import it.polimi.ingsw.server.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

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

    @Test
    /**
     * It tests the right resolution of the playAssistantCard method and the corresponding Exceptions' catch
     */
    void playAssistantCard(){
        ExpertGameManager gm1 = new ExpertGameManager();
        gm1.addPlayer();
        gm1.addPlayer();
        gm1.newGame(true, 2);
        int i = new Random().nextInt(10);
        for(int j = 0; j < gm1.getPlayers().size(); j++) {
            int a = gm1.getTurnManager().getCurrentPlayer(); //when the card is played, the current Player is changed. I have to memorize the actual one to verify if the card is played properly
            assertFalse(gm1.getPlayers().get(gm1.getTurnManager().getCurrentPlayer()).getAssistantCard(i).isPlayed());
            try {
                gm1.playAssistantCard(gm1.getTurnManager().getCurrentPlayer(), i);
            }catch (WrongPhaseException e){
                e.printStackTrace();
            }catch (WrongTurnException f){
                f.printStackTrace();
            }catch (IllegalArgumentException h){
                h.printStackTrace();
            }catch (AlreadyPlayedException z){
                z.printStackTrace();
            }
            assertTrue(gm1.getPlayers().get(a).getAssistantCard(i).isPlayed());
        }
        //To test AlreadyPlayedException's catching
       ExpertGameManager gm2 = new ExpertGameManager();
        gm2.addPlayer();
        gm2.addPlayer();
        gm2.newGame(true, 2);
        for(int z = 0; z < gm2.getPlayers().size(); z++)
            gm2.getPlayers().get(z).getAssistantCard(0).setPlayed(true);
        for(int j = 0; j < gm2.getPlayers().size(); j++) {
            assertTrue(gm2.getPlayers().get(gm2.getTurnManager().getCurrentPlayer()).getAssistantCard(0).isPlayed());
            try {
                gm2.playAssistantCard(gm2.getTurnManager().getCurrentPlayer(), 0);
            } catch (WrongPhaseException e) {
                e.printStackTrace();
            } catch (WrongTurnException f) {
                f.printStackTrace();
            } catch (IllegalArgumentException h) {
                h.printStackTrace();
            } catch (AlreadyPlayedException z) {
                z.printStackTrace();
            }
        }

        //To test WrongTurnException's catching
        ExpertGameManager gm3 = new ExpertGameManager();
        gm3.addPlayer();
        gm3.addPlayer();
        gm3.newGame(true, 2);
        int v = 0;
        while(v == gm3.getTurnManager().getCurrentPlayer())
            v++;
        try {
            gm3.playAssistantCard(v, 2);
        }catch (WrongPhaseException e){
            e.printStackTrace();
        }catch (WrongTurnException f){
            f.printStackTrace();
        }catch (IllegalArgumentException h){
            h.printStackTrace();
        }catch (AlreadyPlayedException z){
            z.printStackTrace();
        }

        //To test WrongPhaseException's catching
        gm1.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm1.playAssistantCard(0, 0);
        }catch (WrongPhaseException e){
            e.printStackTrace();
        }catch (WrongTurnException f){
            f.printStackTrace();
        }catch (IllegalArgumentException h){
            h.printStackTrace();
        }catch (AlreadyPlayedException z){
            z.printStackTrace();
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


    @Test
    /**
     * It tests the right resolution of the moveStudentToHall method and the corresponding Exceptions' catch
     */
    void moveStudentToHall(){
        ExpertGameManager gm = new ExpertGameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        StudentGroup sh = new StudentGroup(3);
        StudentGroup se = new StudentGroup(7);
        try {
            gm.getPlayers().get(0).fillHall(sh);
            gm.getPlayers().get(0).fillDashboardEntrance(se);
        }catch (FullHallException e){
            e.printStackTrace();
        }

        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);
        order.add(1);
        gm.getTurnManager().setActionOrder(order);
        gm.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm.moveStudentsToHall(0, Colour.YELLOW);
            assertEquals(sh.getQuantityColour(Colour.YELLOW)+1, gm.getPlayers().get(0).getDashboard().getHall().getQuantityColour(Colour.YELLOW));
        }catch (FullHallException e){
            e.printStackTrace();
        }catch (NoStudentsException f){
            f.printStackTrace();
        }catch (WrongPhaseException h){
            h.printStackTrace();
        }catch (WrongTurnException h){
            h.printStackTrace();
        }

        //to test the FullHallException's catching
        ExpertGameManager gm4 = new ExpertGameManager();
        gm4.addPlayer();
        gm4.addPlayer();
        gm4.newGame(true, 2);
        StudentGroup sh2 = new StudentGroup(10);
        try {
            gm4.getPlayers().get(0).fillHall(sh2);
            gm4.getPlayers().get(0).fillDashboardEntrance(se);
        }catch (FullHallException e){
            e.printStackTrace();
        }
        gm4.getTurnManager().setActionOrder(order);
        gm4.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm4.moveStudentsToHall(0, Colour.YELLOW);
            assertEquals(sh.getQuantityColour(Colour.YELLOW)+1, gm4.getPlayers().get(0).getDashboard().getHall().getQuantityColour(Colour.YELLOW));
        }catch (FullHallException e){
            e.printStackTrace();
        }catch (NoStudentsException f){
            f.printStackTrace();
        }catch (WrongPhaseException h){
            h.printStackTrace();
        }catch (WrongTurnException h){
            h.printStackTrace();
        }

        //To test the NoStudentException's Catching
        ExpertGameManager gm1 = new ExpertGameManager();
        gm1.addPlayer();
        gm1.addPlayer();
        gm1.newGame(true, 2);
        StudentGroup se2 = new StudentGroup();
        try {
            gm1.getPlayers().get(0).fillHall(sh);
            gm1.getPlayers().get(0).fillDashboardEntrance(se2);
        }catch (FullHallException e){
            e.printStackTrace();
        }
        gm1.getTurnManager().setActionOrder(order);
        gm1.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm1.moveStudentsToHall(0, Colour.YELLOW);
            assertEquals(sh.getQuantityColour(Colour.YELLOW)+1, gm1.getPlayers().get(0).getDashboard().getHall().getQuantityColour(Colour.YELLOW));
        }catch (FullHallException e){
            e.printStackTrace();
        }catch (NoStudentsException f){
            f.printStackTrace();
        }catch (WrongPhaseException h){
            h.printStackTrace();
        }catch (WrongTurnException h){
            h.printStackTrace();
        }

        //To test WrongTurnException's catching
        ExpertGameManager gm2 = new ExpertGameManager();
        gm2.addPlayer();
        gm2.addPlayer();
        gm2.newGame(true, 2);
        gm2.getTurnManager().setActionOrder(order);
        gm2.getTurnManager().setPhase(Phase.ACTION);
        int v = 0;
        while(v == gm2.getTurnManager().getCurrentPlayer())
            v++;
        try {
            gm2.moveStudentsToHall(v, Colour.BLUE);
        }catch (WrongPhaseException e){
            e.printStackTrace();
        }catch (WrongTurnException f){
            f.printStackTrace();
        }catch (FullHallException h){
            h.printStackTrace();
        }catch (NoStudentsException z){
            z.printStackTrace();
        }

        //To test WrongPhaseException's catching
        gm1.getTurnManager().setPhase(Phase.PLANNING);
        try {
            gm1.moveStudentsToHall(0, Colour.BLUE);
        }catch (WrongPhaseException e){
            e.printStackTrace();
        }catch (WrongTurnException f){
            f.printStackTrace();
        }catch (FullHallException h){
            h.printStackTrace();
        }catch (NoStudentsException z){
            z.printStackTrace();
        }
    }

    @Test
    /**
     * It tests the right resolution of the moveStudentToIsland method and the corresponding Exceptions' catch
     */
    void moveStudentsToIsland(){
        ExpertGameManager gm = new ExpertGameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        StudentGroup se = new StudentGroup(4);
        gm.getPlayers().get(0).fillDashboardEntrance(se);
        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);
        order.add(1);
        gm.getTurnManager().setActionOrder(order);
        gm.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm.moveStudentToIsland(0, Colour.YELLOW, 3);
        }catch (NoStudentsException f){
            f.printStackTrace();
        }catch (WrongPhaseException h){
            h.printStackTrace();
        }catch (WrongTurnException h){
            h.printStackTrace();
        }

        //To test the NoStudentException's Catching
        ExpertGameManager gm1 = new ExpertGameManager();
        gm1.addPlayer();
        gm1.addPlayer();
        gm1.newGame(true, 2);
        StudentGroup se2 = new StudentGroup();
        gm1.getPlayers().get(0).fillDashboardEntrance(se2);
        gm1.getTurnManager().setActionOrder(order);
        gm1.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm1.moveStudentToIsland(0, Colour.BLUE, 8);
        }catch (NoStudentsException f){
            f.printStackTrace();
        }catch (WrongPhaseException h){
            h.printStackTrace();
        }catch (WrongTurnException h){
            h.printStackTrace();
        }
        //To test WrongTurnException's catching
        ExpertGameManager gm2 = new ExpertGameManager();
        gm2.addPlayer();
        gm2.addPlayer();
        gm2.newGame(true, 2);
        gm2.getTurnManager().setActionOrder(order);
        gm2.getTurnManager().setPhase(Phase.ACTION);
        int v = 0;
        while(v == gm2.getTurnManager().getCurrentPlayer())
             v++;
        try {
            gm2.moveStudentToIsland(v, Colour.BLUE, 2);
        }catch (WrongPhaseException e){
            e.printStackTrace();
         }catch (WrongTurnException f){
            f.printStackTrace();
        }catch (NoStudentsException h){
            h.printStackTrace();
        }

        //To test WrongPhaseException's catching
        gm.getTurnManager().setPhase(Phase.PLANNING);
        try {
            gm.moveStudentToIsland(1, Colour.GREEN, 4);
        }catch (WrongPhaseException e){
            e.printStackTrace();
        }catch (WrongTurnException f){
            f.printStackTrace();
        }catch (NoStudentsException z){
            z.printStackTrace();
        }
    }
    @Test
    /**
     * It tests the right resolution of the moveStudentToIsland method and the corresponding Exceptions' catch
     */
    void moveMotherNature(){
        ExpertGameManager gm = new ExpertGameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);
        order.add(1);
        gm.getTurnManager().setActionOrder(order);
        gm.getPlayers().get(0).getAssistantCard(3).setPlayed(true);
        gm.getPlayers().get(0).setLastPlayedCard(new AssistantCard(AssistantCardInfo.CARD3));
        try {
            gm.getTurnManager().setPhase(Phase.ACTION);
            gm.getTurnManager().setMotherNaturePhase(true);
            gm.moveMotherNature(2);
        }catch (WrongPhaseException h){
            h.printStackTrace();
        }catch (IllegalArgumentException i){
            i.printStackTrace();
        }

        //To test IllegalArgumentException's catching
        try {
            gm.getTurnManager().setPhase(Phase.ACTION);
            gm.getTurnManager().setMotherNaturePhase(true);
            gm.moveMotherNature(5);
        }catch (WrongPhaseException h){
            h.printStackTrace();
        }catch (IllegalArgumentException i){
            i.printStackTrace();
        }

        //To test WrongPhaseException's catching
        try {
            gm.getTurnManager().setPhase(Phase.PLANNING);
            gm.moveMotherNature(3);
        }catch (WrongPhaseException e){
            e.printStackTrace();
        }catch (IllegalArgumentException z){
            z.printStackTrace();
        }
    }

}