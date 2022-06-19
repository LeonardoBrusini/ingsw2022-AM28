package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.server.exceptions.FullHallException;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.IslandManager;
import it.polimi.ingsw.server.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EffectsStatusTest {
    private CharacterCard c;
    private GameManager gameManager;
    private CurrentStatus status;

    @BeforeEach
    void createGame(){
        gameManager = new GameManager();
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
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setAddedShifts(true);
        ps.add(ps0);
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs.add(ccs0);
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        compareStatus(cs);
    }

   @Test
    void CTESStatus() {
        setCard(CharacterCardInfo.CARD7);
       //card parameters && effect activation
       StudentGroup st = new StudentGroup();
       st.addStudent(Colour.YELLOW);
       st.addStudent(Colour.BLUE);
       st.addStudent(Colour.RED);

       c.setSelectedStudentsTo(st);
       StudentGroup sh = new StudentGroup(5);

       StudentGroup sf = new StudentGroup();
       sf.addStudent(Colour.RED);
       sf.addStudent(Colour.GREEN);
       sf.addStudent(Colour.PINK);

       c.setStudentsOnCard(sf);
       c.setSelectedStudentsFrom(sf);

       gameManager.getPlayers().get(0).fillDashboardEntrance(sh);
       c.setSelectedStudentsTo(st);
       try {
           gameManager.getPlayers().get(0).playCard(0);
       } catch (AlreadyPlayedException e) {
           throw new RuntimeException(e);
       }catch (IllegalArgumentException h){
           throw new RuntimeException(h);
       }
       c.setPlayerThisTurn(gameManager.getPlayers().get(0));
       c.getCardInfo().getEffect().resolveEffect(c);
       status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
       CurrentStatus cs = new CurrentStatus();
       //creation of the expected current status
       GameStatus gs = new GameStatus();
       ArrayList<PlayerStatus> ps = new ArrayList<>();
       PlayerStatus ps0 = new PlayerStatus();
       ps0.setIndex(0);
       ps0.setCoins(1);
       int[] s = new int[5];
       for(Colour colour: Colour.values())
           s[colour.ordinal()] = gameManager.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(colour);
       ps0.setStudentsOnEntrance(s);
       ps.add(ps0);
       gs.setPlayers(ps);
       ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
       CharacterCardStatus ccs0 = new CharacterCardStatus();
       ccs0.setIndex(0);
       ccs0.setCoinOnIt(true);
       int[] s1 = new int[5];
       for(Colour colour: Colour.values())
           s1[colour.ordinal()] = st.getQuantityColour(colour);
       ccs0.setStudents(s1);
       ccs.add(ccs0);
       gs.setCharacterCards(ccs);
       cs.setGame(gs);
       compareStatus(cs);
    }


    @Test
    void CIStatus() {
        setCard(CharacterCardInfo.CARD3);
        IslandManager im = gameManager.getBoard().getIslandManager();
        int before = 3;
        int after = 6;
        im.getArchipelagoByIslandIndex(3).setPresenceMotherNature(false);
        im.getArchipelagoByIslandIndex(6).setPresenceMotherNature(true);
        StudentGroup si = new StudentGroup();
        si.addStudent(Colour.YELLOW);
        im.getIslandByIndex(6).setStudents(si);
        gameManager.getBoard().getProfessorGroup().setTower(Colour.YELLOW,gameManager.getPlayers().get(0).getTower());
        for(Archipelago a: im.getArchipelagos()){
            ArchipelagoStatus as = new ArchipelagoStatus();
                as.setIslands(a.getFullIslandsStatus());
        }
        //card parameters && effect activation
        c.setSelectedIsland(im.getIslandByIndex(6));
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        c.setBoard(gameManager.getBoard());
        c.setGameManager(gameManager);
        c.getCardInfo().getEffect().resolveEffect(c);
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        CurrentStatus cs = new CurrentStatus();
        PlayerStatus ps0 = new PlayerStatus();
        PlayerStatus ps1 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setCoins(1);
        ps0.setNumTowers(7);
        ps1.setIndex(1);
        ps1.setCoins(1);
        ps1.setNumTowers(8);
        ps.add(ps0);
        ps.add(ps1);
        ArrayList<ArchipelagoStatus> as = new ArrayList<>();
        int j = 0;
        for(Archipelago a: im.getArchipelagos()){
            ArrayList<IslandStatus> is = a.getFullIslandsStatus();
            ArchipelagoStatus asTemp = new ArchipelagoStatus();
            asTemp.setIslands(is);
            asTemp.setIndex(j);
            asTemp.setNoEntryTiles(a.getNoEntryTiles());
            as.add(asTemp);
            j++;
        }
        gs.setArchipelagos(as);
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs.add(ccs0);
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        //creation of the expected current status
        compareStatus(cs);
    }


    @Test
    void DCStatus() {
        setCard(CharacterCardInfo.CARD2);
        //card parameters && effect activation
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        c.getCardInfo().getEffect().resolveEffect(c);
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setCoins(1);
        ps.add(ps0);
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs.add(ccs0);
        //creation of the expected current status
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        compareStatus(cs);
    }


    @Test
    void ETHSStatus() {
        setCard(CharacterCardInfo.CARD10);
        //card parameters && effect activation
        StudentGroup st = new StudentGroup();
        st.addStudent(Colour.YELLOW);
        st.addStudent(Colour.BLUE);


        c.setSelectedStudentsTo(st);
        StudentGroup sh = new StudentGroup(5);

        StudentGroup sf = new StudentGroup();
        sf.addStudent(Colour.RED);
        sf.addStudent(Colour.GREEN);

        c.setStudentsOnCard(sf);
        c.setSelectedStudentsFrom(sf);
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        gameManager.getPlayers().get(0).fillDashboardEntrance(sh);
        try {
            gameManager.getPlayers().get(0).fillHall(gameManager.getBoard(),sh);
        }catch (FullHallException e){
            throw new RuntimeException();
        }
        c.setSelectedStudentsTo(st);
        c.setGameManager(gameManager);
        c.setBoard(gameManager.getBoard());
        assertEquals(6, gameManager.getPlayers().get(0).getCoins()); //The player gets coins when it adds students to the Hall
        c.getCardInfo().getEffect().resolveEffect(c);

        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setCoins(8); //The player gets coins when it adds students to the Hall (2 added in EntranceToHallSwitchEffect
        int[] s = new int[5];
        for(Colour colour: Colour.values())
            s[colour.ordinal()] = gameManager.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(colour);
        ps0.setStudentsOnEntrance(s);
        int[] s1 = new int[5];
        for(Colour colour: Colour.values())
            s1[colour.ordinal()] = gameManager.getPlayers().get(0).getDashboard().getHall().getQuantityColour(colour);
        ps0.setStudentsOnHall(s1);
        gs.setProfessors(gameManager.getBoard().getProfessorGroup().getStatus());
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs.add(ccs0);
        gs.setCharacterCards(ccs);
        ps.add(ps0);
        gs.setPlayers(ps);
        cs.setGame(gs);
        compareStatus(cs);
    }


    @Test
    void NETStatus() {
        setCard(CharacterCardInfo.CARD5);
        //card parameters && effect activation
        c.setSelectedIsland(gameManager.getBoard().getIslandManager().getIslandByIndex(4));
        c.setBoard(gameManager.getBoard());
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        c.setNoEntryTiles(4); //With this card at the beginning are added 4 NoEntryTiles on it
        assertEquals(4, c.getNoEntryTiles());
        assertEquals(0, gameManager.getBoard().getIslandManager().getArchipelagoByIslandIndex(c.getSelectedIsland().getIslandIndex()).getNoEntryTiles());
        c.getCardInfo().getEffect().resolveEffect(c);
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        assertEquals(3, c.getNoEntryTiles());
        assertEquals(1, c.getBoard().getIslandManager().getArchipelagoByIslandIndex(4).getNoEntryTiles());
        ArrayList<ArchipelagoStatus> arc = new ArrayList<>();
        CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArchipelagoStatus arcstatus = new ArchipelagoStatus();
        arcstatus.setNoEntryTiles(1);
        arcstatus.setIndex(gameManager.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(4));
        arc.add(arcstatus);
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setCoins(1);
        ps.add(ps0);
        gs.setPlayers(ps);
        gs.setArchipelagos(arc);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs0.setNoEntryTiles(c.getNoEntryTiles());
        ccs.add(ccs0);
        //creation of the expected current status
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        compareStatus(cs);
    }


    @Test
    void RFHStatus() {
        setCard(CharacterCardInfo.CARD12);
        StudentGroup sh = new StudentGroup();
        sh.addStudent(Colour.YELLOW);
        sh.addStudent(Colour.YELLOW);
        sh.addStudent(Colour.YELLOW);
        sh.addStudent(Colour.YELLOW);
        for(Player p: gameManager.getPlayers()) {
            try{
                p.fillHall(gameManager.getBoard(),sh);
            }catch (FullHallException e){
                throw new RuntimeException(e);
            }
        }
        int[] hall = new int[5];
        for(Colour colour: Colour.values())
            if(colour != Colour.YELLOW)
                hall[colour.ordinal()] = 0;
        hall[Colour.YELLOW.ordinal()] = 1;
        c.setSelectedColour(Colour.YELLOW);
        c.setGameManager(gameManager);
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        c.getCardInfo().getEffect().resolveEffect(c);
        //card parameters && effect activation
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        for(Player p: gameManager.getPlayers())
            assertEquals(1, p.getDashboard().getHall().getQuantityColour(Colour.YELLOW));
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setCoins(2); //adding students on hall as before, the player earns a coin
        ps0.setStudentsOnHall(hall);
        ps.add(ps0);
        PlayerStatus ps1 = new PlayerStatus();
        ps1.setIndex(1);
        ps1.setCoins(2); //adding students on hall as before, the player earns a coin
        ps1.setStudentsOnHall(hall);
        ps.add(ps1);
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs.add(ccs0);
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        compareStatus(cs);
    }


    @Test
    void STHStatus() {
        setCard(CharacterCardInfo.CARD11);
        StudentGroup sc = new StudentGroup(2);
        c.setStudentsOnCard(sc);
        c.setSelectedColour(Colour.PINK);
        int[] hall = new int[5];
        hall[Colour.PINK.ordinal()] = 1;
        for(Colour colour: Colour.values())
            if(colour != Colour.PINK)
                hall[colour.ordinal()] = 0;
        //card parameters && effect activation
        c.setBoard(gameManager.getBoard());
        c.setGameManager(gameManager);
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        c.getCardInfo().getEffect().resolveEffect(c);
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setCoins(1);
        ps0.setStudentsOnHall(hall);
        ps.add(ps0);
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs0.setStudents(c.getStudentsOnCard().getStatus());
        assertEquals(10, c.getStudentsOnCard().getTotalStudents());
        ccs.add(ccs0);
        gs.setCharacterCards(ccs);
        gs.setProfessors(gameManager.getBoard().getProfessorGroup().getStatus());
        cs.setGame(gs);
        compareStatus(cs);
    }


    @Test
    void STIStatus() {
        setCard(CharacterCardInfo.CARD1);
        c.setSelectedColour(Colour.BLUE);
        c.setSelectedIsland(gameManager.getBoard().getIslandManager().getIslandByIndex(4));
        StudentGroup sc = new StudentGroup(2);
        c.setStudentsOnCard(sc);
        c.setBoard(gameManager.getBoard());
        gameManager.getBoard().getIslandManager().getIslandByIndex(4).setStudents(new StudentGroup());
        int[] island = new int[5];
        for(Colour colour: Colour.values())
            if(colour != Colour.BLUE)
                island[colour.ordinal()] = 0;
        island[Colour.BLUE.ordinal()] = 1;
        //card parameters && effect activation
        c.setGameManager(gameManager);
        c.setPlayerThisTurn(gameManager.getPlayers().get(0));
        c.getCardInfo().getEffect().resolveEffect(c);
        status = c.getCardInfo().getEffect().getUpdatedStatus(c,gameManager);
        CurrentStatus cs = new CurrentStatus();
        //creation of the expected current status
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        ArrayList<IslandStatus> is = new ArrayList<>();
        ArrayList<ArchipelagoStatus> arc = new ArrayList<>();
        ArchipelagoStatus arcstatus = new ArchipelagoStatus();
        IslandStatus isstatus = new IslandStatus();
        isstatus.setIslandIndex(4);
        isstatus.setStudents(island);
        is.add(isstatus);
        arcstatus.setIslands(is);
        arc.add(arcstatus);
        arcstatus.setIndex(gameManager.getBoard().getIslandManager().getArchipelagoIndexByIslandIndex(4));
        PlayerStatus ps0 = new PlayerStatus();
        ps0.setIndex(0);
        ps0.setCoins(1);
        ps.add(ps0);
        gs.setArchipelagos(arc);
        gs.setPlayers(ps);
        ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
        CharacterCardStatus ccs0 = new CharacterCardStatus();
        ccs0.setIndex(0);
        ccs0.setCoinOnIt(true);
        ccs0.setStudents(c.getStudentsOnCard().getStatus());
        assertEquals(10, c.getStudentsOnCard().getTotalStudents());
        ccs.add(ccs0);
        gs.setCharacterCards(ccs);
        cs.setGame(gs);
        compareStatus(cs);
    }

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
            assertEquals(gs.getPlayers().size(),status.getGame().getPlayers().size());
            for(int i=0;i<gs.getPlayers().size();i++) {
                comparePStatus(gs.getPlayers().get(i),i);
            }
        } else {
            assertNull(status.getGame().getPlayers());
        }
        if(gs.getCharacterCards()!=null) {
            assertEquals(gs.getCharacterCards().size(),status.getGame().getCharacterCards().size());
            for(int i=0;i<gs.getCharacterCards().size();i++) {
                compareCCStatus(gs.getCharacterCards().get(i),i);
            }
        } else {
            assertNull(status.getGame().getCharacterCards());
        }
        if(gs.getArchipelagos()!=null) {
            assertEquals(gs.getArchipelagos().size(),status.getGame().getArchipelagos().size());
            for(int i=0;i<gs.getArchipelagos().size();i++) {
                compareAStatus(gs.getArchipelagos().get(i),i);
            }
        } else {
            assertNull(status.getGame().getArchipelagos());
        }
        if(gs.getClouds()!=null) {
            assertEquals(gs.getClouds().size(),status.getGame().getClouds().size());
            for(int i=0;i<gs.getClouds().size();i++) {
                compareCStatus(gs.getClouds().get(i), i);
            }
        } else {
            assertNull(status.getGame().getClouds());
        }
    }
    private void compareCStatus(CloudStatus cloud, int i) {
        assertEquals(cloud.getIndex(),status.getGame().getClouds().get(i).getIndex());
        if(cloud.getStudents()!=null) {
            assertEquals(cloud.getStudents().length,status.getGame().getClouds().get(i).getStudents().length);
            for(int j=0;j<cloud.getStudents().length;j++) {
                assertEquals(cloud.getStudents()[j],status.getGame().getClouds().get(i).getStudents()[j]);
            }
        } else {
            assertNull(status.getGame().getClouds().get(i).getStudents());
        }
    }
    private void compareAStatus(ArchipelagoStatus as, int i) {
        ArchipelagoStatus a = status.getGame().getArchipelagos().get(i);
        assertEquals(as.getIndex(),a.getIndex());
        assertEquals(as.getNoEntryTiles(),a.getNoEntryTiles());
        if(as.getIslands()!=null) {
            assertEquals(as.getIslands().size(),a.getIslands().size());
            for (int j=0;j<as.getIslands().size();j++) {
                compareIStatus(as.getIslands().get(j),a.getIslands().get(j));
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
        CharacterCardStatus c = status.getGame().getCharacterCards().get(i);
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
        PlayerStatus p = status.getGame().getPlayers().get(i);
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
