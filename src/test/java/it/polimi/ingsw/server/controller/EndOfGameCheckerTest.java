package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.model.ProfessorGroup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EndOfGameCheckerTest {
    private GameManager gameManager;
    private EndOfGameChecker endOfGameChecker;

    @Test
    void updateEOG() {
        gameManager =new GameManager();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true,2);
        ArrayList<Colour> e = gameManager.getBoard().getBag().removeStudents(124);

        EndOfGameChecker.instance().updateEOGLastTurn(gameManager.getBoard(), gameManager.getPlayers());

        assertEquals(true, EndOfGameChecker.instance().isLastTurn());

        ArrayList<Colour> f = gameManager.getBoard().getBag().removeStudents(6);

        EndOfGameChecker.instance().updateEOG(gameManager.getBoard(), gameManager.getPlayers());
        assertEquals(true, EndOfGameChecker.instance().isEndOfGame());

        Boolean b = EndOfGameChecker.instance().isEndOfGame();
        b = EndOfGameChecker.instance().isLastTurn();
        EndOfGameChecker.instance().setLastTurn(true);
        EndOfGameChecker.instance().setLastTurn(false);
        int c = EndOfGameChecker.instance().getWinner();
        EndOfGameChecker.resetInstance();
    }

    /*@Test
    void updateEOGLastTurn() {
        ExpertGameManager expertGameManager = new ExpertGameManager();
        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.newGame(true,2);
        EndOfGameChecker.instance().setLastTurn(true);

        endOfGameChecker.updateEOGLastTurn(expertGameManager.getBoard(),expertGameManager.getPlayers());

        assertTrue(endOfGameChecker.isEndOfGame());
    }*/

    @Test
    void checkWinner(){
        gameManager =new GameManager();
        gameManager.addPlayer();
        gameManager.addPlayer();
        gameManager.newGame(true,2);

        ProfessorGroup n = new ProfessorGroup();
        for(Colour c: Colour.values()){
            n.setTower(c, Tower.WHITE);
        }


    }
}