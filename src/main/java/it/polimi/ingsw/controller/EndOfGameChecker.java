package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class EndOfGameChecker {
    private boolean lastTurn;
    private Player winner;
    private boolean endOfGame;
    private static EndOfGameChecker instance;

    private EndOfGameChecker() {
        lastTurn=false;
        winner=null;
        endOfGame=false;
    }

    public static EndOfGameChecker instance() {
        if(instance==null) instance = new EndOfGameChecker();
        return instance;
    }

    public boolean updateEOG(Board b, ArrayList<Player> players) {
        if(lastTurn) {
            endOfGame = true;
            return endOfGame;
        }
        if (b.getIslandManager().getArchipelagos().size()<=3){
            endOfGame = true;
            return true;
        }
        for(Player p: players) {
            if(p.getDashboard().getNumTowers()==0) {
                endOfGame=true;
                return endOfGame;
            }
        }
        return false;
    }

    public void updateLastTurn() {

    }

    //getters & setters
    public boolean isLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isEndOfGame() {
        return endOfGame;
    }

    public void setEndOfGame(boolean endOfGame) {
        this.endOfGame = endOfGame;
    }
}
