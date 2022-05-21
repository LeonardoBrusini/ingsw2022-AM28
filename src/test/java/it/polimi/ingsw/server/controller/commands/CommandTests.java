package it.polimi.ingsw.server.controller.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.controller.Phase;
import it.polimi.ingsw.server.controller.TurnManager;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Cloud;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommandTests {
    private GameManager gm;
    private CurrentStatus status;
    private Command command;
    private CommandList strategy;

    @BeforeEach
    void createGame(){
        gm = new GameManager();
        gm.addPlayer();
        gm.addPlayer();
        gm.newGame(true,2);
        status = gm.getFullCurrentStatus();
        gm.getPlayers().get(0).getDashboard().getEntrance().setStudents(new StudentGroup(1));
        gm.getPlayers().get(0).getDashboard().getEntrance().addStudent(Colour.YELLOW);
        gm.getPlayers().get(0).getDashboard().getEntrance().addStudent(Colour.YELLOW);
    }

    @Test
    void MoveToHallCommand() {
        TurnManager t = gm.getTurnManager();
        while (t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase() || t.getCurrentPlayer()!=0) t.nextPhase(gm.getBoard(),gm.getPlayers());
        strategy = CommandList.MOVETOHALL;
        command = new Command();
        command.setPlayerIndex(0);
        command.setCmd("MOVETOHALL");
        command.setStudentColour("YELLOW");
        StudentGroup entranceBefore = new StudentGroup(gm.getPlayers().get(0).getDashboard().getEntrance());
        StudentGroup hallBefore = new StudentGroup(gm.getPlayers().get(0).getDashboard().getHall());
        strategy.getCmd().resolveCommand(gm,command);
        for (Colour c: Colour.values()) {
            if(c!=Colour.YELLOW) {
                assertEquals(entranceBefore.getQuantityColour(c),gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(c));
                assertEquals(hallBefore.getQuantityColour(c),gm.getPlayers().get(0).getDashboard().getHall().getQuantityColour(c));
            } else {
                assertEquals(entranceBefore.getQuantityColour(c) - 1, gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(c));
                assertEquals(hallBefore.getQuantityColour(c) + 1, gm.getPlayers().get(0).getDashboard().getHall().getQuantityColour(c));
            }
        }
        strategy.getCmd().resolveCommand(gm,command);
        strategy.getCmd().resolveCommand(gm,command);
        assertEquals(2,gm.getPlayers().get(0).getCoins());

        //test exceptions
        while (t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase() || t.getCurrentPlayer()!=0) t.nextPhase(gm.getBoard(),gm.getPlayers());
        gm.getPlayers().get(0).getDashboard().getHall().setNumStudents(10,Colour.GREEN);
        command.setStudentColour("GREEN");
        assertEquals(StatusCode.FULLHALL,strategy.getCmd().resolveCommand(gm,command));

        gm.getPlayers().get(0).getDashboard().getHall().setNumStudents(0,Colour.GREEN);
        gm.getPlayers().get(0).getDashboard().getEntrance().setNumStudents(0,Colour.GREEN);
        assertEquals(StatusCode.NOSTUDENTS,strategy.getCmd().resolveCommand(gm,command));

        gm.getPlayers().get(0).getDashboard().getEntrance().setNumStudents(1,Colour.GREEN);
        command.setPlayerIndex(3);
        assertEquals(StatusCode.ILLEGALARGUMENT,strategy.getCmd().resolveCommand(gm,command));

        command.setPlayerIndex(0);
        t.setPhase(Phase.PLANNING);
        assertEquals(StatusCode.WRONGPHASE,strategy.getCmd().resolveCommand(gm,command));

        t.setPhase(Phase.ACTION);
        while (t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase() || t.getCurrentPlayer()==0) t.nextPhase(gm.getBoard(),gm.getPlayers());
        assertEquals(StatusCode.WRONGTURN,strategy.getCmd().resolveCommand(gm,command));

        //while (t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase() || t.getCurrentPlayer()!=0) t.nextPhase(gm.getBoard(),gm.getPlayers());
        strategy.getCmd().getUpdatedStatus(gm,command);
        /*CurrentStatus cs = new CurrentStatus();
        GameStatus gs = new GameStatus();
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        PlayerStatus ps0 = new PlayerStatus();
        gs.setProfessors(gm.getBoard().getProfessorGroup().getStatus());
        ps0.setIndex(0);
        ps0.setCoins(gm.getPlayers().get(0).getCoins());
        ps0.setStudentsOnEntrance(gm.getPlayers().get(0).getDashboard().getEntrance().getStatus());
        ps0.setStudentsOnHall(gm.getPlayers().get(0).getDashboard().getHall().getStatus());
        ps.add(ps0);
        gs.setPlayers(ps);
        cs.setGame(gs);
        status = new Gson().fromJson(strategy.getCmd().getUpdatedStatus(gm,command),CurrentStatus.class);
        compareStatus(cs);*/
    }

    @Test
    void MoveToIslandCommand() {
        TurnManager t = gm.getTurnManager();
        strategy = CommandList.MOVETOISLAND;
        command = new Command();
        command.setPlayerIndex(0);
        command.setCmd("MOVETOISLAND");
        command.setStudentColour("YELLOW");
        command.setIndex(1);
        while (t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase() || t.getCurrentPlayer()!=0) t.nextPhase(gm.getBoard(),gm.getPlayers());
        System.out.println(gm.getTurnManager().getCurrentPlayer());
        command.setPlayerIndex(0);
        t.setPhase(Phase.PLANNING);
        System.out.println(gm.getTurnManager().getPhase().toString());
        System.out.println(gm.getTurnManager().isMoveStudentsPhase());
        System.out.println(command.getPlayerIndex());
        System.out.println(command.getIndex());
        System.out.println(Colour.valueOf(command.getStudentColour()));
        System.out.println(CommandList.valueOf(command.getCmd()));
        assertEquals(StatusCode.WRONGPHASE,strategy.getCmd().resolveCommand(gm,command));

        t.setPhase(Phase.ACTION);
        StudentGroup entranceBefore = new StudentGroup(gm.getPlayers().get(0).getDashboard().getEntrance());
        StudentGroup onIslandBefore = new StudentGroup(gm.getBoard().getIslandManager().getIslandByIndex(1).getStudents());
        strategy.getCmd().resolveCommand(gm,command);
        for (Colour c: Colour.values()) {
            if(c!=Colour.YELLOW) {
                assertEquals(onIslandBefore.getQuantityColour(c),gm.getBoard().getIslandManager().getIslandByIndex(1).getStudents().getQuantityColour(c));
                assertEquals(entranceBefore.getQuantityColour(c),gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(c));
            } else {
                assertEquals(onIslandBefore.getQuantityColour(c)+1,gm.getBoard().getIslandManager().getIslandByIndex(1).getStudents().getQuantityColour(c));
                assertEquals(entranceBefore.getQuantityColour(c) - 1, gm.getPlayers().get(0).getDashboard().getEntrance().getQuantityColour(c));
            }
        }
        strategy.getCmd().getUpdatedStatus(gm,command);
        command.setStudentColour("GREEN");
        gm.getPlayers().get(0).getDashboard().getEntrance().setNumStudents(0,Colour.GREEN);
        assertEquals(StatusCode.NOSTUDENTS,strategy.getCmd().resolveCommand(gm,command));

        gm.getPlayers().get(0).getDashboard().getEntrance().setNumStudents(1,Colour.GREEN);
        command.setPlayerIndex(3);
        assertEquals(StatusCode.ILLEGALARGUMENT,strategy.getCmd().resolveCommand(gm,command));


        command.setPlayerIndex(0);
        t.setPhase(Phase.ACTION);
        while (t.getPhase()!=Phase.ACTION || !t.isMoveStudentsPhase() || t.getCurrentPlayer()==0) t.nextPhase(gm.getBoard(),gm.getPlayers());
        assertEquals(StatusCode.WRONGTURN,strategy.getCmd().resolveCommand(gm,command));
    }

    @Test
    void PlayAssistantCardCommand(){
        TurnManager t = gm.getTurnManager();
        strategy = CommandList.PLAYASSISTANTCARD;
        command = new Command();
        command.setCmd("PLAYASSISTANTCARD");
        command.setPlayerIndex(t.getCurrentPlayer());

        command.setIndex(12);
        assertEquals(StatusCode.ILLEGALARGUMENT,strategy.getCmd().resolveCommand(gm,command));
        command.setIndex(0);

        t.setPhase(Phase.ACTION);
        assertEquals(StatusCode.WRONGPHASE,strategy.getCmd().resolveCommand(gm,command));
        t.setPhase(Phase.PLANNING);

        assertNull(strategy.getCmd().resolveCommand(gm,command));
        strategy.getCmd().getUpdatedStatus(gm,command);
        command.setPlayerIndex(t.getCurrentPlayer());
        assertEquals(StatusCode.ILLEGALARGUMENT,strategy.getCmd().resolveCommand(gm,command));

        while (t.getPhase()!=Phase.ACTION) t.nextPhase(gm.getBoard(),gm.getPlayers());
        while (t.getPhase()!=Phase.PLANNING) t.nextPhase(gm.getBoard(),gm.getPlayers());
        command.setIndex(0);
        command.setPlayerIndex(t.getCurrentPlayer());
        assertEquals(StatusCode.ALREADYPLAYEDAC,strategy.getCmd().resolveCommand(gm,command));
        command.setIndex(5);
        command.setPlayerIndex(t.getCurrentPlayer()==0 ? 1 : 0);
        assertEquals(StatusCode.WRONGTURN,strategy.getCmd().resolveCommand(gm,command));
    }

    @Test
    void TakeFromCloudCommand() {
        TurnManager t = gm.getTurnManager();
        strategy = CommandList.TAKEFROMCLOUD;
        command = new Command();
        command.setCmd("TAKEFROMCLOUD");
        command.setIndex(0);
        while (!t.isCloudSelectionPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());

        command.setPlayerIndex(t.getCurrentPlayer()==0 ? 1 : 0);
        assertEquals(StatusCode.WRONGTURN,strategy.getCmd().resolveCommand(gm,command));

        command.setPlayerIndex(t.getCurrentPlayer());
        t.setCloudSelectionPhase(false);
        assertEquals(StatusCode.WRONGPHASE,strategy.getCmd().resolveCommand(gm,command));

        t.setCloudSelectionPhase(true);
        command.setIndex(10);
        assertEquals(StatusCode.ILLEGALARGUMENT,strategy.getCmd().resolveCommand(gm,command));

        command.setIndex(0);
        assertNull(strategy.getCmd().resolveCommand(gm,command));
        strategy.getCmd().getUpdatedStatus(gm,command);

        while (!t.isCloudSelectionPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
        command.setPlayerIndex(t.getCurrentPlayer());
        assertEquals(StatusCode.NOSTUDENTS,strategy.getCmd().resolveCommand(gm,command));

        for (Cloud c: gm.getBoard().getClouds()){
            c.clearStudents();
        }
        assertNull(strategy.getCmd().resolveCommand(gm,command));
    }

    @Test
    void PlayCharacterCard(){
        TurnManager t = gm.getTurnManager();
        strategy = CommandList.PLAYCHARACTERCARD;
        command = new Command();
        command.setCmd("PLAYCHARACTERCARD");
        command.setIndex(0);
        while (!t.isMotherNaturePhase() && !t.isMoveStudentsPhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
        gm.getBoard().getCharacterCards().set(0,new CharacterCard(CharacterCardInfo.CARD4));
        CharacterCard card = gm.getBoard().getCharacterCards().get(0);
        gm.getPlayers().get(0).setLastPlayedCard(gm.getPlayers().get(0).getAssistantCard(0));
        gm.getPlayers().get(1).setLastPlayedCard(gm.getPlayers().get(1).getAssistantCard(0));

        command.setPlayerIndex(t.getCurrentPlayer()==0 ? 1 : 0);
        assertEquals(StatusCode.WRONGTURN,strategy.getCmd().resolveCommand(gm,command));
        command.setPlayerIndex(t.getCurrentPlayer());

        boolean mnpBefore = t.isMotherNaturePhase();
        boolean mspBefore = t.isMoveStudentsPhase();
        t.setMotherNaturePhase(false);
        t.setMoveStudentsPhase(false);
        assertEquals(StatusCode.WRONGPHASE,strategy.getCmd().resolveCommand(gm,command));
        t.setMotherNaturePhase(mnpBefore);
        t.setMoveStudentsPhase(mspBefore);


        command.setIndex(3);
        assertEquals(StatusCode.ILLEGALARGUMENT,strategy.getCmd().resolveCommand(gm,command));
        command.setIndex(0);
        assertNull(strategy.getCmd().resolveCommand(gm,command));
        strategy.getCmd().getUpdatedStatus(gm,command);
        assertEquals(StatusCode.ALREADYPLAYEDCC,strategy.getCmd().resolveCommand(gm,command));

        int player = t.getCurrentPlayer();
        while (t.getPhase()!=Phase.PLANNING) t.nextPhase(gm.getBoard(),gm.getPlayers());
        while (player!=t.getCurrentPlayer() || t.getPhase()!=Phase.ACTION || (!t.isMoveStudentsPhase() && !t.isMotherNaturePhase())) t.nextPhase(gm.getBoard(),gm.getPlayers());
        assertEquals(StatusCode.NOTENOUGHCOINS,strategy.getCmd().resolveCommand(gm,command));

    }

    @Test
    void MoveMotherNature() {
        gm.getPlayers().get(0).setLastPlayedCard(gm.getPlayers().get(0).getAssistantCard(0));
        gm.getPlayers().get(1).setLastPlayedCard(gm.getPlayers().get(1).getAssistantCard(0));
        TurnManager t = gm.getTurnManager();
        strategy = CommandList.MOVEMOTHERNATURE;
        command = new Command();
        command.setCmd("MOVEMOTHERNATURE");
        command.setMotherNatureShifts(1);
        while (!t.isMotherNaturePhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());

        command.setPlayerIndex(t.getCurrentPlayer()==0 ? 1 : 0);
        assertEquals(StatusCode.WRONGTURN,strategy.getCmd().resolveCommand(gm,command));

        command.setPlayerIndex(t.getCurrentPlayer());
        t.setMotherNaturePhase(false);
        assertEquals(StatusCode.WRONGPHASE,strategy.getCmd().resolveCommand(gm,command));

        t.setMotherNaturePhase(true);
        command.setMotherNatureShifts(2);
        assertEquals(StatusCode.ILLEGALARGUMENT,strategy.getCmd().resolveCommand(gm,command));

        command.setMotherNatureShifts(1);
        assertNull(strategy.getCmd().resolveCommand(gm,command));
        strategy.getCmd().getUpdatedStatus(gm,command);

        while (!t.isMotherNaturePhase()) t.nextPhase(gm.getBoard(),gm.getPlayers());
        gm.getBoard().getCharacterCards().set(0,new CharacterCard(CharacterCardInfo.CARD4));
        CharacterCard card = gm.getBoard().getCharacterCards().get(0);
        card.setPlayerThisTurn(gm.getPlayers().get(t.getCurrentPlayer()));
        card.getCardInfo().getEffect().resolveEffect(card);
        command.setPlayerIndex(t.getCurrentPlayer());
        command.setMotherNatureShifts(3);
        assertNull(strategy.getCmd().resolveCommand(gm,command));
    }
}