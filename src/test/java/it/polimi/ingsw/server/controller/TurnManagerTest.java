package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TurnManagerTest {
    private TurnManager turnManager;
    private GameManager gameManager = new GameManager();;

    @Test
    void nextPhase() {
        ArrayList<Integer> actionorder = new ArrayList<Integer>();
        actionorder.add(2);
        actionorder.add(0);
        actionorder.add(1);
        ArrayList<Integer> planningorder = new ArrayList<Integer>();
        planningorder.add(1);
        planningorder.add(2);
        planningorder.add(0);


        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true, 3);
        turnManager = gameManager.getTurnManager();

        turnManager.setPhase(Phase.PLANNING);;
        turnManager.setPlanningOrder(planningorder);
        turnManager.setActionOrder(actionorder);
        turnManager.nextPhase(gameManager.getBoard(), gameManager.getPlayers());

        assertEquals(Phase.PLANNING, turnManager.getPhase());


        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true, 3);
        turnManager = gameManager.getTurnManager();

        turnManager.setMotherNaturePhase(true);
        turnManager.setMoveStudentsPhase(true);
        turnManager.setPhase(Phase.ACTION);
        turnManager.setNumOfMovedStudents(2);
        turnManager.setPlanningOrder(planningorder);
        turnManager.setActionOrder(actionorder);
        turnManager.nextPhase(gameManager.getBoard(), gameManager.getPlayers());


        assertEquals(3, turnManager.getNumOfMovedStudents());


        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true, 3);
        turnManager = gameManager.getTurnManager();

        turnManager.setMotherNaturePhase(true);
        turnManager.setMoveStudentsPhase(true);
        turnManager.setPhase(Phase.ACTION);
        turnManager.setNumOfMovedStudents(3);
        turnManager.setPlanningOrder(planningorder);
        turnManager.setActionOrder(actionorder);
        turnManager.nextPhase(gameManager.getBoard(), gameManager.getPlayers());


        assertEquals(true, turnManager.isMotherNaturePhase());
        assertEquals(true, turnManager.isMotherNaturePhase());


        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true, 3);
        turnManager = gameManager.getTurnManager();

        turnManager.setMotherNaturePhase(false);
        turnManager.setMoveStudentsPhase(false);
        turnManager.setPhase(Phase.ACTION);
        turnManager.setCurrentPlayer(2);
        turnManager.setNumOfMovedStudents(3);
        turnManager.setPlanningOrder(planningorder);
        turnManager.setActionOrder(actionorder);
        turnManager.nextPhase(gameManager.getBoard(), gameManager.getPlayers());



        assertEquals(false, turnManager.isCloudSelectionPhase());
        assertEquals(false, turnManager.isMoveStudentsPhase());
        assertEquals(false, turnManager.isMotherNaturePhase());
        //assertEquals(1,turnManager.getCurrentPlayer());
        assertEquals(3, turnManager.getNumOfMovedStudents());

        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true, 3);
        turnManager = gameManager.getTurnManager();


        turnManager.setMotherNaturePhase(true);
        turnManager.setMoveStudentsPhase(true);
        turnManager.setPhase(Phase.ACTION);
        turnManager.setNumOfMovedStudents(3);
        turnManager.setPlanningOrder(planningorder);
        turnManager.setActionOrder(actionorder);


        turnManager.nextPhase(gameManager.getBoard(), gameManager.getPlayers());


        boolean[] vet = new boolean[2];
        vet[0] = true;
        vet[1] = false;
        Phase[] phases = Phase.values();
        Board b = gameManager.getBoard();
        ArrayList<Player> players= gameManager.getPlayers();
        for(int m=2;m<=3;m++) {
            for (Phase p : phases) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (int k = 0; k < 2; k++) {
                            actionorder = new ArrayList<Integer>();
                            actionorder.add(2);
                            actionorder.add(0);
                            actionorder.add(1);
                            planningorder = new ArrayList<Integer>();
                            planningorder.add(1);
                            planningorder.add(2);
                            planningorder.add(0);

                            gameManager = new GameManager();

                            gameManager.addPlayer();
                            gameManager.addPlayer();
                            gameManager.addPlayer();
                            gameManager.newGame(true, 3);
                            turnManager = gameManager.getTurnManager();

                            turnManager.setMotherNaturePhase(vet[i]);
                            turnManager.setMoveStudentsPhase(vet[j]);
                            turnManager.setCloudSelectionPhase(vet[k]);
                            turnManager.setPhase(p);
                            turnManager.setNumOfMovedStudents(m);


                            turnManager.setPlanningOrder(planningorder);
                            turnManager.setActionOrder(actionorder);
                            b = gameManager.getBoard();
                            players = gameManager.getPlayers();

                            turnManager.nextPhase(b, players);
                        }
                    }
                }
            }
        }
    }
}