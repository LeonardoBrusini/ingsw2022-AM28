package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.enumerations.Colour;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EndOfGameCheckerTest {
    private ExpertGameManager expertGameManager;

    @Test
    void updateEOG() {
        expertGameManager=new ExpertGameManager();
        expertGameManager.addPlayer();
        expertGameManager.addPlayer();
        expertGameManager.newGame(true,2);
        ArrayList<Colour> e = expertGameManager.getBoard().getBag().removeStudents(124);

        EndOfGameChecker.instance().updateEOGLastTurn(expertGameManager.getBoard(),expertGameManager.getPlayers());

        assertEquals(true, EndOfGameChecker.instance().isLastTurn());

        ArrayList<Colour> f = expertGameManager.getBoard().getBag().removeStudents(6);

        EndOfGameChecker.instance().updateEOG(expertGameManager.getBoard(),expertGameManager.getPlayers());
        assertEquals(true, EndOfGameChecker.instance().isEndOfGame());


    }

    @Test
    void updateEOGLastTurn() {
    }
}