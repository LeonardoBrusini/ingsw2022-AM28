package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurnManagerTest {
    private TurnManager turnManager;
    private ExpertGameManager expertGameManager = new ExpertGameManager();;
    @Test
    void nextPhase(){
        ArrayList<Integer> actionorder = new ArrayList<Integer>();
        actionorder.add(2);
        actionorder.add(0);
        actionorder.add(1);
        ArrayList<Integer> planningorder = new ArrayList<Integer>();
        planningorder.add(1);
        planningorder.add(2);
        planningorder.add(0);


        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.newGame(true,3 ) ;
        turnManager = expertGameManager.getTurnManager();

        turnManager.setPhase(Phase.PLANNING);
        turnManager.nextPhase(expertGameManager.getBoard(), expertGameManager.getPlayers());
        turnManager.setPlanningOrder( planningorder);
        turnManager.setPlanningOrder(actionorder);

        assertEquals(Phase.PLANNING, turnManager.getPhase());



        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.newGame(true,3 ) ;
        turnManager = expertGameManager.getTurnManager();

        turnManager.setMotherNaturePhase(true);
        turnManager.setMoveStudentsPhase(true);
        turnManager.setPhase(Phase.ACTION);
        turnManager.setNumOfMovedStudents(2);
        turnManager.setPlanningOrder(planningorder);
        turnManager.setPlanningOrder(actionorder);

        assertEquals(2,turnManager.getNumOfMovedStudents());



        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.newGame(true,3 ) ;
        turnManager = expertGameManager.getTurnManager();

        turnManager.setMotherNaturePhase(true);
        turnManager.setMoveStudentsPhase(true);
        turnManager.setPhase(Phase.ACTION);
        turnManager.setNumOfMovedStudents(3);
        turnManager.setPlanningOrder( planningorder);
        turnManager.setPlanningOrder(actionorder);

        assertEquals(true,turnManager.isMotherNaturePhase());
        assertEquals(true,turnManager.isMotherNaturePhase());


        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.newGame(true,3 ) ;
        turnManager = expertGameManager.getTurnManager();

        turnManager.setMotherNaturePhase(false);
        turnManager.setMoveStudentsPhase(false);
        turnManager.setPhase(Phase.ACTION);
        turnManager.setCurrentPlayer(2);
        turnManager.setNumOfMovedStudents(3);
        turnManager.setPlanningOrder( planningorder);
        turnManager.setPlanningOrder(actionorder);


        assertEquals(false,turnManager.isCloudSelectionPhase());
        assertEquals(false,turnManager.isMoveStudentsPhase());
        assertEquals(false,turnManager.isMotherNaturePhase());
        //assertEquals(1,turnManager.getCurrentPlayer());
        assertEquals(3,turnManager.getNumOfMovedStudents());

    }


    @Test
    void nextPlayer() {
    }
}