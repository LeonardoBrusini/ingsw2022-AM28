package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.ExpertGameManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EffectsStatusTest {
    private CharacterCard c;
    private ExpertGameManager gameManager;
    private CurrentStatus status;

    @BeforeEach
    void createGame(){
        gameManager = new ExpertGameManager();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true,2);
    }

    void setCard(CharacterCardInfo cci) {
        gameManager.getBoard().getCharacterCards().set(0,new CharacterCard(cci));
        c = gameManager.getBoard().getCharacterCards().get(0);
    }

    @Test
    void AMNSStatus(){
        setCard(CharacterCardInfo.CARD4);
        try {
            gameManager.getPlayers().get(0).playCard(0);
        } catch (AlreadyPlayedException e) {
            throw new RuntimeException(e);
        }
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        c.getCardInfo().getEffect().resolveEffect(c);
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        PlayerStatus[] ps = new PlayerStatus[1];
        ps[0] = new PlayerStatus();
        ps[0].setIndex(0);
        ps[0].setAddedShifts(true);
        gs.setPlayers(ps);
        CharacterCardStatus[] ccs = new CharacterCardStatus[1];
        ccs[0] = new CharacterCardStatus();
        ccs[0].setIndex(0);
        ccs[0].setCoinOnIt(true);
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        compareStatus(cs);
    }

   /* @Test
    void CTESStatus() {
        setCard(CharacterCardInfo.CARD7);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }

    @Test
    void CIStatus() {
        setCard(CharacterCardInfo.CARD3);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }

    @Test
    void DCStatus() {
        setCard(CharacterCardInfo.CARD2);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }

    @Test
    void ETHSStatus() {
        setCard(CharacterCardInfo.CARD10);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }

    @Test
    void NETStatus() {
        setCard(CharacterCardInfo.CARD5);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }

    @Test
    void RFHStatus() {
        setCard(CharacterCardInfo.CARD12);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }

    @Test
    void STHStatus() {
        setCard(CharacterCardInfo.CARD11);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }

    @Test
    void STIStatus() {
        setCard(CharacterCardInfo.CARD1);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        compareStatus(cs);
    }*/

    void compareStatus(CurrentStatus cs) {
        assertEquals(cs.getGameMode(),status.getGameMode());
        assertEquals(cs.getStatus(),status.getStatus());
        assertEquals(cs.getErrorMessage(),status.getErrorMessage());
        assertEquals(cs.getPlayerID(),status.getPlayerID());
        assertEquals(cs.getWinner(),status.getWinner());
        //turn
        if(cs.getTurn()!=null) {
            assertEquals(cs.getTurn().getPhase(),status.getTurn().getPhase());
            assertEquals(cs.getTurn().getPlayer(),status.getTurn().getPlayer());
        } else {
            assertNull(status.getTurn());
        }
        //game
        if(cs.getGame()!=null) {
            compareGameStatus(cs.getGame());
        } else {
            assertNull(status.getGame());
        }
    }
    void compareGameStatus(GameStatus gs) {
        if(gs.getProfessors()!=null) {
            assertEquals(gs.getProfessors().length,status.getGame().getProfessors().length);
            for(int i=0;i<gs.getProfessors().length;i++) {
                assertEquals(gs.getProfessors()[i],status.getGame().getProfessors()[i]);
            }
        } else {
            assertNull(status.getGame().getProfessors());
        }
        if(gs.getPlayers()!=null) {
            assertEquals(gs.getPlayers().length,status.getGame().getPlayers().length);
            for(int i=0;i<gs.getPlayers().length;i++) {
                comparePStatus(gs.getPlayers()[i],i);
            }
        } else {
            assertNull(status.getGame().getPlayers());
        }
        if(gs.getCharacterCards()!=null) {
            assertEquals(gs.getCharacterCards().length,status.getGame().getCharacterCards().length);
            for(int i=0;i<gs.getCharacterCards().length;i++) {
                compareCCStatus(gs.getCharacterCards()[i],i);
            }
        } else {
            assertNull(status.getGame().getCharacterCards());
        }
        if(gs.getArchipelagos()!=null) {
            assertEquals(gs.getArchipelagos().length,status.getGame().getArchipelagos().length);
            for(int i=0;i<gs.getArchipelagos().length;i++) {
                compareAStatus(gs.getArchipelagos()[i],i);
            }
        } else {
            assertNull(status.getGame().getArchipelagos());
        }
        if(gs.getClouds()!=null) {
            assertEquals(gs.getClouds().length,status.getGame().getClouds().length);
            for(int i=0;i<gs.getClouds().length;i++) {
                compareCStatus(gs.getClouds()[i], i);
            }
        } else {
            assertNull(status.getGame().getClouds());
        }
    }
    private void compareCStatus(CloudStatus cloud, int i) {
        assertEquals(cloud.getIndex(),status.getGame().getClouds()[i].getIndex());
        if(cloud.getStudents()!=null) {
            assertEquals(cloud.getStudents().length,status.getGame().getClouds()[i].getStudents().length);
            for(int j=0;j<cloud.getStudents().length;j++) {
                assertEquals(cloud.getStudents()[j],status.getGame().getClouds()[i].getStudents()[j]);
            }
        } else {
            assertNull(status.getGame().getClouds()[i].getStudents());
        }
    }
    private void compareAStatus(ArchipelagoStatus as, int i) {
        ArchipelagoStatus a = status.getGame().getArchipelagos()[i];
        assertEquals(as.getIndex(),a.getIndex());
        assertEquals(as.getNoEntryTiles(),a.getNoEntryTiles());
        if(as.getIslands()!=null) {
            assertEquals(as.getIslands().length,a.getIslands().length);
            for (int j=0;j<as.getIslands().length;j++) {
                compareIStatus(as.getIslands()[j],a.getIslands()[j]);
            }
        } else {
            assertNull(as.getIslands());
        }
    }
    private void compareIStatus(IslandStatus is, IslandStatus i) {
        assertEquals(is.getIslandIndex(),i.getIslandIndex());
        assertEquals(is.getTowerColour(),i.getTowerColour());
        if(is.getStudents()!=null) {
            assertEquals(is.getStudents().length,i.getStudents().length);
            for (int j=0;j<is.getStudents().length;j++) {
                assertEquals(is.getStudents()[j],i.getStudents()[j]);
            }
        } else {
            assertNull(is.getStudents());
        }
    }
    private void compareCCStatus(CharacterCardStatus ccs, int i) {
        CharacterCardStatus c = status.getGame().getCharacterCards()[i];
        assertEquals(ccs.getIndex(),c.getIndex());
        assertEquals(ccs.getFileName(),c.getFileName());
        assertEquals(ccs.getNoEntryTiles(),c.getNoEntryTiles());
        if(ccs.getStudents()!=null) {
            assertEquals(ccs.getStudents().length,c.getStudents().length);
            for (int j=0;j<ccs.getStudents().length;j++) {
                assertEquals(ccs.getStudents()[j],c.getStudents()[j]);
            }
        } else {
            assertNull(c.getStudents());
        }
    }
    private void comparePStatus(PlayerStatus player, int i) {
        PlayerStatus p = status.getGame().getPlayers()[i];
        assertEquals(player.getIndex(),p.getIndex());
        assertEquals(player.getCoins(),p.getCoins());
        assertEquals(player.getAddedShifts(),p.getAddedShifts());
        assertEquals(player.getNumTowers(),p.getNumTowers());
        assertEquals(player.getLastAssistantCardPlayed(),p.getLastAssistantCardPlayed());
        assertEquals(player.getTowerColour(),p.getTowerColour());
        if(player.getAssistantCards()!=null) {
            assertEquals(player.getAssistantCards().length,p.getAssistantCards().length);
            for(int j=0;j<player.getAssistantCards().length;j++) {
                assertEquals(player.getAssistantCards()[j],p.getAssistantCards()[j]);
            }
        } else {
            assertNull(p.getAssistantCards());
        }
        if(player.getStudentsOnEntrance()!=null) {
            assertEquals(player.getStudentsOnEntrance().length,p.getStudentsOnEntrance().length);
            for(int j=0;j<player.getStudentsOnEntrance().length;j++) {
                assertEquals(player.getStudentsOnEntrance()[j],p.getStudentsOnEntrance()[j]);
            }
        } else {
            assertNull(p.getStudentsOnEntrance());
        }
        if(player.getStudentsOnHall()!=null) {
            assertEquals(player.getStudentsOnHall().length,p.getStudentsOnHall().length);
            for(int j=0;j<player.getStudentsOnHall().length;j++) {
                assertEquals(player.getStudentsOnHall()[j],p.getStudentsOnHall()[j]);
            }
        } else {
            assertNull(p.getStudentsOnHall());
        }
    }
}
