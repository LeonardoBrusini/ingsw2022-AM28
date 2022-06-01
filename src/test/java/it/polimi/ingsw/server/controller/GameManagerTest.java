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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    private final GameManager gm = new GameManager();

    @BeforeEach
    void resetEOGChecker() {
        EndOfGameChecker.resetInstance();
    }

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
        assertEquals(Tower.GREY, gm.getPlayers().get(2).getTower());
        assertEquals(3, gm.getPlayers().size());
        assertEquals(1, gm.getPlayers().get(2).getCoins());
    }

    /**
     * It tests that the Game is ready with all right  parameters
     */
    @Test
    void newGame(){
        GameManager gm1 = new GameManager();
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
            assertEquals(7, p.getDashboard().getEntrance().getTotalStudents());
            for (int i = 0; i < 10; i++)
                assertFalse(p.getAssistantCard(i).isPlayed());
            assertEquals(1, p.getCoins());
            for (Colour c : Colour.values()) {
                assertEquals(0, p.getDashboard().getHall().getQuantityColour(c));
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

        GameManager gm2 = new GameManager();
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
            assertEquals(9, p.getDashboard().getEntrance().getTotalStudents());
            for(int j = 0; i < 10; i++)
                assertFalse(p.getAssistantCard(j).isPlayed());
            assertEquals(1, p.getCoins());
            for(Colour c: Colour.values()){
                assertEquals(0, p.getDashboard().getHall().getQuantityColour(c));
            }
        }
        cs = gm2.getFullCurrentStatus();
        assertEquals("expert", cs.getGameMode());


        GameManager gm3 = new GameManager();
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
            assertEquals(7, p.getDashboard().getEntrance().getTotalStudents());
            for (int j = 0; j < 10; j++)
                assertFalse(p.getAssistantCard(j).isPlayed());
            assertEquals(1, p.getCoins());
            for (Colour c : Colour.values()) {
                assertEquals(0, p.getDashboard().getHall().getQuantityColour(c));
            }
        }
        assertEquals("simple", cs.getGameMode());
    }

    /**
     * It tests the right resolution of the playAssistantCard method and the corresponding Exceptions' catch
     */
    @Test
    void playAssistantCard(){
        GameManager gm1 = new GameManager();
        gm1.addPlayer();
        gm1.addPlayer();
        gm1.newGame(true, 2);
        int i = new Random().nextInt(10);
        for(int j = 0; j < gm1.getPlayers().size(); j++) {
            int a = gm1.getTurnManager().getCurrentPlayer(); //when the card is played, the current Player is changed. I have to memorize the actual one to verify if the card is played properly
            assertFalse(gm1.getPlayers().get(gm1.getTurnManager().getCurrentPlayer()).getAssistantCard(i).isPlayed());
            try {
                boolean isPlayed = false;
                gm1.getPlayers().get(gm1.getTurnManager().getCurrentPlayer());
                for(int z=0; z< gm1.getPlayers().size(); z++) {
                    if (gm1.getPlayers().get(z).getAssistantCard(i).isPlayed() && !gm1.getPlayers().get(z).equals(gm1.getPlayers().get(gm1.getTurnManager().getCurrentPlayer())))
                        isPlayed = true;
                }
                if(!isPlayed) {
                    gm1.playAssistantCard(gm1.getTurnManager().getCurrentPlayer(), i);
                    assertTrue(gm1.getPlayers().get(a).getAssistantCard(i).isPlayed());
                }
            }catch (WrongPhaseException | WrongTurnException | IllegalArgumentException | AlreadyPlayedException e){
                e.printStackTrace();
            }
        }
        //To test AlreadyPlayedException's catching
        GameManager gm2 = new GameManager();
        gm2.addPlayer();
        gm2.addPlayer();
        gm2.newGame(true, 2);
        for(int z = 0; z < gm2.getPlayers().size(); z++)
            gm2.getPlayers().get(z).getAssistantCard(0).setPlayed(true);
        try{
        for(int j = 0; j < gm2.getPlayers().size(); j++) {
            assertTrue(gm2.getPlayers().get(gm2.getTurnManager().getCurrentPlayer()).getAssistantCard(0).isPlayed());

                gm2.playAssistantCard(gm2.getTurnManager().getCurrentPlayer(), 0);

            }
        } catch (WrongPhaseException | WrongTurnException | IllegalArgumentException | AlreadyPlayedException e) {
            e.printStackTrace();
        }

        //To test WrongTurnException's catching
        GameManager gm3 = new GameManager();
        gm3.addPlayer();
        gm3.addPlayer();
        gm3.newGame(true, 2);
        int v = 0;
        while(v == gm3.getTurnManager().getCurrentPlayer())
            v++;
        try {
            gm3.playAssistantCard(v, 2);
        }catch (WrongPhaseException | WrongTurnException | IllegalArgumentException | AlreadyPlayedException e){
            e.printStackTrace();
        }

        //To test WrongPhaseException's catching
        gm1.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm1.playAssistantCard(0, 0);
        }catch (WrongPhaseException | WrongTurnException | IllegalArgumentException | AlreadyPlayedException e){
            e.printStackTrace();
        }

        //To test IllegalArgumentException's catching
       try {
            gm1.playAssistantCard(0, 11);
        }catch (WrongPhaseException | WrongTurnException | IllegalArgumentException | AlreadyPlayedException e){
            e.printStackTrace();
        }
        //To test AlreadyPlayedException's catching
        for(int j = 0; j < gm2.getPlayers().size(); j++) {
            int a = gm2.getTurnManager().getCurrentPlayer(); //when the card is played, the current Player is changed. I have to memorize the actual one to verify if the card is played properly
            if(!gm2.getPlayers().get(a).getAssistantCard(i).isPlayed())
                assertFalse(gm2.getPlayers().get(a).getAssistantCard(i).isPlayed());
            try {
                boolean isPlayed = false;
                gm2.getPlayers().get(gm2.getTurnManager().getCurrentPlayer());
                for(int z=0; z< gm1.getPlayers().size(); z++) {
                    if (gm2.getPlayers().get(z).getAssistantCard(i).isPlayed() && !gm2.getPlayers().get(z).equals(gm2.getPlayers().get(gm2.getTurnManager().getCurrentPlayer())))
                        isPlayed = true;
                }
                if(isPlayed) {
                    gm2.playAssistantCard(gm2.getTurnManager().getCurrentPlayer(), i);
                    //It should throw new IllegalArgumentException
                }
            }catch (WrongPhaseException | WrongTurnException | IllegalArgumentException | AlreadyPlayedException e){
                e.printStackTrace();
            }
        }
    }


    @Test
    /**
     * It tests the right resolution of the moveStudentToHall method and the corresponding Exceptions' catch
     */
    void moveStudentToHall(){
        GameManager gm = new GameManager();
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
        }catch (FullHallException | NoStudentsException | WrongPhaseException | WrongTurnException e){
            e.printStackTrace();
        }

        //to test the FullHallException's catching
        GameManager gm4 = new GameManager();
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
        }catch (FullHallException | NoStudentsException | WrongPhaseException | WrongTurnException e){
            e.printStackTrace();
        }

        //To test the NoStudentException's Catching
        GameManager gm1 = new GameManager();
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
        }catch (FullHallException | NoStudentsException | WrongPhaseException | WrongTurnException e){
            e.printStackTrace();
        }

        //To test WrongTurnException's catching
        GameManager gm2 = new GameManager();
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
        }catch (WrongPhaseException | WrongTurnException | FullHallException | NoStudentsException e){
            e.printStackTrace();
        }

        //To test WrongPhaseException's catching
        gm1.getTurnManager().setPhase(Phase.PLANNING);
        try {
            gm1.moveStudentsToHall(0, Colour.BLUE);
        }catch (WrongPhaseException | WrongTurnException | FullHallException | NoStudentsException e){
            e.printStackTrace();
        }
    }

    @Test
    /**
     * It tests the right resolution of the moveStudentToIsland method and the corresponding Exceptions' catch
     */
    void moveStudentsToIsland(){
        GameManager gm = new GameManager();
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
        }catch (NoStudentsException | WrongPhaseException | WrongTurnException f){
            f.printStackTrace();
        }

        //To test the NoStudentException's Catching
        GameManager gm1 = new GameManager();
        gm1.addPlayer();
        gm1.addPlayer();
        gm1.newGame(true, 2);
        StudentGroup se2 = new StudentGroup();
        gm1.getPlayers().get(0).fillDashboardEntrance(se2);
        gm1.getTurnManager().setActionOrder(order);
        gm1.getTurnManager().setPhase(Phase.ACTION);
        try {
            gm1.moveStudentToIsland(0, Colour.BLUE, 8);
        }catch (NoStudentsException | WrongPhaseException | WrongTurnException f){
            f.printStackTrace();
        }

        //To test WrongTurnException's catching
        GameManager gm2 = new GameManager();
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
        }catch (WrongPhaseException | WrongTurnException | NoStudentsException e){
            e.printStackTrace();
         }

        //To test WrongPhaseException's catching
        gm.getTurnManager().setPhase(Phase.PLANNING);
        try {
            gm.moveStudentToIsland(1, Colour.GREEN, 4);
        }catch (WrongPhaseException | WrongTurnException | NoStudentsException e){
            e.printStackTrace();
        }
    }
    @Test
    /**
     * It tests the right resolution of the moveMotherNature method and the corresponding Exceptions' catch
     */
    void moveMotherNature(){
        GameManager gm = new GameManager();
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
        }catch (WrongPhaseException | IllegalArgumentException h){
            h.printStackTrace();
        }

        //To test IllegalArgumentException's catching
        try {
            gm.getTurnManager().setPhase(Phase.ACTION);
            gm.getTurnManager().setMotherNaturePhase(true);
            gm.moveMotherNature(5);
        }catch (WrongPhaseException | IllegalArgumentException h){
            h.printStackTrace();
        }

        //To test WrongPhaseException's catching
        try {
            gm.getTurnManager().setPhase(Phase.PLANNING);
            gm.moveMotherNature(3);
        }catch (WrongPhaseException | IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    @Test
    /**
     * It tests the right resolution of the takeStudentsFromCloud method and the corresponding Exceptions' catch
     */
    void takeStudentsFromCloud(){
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);
        order.add(1);
        gm.getTurnManager().setActionOrder(order);
        gm.getBoard().fillClouds();
        int totCloud = gm.getBoard().getClouds().get(1).getStudentsOnCloud().getTotalStudents();
        int totEntrance = gm.getPlayers().get(0).getDashboard().getEntrance().getTotalStudents();
        try {
            gm.getTurnManager().setPhase(Phase.ACTION);
            gm.getTurnManager().setCloudSelectionPhase(true);
            gm.takeStudentsFromCloud(1, 0);
        }catch (WrongPhaseException | IllegalArgumentException | WrongTurnException | NoStudentsException e){
            e.printStackTrace();
        }
        assertEquals(0, gm.getBoard().getClouds().get(1).getStudentsOnCloud().getTotalStudents());
        assertEquals(totCloud + totEntrance, gm.getPlayers().get(0).getDashboard().getEntrance().getTotalStudents());

        //To test NoStudentException's catching in case of no students on cloud
        StudentGroup sc = new StudentGroup();
        gm.getBoard().getClouds().get(1).addGroup(sc);
        try {
            gm.getTurnManager().setPhase(Phase.ACTION);
            gm.getTurnManager().setCloudSelectionPhase(true);
            gm.takeStudentsFromCloud(1, 1);
        }catch (WrongPhaseException | IllegalArgumentException | WrongTurnException | NoStudentsException e){
            e.printStackTrace();
        }

        //To test WrongPhaseException's catching
        try {
            gm.getTurnManager().setPhase(Phase.PLANNING);
            gm.getTurnManager().setCloudSelectionPhase(false);
            gm.takeStudentsFromCloud(1, 0);
        }catch (WrongPhaseException | IllegalArgumentException | WrongTurnException | NoStudentsException e){
            e.printStackTrace();
        }

        //To test WrongTurnException's catching
        GameManager gm2 = new GameManager();
        gm2.addPlayer();
        gm2.addPlayer();
        gm2.newGame(true, 2);
        gm2.getTurnManager().setActionOrder(order);
        int v = 0;
        while(v == gm2.getTurnManager().getCurrentPlayer())
            v++;
        try {
            gm2.getTurnManager().setPhase(Phase.ACTION);
            gm2.getTurnManager().setCloudSelectionPhase(true);
            gm2.takeStudentsFromCloud(0, 1);
        }catch (WrongPhaseException | WrongTurnException | IllegalArgumentException | NoStudentsException e){
            e.printStackTrace();
        }

        //To test IllegalArgumentException's catching
        try {
            gm.getTurnManager().setPhase(Phase.ACTION);
            gm.getTurnManager().setCloudSelectionPhase(true);
            gm.takeStudentsFromCloud(4,0);
        }catch (WrongPhaseException | IllegalArgumentException | WrongTurnException | NoStudentsException h){
            h.printStackTrace();
        }

        GameManager gm3 = new GameManager();
        gm3.addPlayer();
        gm3.addPlayer();
        gm3.addPlayer();
        gm3.newGame(true, 3);
        order.add(0);
        order.add(1);
        order.add(2);
        gm3.getTurnManager().setActionOrder(order);
        try {
            gm3.getTurnManager().setPhase(Phase.ACTION);
            gm3.getTurnManager().setCloudSelectionPhase(true);
            gm3.takeStudentsFromCloud(4,0);
        }catch (WrongPhaseException | IllegalArgumentException | WrongTurnException | NoStudentsException h){
            h.printStackTrace();
        }
    }

    @Test
    void playCharacterCard2468(){
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.getPlayers().get(0).setCoins(8);
        gm.newGame(true, 2);
        TurnManager t = gm.getTurnManager();
        CharacterCard card2 = new CharacterCard(CharacterCardInfo.CARD2);
        CharacterCard card4 = new CharacterCard(CharacterCardInfo.CARD4);
        CharacterCard card6 = new CharacterCard(CharacterCardInfo.CARD6);
        CharacterCard card8 = new CharacterCard(CharacterCardInfo.CARD8);
        ArrayList<CharacterCard> cards = new ArrayList<>();
        cards.add(card2);
        cards.add(card4);
        gm.getBoard().setCharacterCards(cards);
        try {
            gm.getPlayers().get(0).playCard(3);
        }catch (AlreadyPlayedException e){
            throw new RuntimeException(e);
        }
        for(int i = 0; i < cards.size(); i++) {
            try {
                while (t.getCurrentPlayer()!=0 || t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
                //gm.getTurnManager().setPhase(Phase.ACTION);
                //gm.getTurnManager().setMoveStudentsPhase(true);
                //gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
        assertTrue(card2.isActivated());
        assertEquals(4, gm.getPlayers().get(0).getLastPlayedCard().getInfo().getMotherNatureShifts());
        ArrayList<CharacterCard> cards1 = new ArrayList<>();
        cards1.add(card6);
        cards1.add(card8);
        gm.getBoard().setCharacterCards(cards1);
        for(int i = 0; i < cards1.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
        assertTrue(card6.isActivated());
        assertTrue(card8.isActivated());

        //To test AlreadyPlayedException
        gm.getPlayers().get(0).setCoins(8);
        CharacterCard card12 = new CharacterCard(CharacterCardInfo.CARD2);
        CharacterCard card14 = new CharacterCard(CharacterCardInfo.CARD4);
        CharacterCard card16 = new CharacterCard(CharacterCardInfo.CARD6);
        CharacterCard card18 = new CharacterCard(CharacterCardInfo.CARD8);
        ArrayList<CharacterCard> cards2 = new ArrayList<>();
        cards2.add(card12);
        cards2.add(card14);
        gm.getBoard().setCharacterCards(cards2);
        for(int i = 0; i < cards2.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.playCharacterCard(0, i);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }

        //To test NotEnoughCoinsException
        CharacterCard card22 = new CharacterCard(CharacterCardInfo.CARD2);
        CharacterCard card24 = new CharacterCard(CharacterCardInfo.CARD4);
        ArrayList<CharacterCard> cards4 = new ArrayList<>();
        cards4.add(card22);
        cards4.add(card24);
        gm.getBoard().setCharacterCards(cards4);
        try {
            gm.getPlayers().get(0).playCard(7);
        }catch (AlreadyPlayedException e){
            throw new RuntimeException(e);
        }
        for(int i = 0; i < cards.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }


        //To test WrongPhaseException
        CharacterCard card32 = new CharacterCard(CharacterCardInfo.CARD2);
        CharacterCard card34 = new CharacterCard(CharacterCardInfo.CARD4);
        ArrayList<CharacterCard> cards5 = new ArrayList<>();
        cards5.add(card32);
        cards5.add(card34);
        gm.getBoard().setCharacterCards(cards5);
        try {
            gm.getPlayers().get(0).playCard(5);
        }catch (AlreadyPlayedException e){
            e.printStackTrace();
        }
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.PLANNING);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }

        //To test IllegalArgumentException
        CharacterCard card42 = new CharacterCard(CharacterCardInfo.CARD2);
        CharacterCard card44 = new CharacterCard(CharacterCardInfo.CARD4);
        ArrayList<CharacterCard> cards6 = new ArrayList<>();
        cards6.add(card42);
        cards6.add(card44);
        gm.getBoard().setCharacterCards(cards6);
        try {
            gm.getPlayers().get(0).playCard(4);
        }catch (AlreadyPlayedException e){
            throw new RuntimeException(e);
        }
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(2, i);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void playCharacterCard91112(){
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.getPlayers().get(0).setCoins(8);
        gm.newGame(true, 2);
        TurnManager t = gm.getTurnManager();
        try {
            gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(5));
            gm.getPlayers().get(1).getDashboard().fillHall(new StudentGroup(5));
        }catch (FullHallException h){
            h.printStackTrace();
        }
        CharacterCard card9 = new CharacterCard(CharacterCardInfo.CARD9);
        CharacterCard card11 = new CharacterCard(CharacterCardInfo.CARD11);
        CharacterCard card12 = new CharacterCard(CharacterCardInfo.CARD12);
        card11.setStudentsOnCard(new StudentGroup(5));
        ArrayList<CharacterCard> cards = new ArrayList<>();
        cards.add(card9);
        cards.add(card11);
        gm.getBoard().setCharacterCards(cards);
        for(int i = 0; i < cards.size(); i++) {
            try {
                while (t.getCurrentPlayer()!=0 || t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
                //gm.getTurnManager().setPhase(Phase.ACTION);
                //gm.getTurnManager().setMoveStudentsPhase(true);
                //gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCoins(10);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, Colour.YELLOW);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
        assertTrue(card9.isActivated());
        assertEquals(25, card11.getStudentsOnCard().getTotalStudents());
        assertEquals(26, gm.getPlayers().get(0).getDashboard().getHall().getTotalStudents());
        assertEquals(6, gm.getPlayers().get(0).getDashboard().getHall().getQuantityColour(card11.getSelectedColour()));
        cards.add(card12);
        gm.getBoard().setCharacterCards(cards);
        try {
            gm.getTurnManager().setPhase(Phase.ACTION);
            gm.getTurnManager().setMoveStudentsPhase(true);
            gm.getTurnManager().setCurrentPlayer(1);
            gm.getPlayers().get(1).setCoins(10);
            gm.getPlayers().get(1).setCcActivatedThisTurn(false);
            gm.playCharacterCard(1, 2, Colour.YELLOW);
        } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
            e.printStackTrace();
        }
        assertEquals(23, gm.getPlayers().get(0).getDashboard().getHall().getTotalStudents());
        assertEquals(3, gm.getPlayers().get(0).getDashboard().getHall().getQuantityColour(card12.getSelectedColour()));
        assertEquals(22, gm.getPlayers().get(1).getDashboard().getHall().getTotalStudents());
        assertEquals(2, gm.getPlayers().get(1).getDashboard().getHall().getQuantityColour(card12.getSelectedColour()));

        //To test AlreadyPlayedException
        gm.getPlayers().get(0).setCoins(8);
        CharacterCard card19 = new CharacterCard(CharacterCardInfo.CARD9);
        CharacterCard card111 = new CharacterCard(CharacterCardInfo.CARD11);
        ArrayList<CharacterCard> cards2 = new ArrayList<>();
        cards2.add(card19);
        cards2.add(card111);
        card111.setStudentsOnCard(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards2);
        for(int i = 0; i < cards2.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.playCharacterCard(0, i, Colour.YELLOW);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }

        //To test NotEnoughCoinsException
        CharacterCard card29 = new CharacterCard(CharacterCardInfo.CARD9);
        CharacterCard card211 = new CharacterCard(CharacterCardInfo.CARD11);
        ArrayList<CharacterCard> cards3 = new ArrayList<>();
        cards3.add(card29);
        cards3.add(card211);
        card211.setStudentsOnCard(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards3);
        for(int i = 0; i < cards3.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, Colour.YELLOW);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }


        //To test WrongPhaseException
        CharacterCard card39 = new CharacterCard(CharacterCardInfo.CARD9);
        CharacterCard card311 = new CharacterCard(CharacterCardInfo.CARD11);
        ArrayList<CharacterCard> cards4 = new ArrayList<>();
        cards4.add(card39);
        cards4.add(card311);
        gm.getBoard().setCharacterCards(cards4);
        card311.setStudentsOnCard(new StudentGroup(5));
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards4.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.PLANNING);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, Colour.YELLOW);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }

        //To test IllegalArgumentException
        CharacterCard card49 = new CharacterCard(CharacterCardInfo.CARD9);
        CharacterCard card411 = new CharacterCard(CharacterCardInfo.CARD11);
        ArrayList<CharacterCard> cards5 = new ArrayList<>();
        cards5.add(card49);
        cards5.add(card411);
        card411.setStudentsOnCard(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards5);
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards5.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(2, i, Colour.YELLOW);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void playAssistantCard1(){
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.getPlayers().get(0).setCoins(8);
        gm.newGame(true, 2);
        TurnManager t = gm.getTurnManager();
        CharacterCard card1 = new CharacterCard(CharacterCardInfo.CARD1);
        ArrayList<CharacterCard> cards = new ArrayList<>();
        card1.setStudentsOnCard(new StudentGroup(3));
        cards.add(card1);
        gm.getBoard().setCharacterCards(cards);
        gm.getPlayers().get(0).setCoins(8);
        int beforeC = gm.getBoard().getIslandManager().getIslandByIndex(4).getStudents().getQuantityColour(Colour.YELLOW);
        int beforeS = gm.getBoard().getIslandManager().getIslandByIndex(4).getStudents().getTotalStudents();
        for(int i = 0; i < cards.size(); i++) {
            try {
                while (t.getCurrentPlayer()!=0 || t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
                //gm.getTurnManager().setPhase(Phase.ACTION);
                //gm.getTurnManager().setMoveStudentsPhase(true);
                //gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, Colour.YELLOW, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
        beforeC++;
        beforeS++;
        assertEquals(beforeS, gm.getBoard().getIslandManager().getIslandByIndex(4).getStudents().getTotalStudents());
        assertEquals(beforeC, gm.getBoard().getIslandManager().getIslandByIndex(4).getStudents().getQuantityColour(card1.getSelectedColour()));
        assertEquals(15, card1.getStudentsOnCard().getTotalStudents());


        //To test AlreadyPlayedException
        gm.getPlayers().get(0).setCoins(8);
        CharacterCard card11 = new CharacterCard(CharacterCardInfo.CARD1);
        ArrayList<CharacterCard> cards2 = new ArrayList<>();
        cards2.add(card11);
        card11.setStudentsOnCard(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards2);
        for(int i = 0; i < cards2.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.playCharacterCard(0, i, Colour.YELLOW, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }


        //To test NotEnoughCoinsException
        gm.getPlayers().get(0).setCoins(0);
        CharacterCard card21 = new CharacterCard(CharacterCardInfo.CARD1);
        ArrayList<CharacterCard> cards3 = new ArrayList<>();
        cards3.add(card21);
        card21.setStudentsOnCard(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards3);
        for(int i = 0; i < cards3.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, Colour.YELLOW, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }


        //To test WrongPhaseException
        gm.getPlayers().get(0).setCoins(8);
        CharacterCard card31 = new CharacterCard(CharacterCardInfo.CARD1);
        ArrayList<CharacterCard> cards4 = new ArrayList<>();
        cards4.add(card31);
        card31.setStudentsOnCard(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards4);
        for(int i = 0; i < cards4.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.PLANNING);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, Colour.YELLOW, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }

        //To test IllegalArgumentException
        gm.getPlayers().get(0).setCoins(8);
        CharacterCard card41 = new CharacterCard(CharacterCardInfo.CARD1);
        ArrayList<CharacterCard> cards5 = new ArrayList<>();
        cards5.add(card41);
        card41.setStudentsOnCard(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards5);
        for(int i = 0; i < cards5.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(2, i, Colour.YELLOW, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void playCharacterCard35(){
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.getPlayers().get(0).setCoins(8);
        gm.newGame(true, 2);
        TurnManager t = gm.getTurnManager();
        CharacterCard card3 = new CharacterCard(CharacterCardInfo.CARD3);
        CharacterCard card5 = new CharacterCard(CharacterCardInfo.CARD5);
        ArrayList<CharacterCard> cards = new ArrayList<>();
        card5.setNoEntryTiles(1);
        cards.add(card3);
        cards.add(card5);
        gm.getBoard().setCharacterCards(cards);
        int before = gm.getBoard().getMotherNature().getIslandIndex();
        gm.getBoard().getProfessorGroup().setTower(Colour.YELLOW, gm.getPlayers().get(0).getTower());
        StudentGroup si = new StudentGroup();
        si.addStudent(Colour.YELLOW);
        gm.getBoard().getIslandManager().getIslandByIndex(4).setStudents(si);
        for(int i = 0; i < cards.size(); i++) {
            try {
                while (t.getCurrentPlayer()!=0 || t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
                //gm.getTurnManager().setPhase(Phase.ACTION);
                //gm.getTurnManager().setMoveStudentsPhase(true);
                //gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
        assertEquals(1, gm.getBoard().getIslandManager().getArchipelagoByIslandIndex(4).getNoEntryTiles());
        assertEquals(0, card5.getNoEntryTiles());
        assertEquals(before, gm.getBoard().getMotherNature().getIslandIndex());
        assertEquals(gm.getPlayers().get(0).getTower(), gm.getBoard().getIslandManager().getIslandByIndex(4).getTower());

        //To test AlreadyPlayedException
        CharacterCard card13 = new CharacterCard(CharacterCardInfo.CARD3);
        CharacterCard card15 = new CharacterCard(CharacterCardInfo.CARD5);
        ArrayList<CharacterCard> cards2 = new ArrayList<>();
        card5.setNoEntryTiles(1);
        cards2.add(card13);
        cards2.add(card15);
        gm.getBoard().setCharacterCards(cards2);
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards2.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.playCharacterCard(0, i, 4);
            } catch (IllegalArgumentException | WrongTurnException | AlreadyPlayedException | NotEnoughCoinsException | WrongPhaseException e) {
                e.printStackTrace();
            }
        }


        //To test NotEnoughCoinsException
        CharacterCard card23 = new CharacterCard(CharacterCardInfo.CARD3);
        CharacterCard card25 = new CharacterCard(CharacterCardInfo.CARD5);
        ArrayList<CharacterCard> cards3 = new ArrayList<>();
        card5.setNoEntryTiles(1);
        cards3.add(card23);
        cards3.add(card25);
        gm.getBoard().setCharacterCards(cards3);
        for(int i = 0; i < cards3.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }


        //To test WrongPhaseException
        CharacterCard card33 = new CharacterCard(CharacterCardInfo.CARD3);
        CharacterCard card35 = new CharacterCard(CharacterCardInfo.CARD5);
        ArrayList<CharacterCard> cards4 = new ArrayList<>();
        card5.setNoEntryTiles(1);
        cards4.add(card33);
        cards4.add(card35);
        gm.getBoard().setCharacterCards(cards4);
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards4.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.PLANNING);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, 4);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }

        //To test IllegalArgumentException
        CharacterCard card43 = new CharacterCard(CharacterCardInfo.CARD3);
        CharacterCard card45 = new CharacterCard(CharacterCardInfo.CARD5);
        ArrayList<CharacterCard> cards5 = new ArrayList<>();
        card5.setNoEntryTiles(1);
        cards5.add(card43);
        cards5.add(card45);
        gm.getBoard().setCharacterCards(cards5);
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards5.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(2, i,  4);
            } catch (IllegalArgumentException | WrongTurnException | AlreadyPlayedException | NotEnoughCoinsException | WrongPhaseException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void playCharacterCard710(){
        GameManager gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true, 2);
        TurnManager t = gm.getTurnManager();
        CharacterCard card7 = new CharacterCard(CharacterCardInfo.CARD7);
        CharacterCard card10 = new CharacterCard(CharacterCardInfo.CARD10);
        ArrayList<CharacterCard> cards = new ArrayList<>();
        StudentGroup sf = new StudentGroup();
        sf.addStudent(Colour.RED);
        StudentGroup st = new StudentGroup();
        st.addStudent(Colour.BLUE);
        StudentGroup soc = new StudentGroup(5);
        card7.setStudentsOnCard(soc);
        card10.setStudentsOnCard(soc);
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getPlayers().get(1).getDashboard().fillEntrance(new StudentGroup(5));
        int tot11 = gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(Colour.RED);
        int tot12 = gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(Colour.BLUE);
        try {
            gm.getPlayers().get(1).getDashboard().fillHall(new StudentGroup(5));
        }catch (FullHallException e){
            e.printStackTrace();
        }
        cards.add(card7);
        cards.add(card10);
        gm.getBoard().setCharacterCards(cards);
        gm.getPlayers().get(0).setCoins(10);
        try {
            while (t.getCurrentPlayer()!=0 || t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
            //gm.getTurnManager().setPhase(Phase.ACTION);
            //gm.getTurnManager().setMoveStudentsPhase(true);
            //gm.getTurnManager().setCurrentPlayer(0);
            gm.getPlayers().get(0).setCcActivatedThisTurn(false);
            gm.playCharacterCard(0, 0,sf,st);
        } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
            e.printStackTrace();
        }
        assertEquals(6, card7.getStudentsOnCard().getQuantityColour(Colour.BLUE));
        assertEquals(tot11+1, gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(Colour.RED));
        assertEquals(4, card7.getStudentsOnCard().getQuantityColour(Colour.RED));
        assertEquals(tot12-1, gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(Colour.BLUE));

        gm.getPlayers().get(1).setCoins(10);
        int tot21entranceblue = gm.getPlayers().get(1).getDashboard().getEntrance().getQuantityColour(Colour.BLUE);
        int tot22hallred = gm.getPlayers().get(1).getDashboard().getHall().getQuantityColour(Colour.RED);
        int tot21entrancered = gm.getPlayers().get(1).getDashboard().getEntrance().getQuantityColour(Colour.RED);
        int tot22hallblue = gm.getPlayers().get(1).getDashboard().getHall().getQuantityColour(Colour.RED);
        try {
            while (t.getCurrentPlayer()!=1 || t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
            //gm.getTurnManager().setPhase(Phase.ACTION);
            //gm.getTurnManager().setMoveStudentsPhase(true);
            //gm.getTurnManager().setCurrentPlayer(0);
            gm.getPlayers().get(1).setCcActivatedThisTurn(false);
            gm.playCharacterCard(1, 1,sf,st);
        } catch (IllegalArgumentException | NotEnoughCoinsException | WrongPhaseException | AlreadyPlayedException | WrongTurnException e) {
            e.printStackTrace();
        }
        assertEquals(tot22hallred + 1, gm.getPlayers().get(1).getDashboard().getHall().getQuantityColour(Colour.RED));
        assertEquals(tot21entranceblue+1, gm.getPlayers().get(1).getDashboard().getEntrance().getQuantityColour(Colour.BLUE));
        assertEquals(tot22hallblue-1, gm.getPlayers().get(1).getDashboard().getHall().getQuantityColour(Colour.BLUE));
        assertEquals(tot21entrancered-1, gm.getPlayers().get(1).getDashboard().getEntrance().getQuantityColour(Colour.RED));

        //To test AlreadyPlayedException
        CharacterCard card17 = new CharacterCard(CharacterCardInfo.CARD7);
        CharacterCard card110 = new CharacterCard(CharacterCardInfo.CARD10);
        ArrayList<CharacterCard> cards2 = new ArrayList<>();
        cards2.add(card17);
        cards2.add(card110);
        card17.setStudentsOnCard(soc);
        card110.setStudentsOnCard(soc);
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getPlayers().get(1).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards2);
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards2.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.playCharacterCard(0, i, sf, st);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }


        //To test NotEnoughCoinsException
        CharacterCard card27 = new CharacterCard(CharacterCardInfo.CARD7);
        CharacterCard card210 = new CharacterCard(CharacterCardInfo.CARD10);
        ArrayList<CharacterCard> cards3 = new ArrayList<>();
        cards3.add(card27);
        cards3.add(card210);
        card27.setStudentsOnCard(soc);
        card210.setStudentsOnCard(soc);
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getPlayers().get(1).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards3);
        try {
            gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(5));
        }catch (FullHallException e){
            e.printStackTrace();
        }
        gm.getPlayers().get(0).setCoins(0);
        gm.getBoard().setCharacterCards(cards3);
        for(int i = 0; i < cards3.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, sf,st);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }


        //To test WrongPhaseException
        CharacterCard card37 = new CharacterCard(CharacterCardInfo.CARD7);
        CharacterCard card310 = new CharacterCard(CharacterCardInfo.CARD10);
        ArrayList<CharacterCard> cards4 = new ArrayList<>();
        cards4.add(card37);
        cards4.add(card310);
        card37.setStudentsOnCard(soc);
        card310.setStudentsOnCard(soc);
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getPlayers().get(1).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards4);
        try {
            gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(5));
        }catch (FullHallException e){
            e.printStackTrace();
        }
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards4.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.PLANNING);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(0, i, sf,st);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }

        //To test IllegalArgumentException
        CharacterCard card47 = new CharacterCard(CharacterCardInfo.CARD7);
        CharacterCard card410 = new CharacterCard(CharacterCardInfo.CARD10);
        ArrayList<CharacterCard> cards5 = new ArrayList<>();
        cards5.add(card47);
        cards5.add(card410);
        card47.setStudentsOnCard(soc);
        card410.setStudentsOnCard(soc);
        gm.getPlayers().get(0).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getPlayers().get(1).getDashboard().fillEntrance(new StudentGroup(5));
        gm.getBoard().setCharacterCards(cards5);
        try {
            gm.getPlayers().get(0).getDashboard().fillHall(new StudentGroup(5));
        }catch (FullHallException e){
            e.printStackTrace();
        }
        gm.getPlayers().get(0).setCoins(8);
        for(int i = 0; i < cards5.size(); i++) {
            try {
                gm.getTurnManager().setPhase(Phase.ACTION);
                gm.getTurnManager().setMoveStudentsPhase(true);
                gm.getTurnManager().setCurrentPlayer(0);
                gm.getPlayers().get(0).setCcActivatedThisTurn(false);
                gm.playCharacterCard(2, i,  sf,st);
            } catch (IllegalArgumentException | WrongPhaseException | NotEnoughCoinsException | AlreadyPlayedException | WrongTurnException e) {
                e.printStackTrace();
            }
        }
    }

}