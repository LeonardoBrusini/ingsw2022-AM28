package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.model.ProfessorGroup;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;

/**
 * Observer class to check when the game ends
 */
public class EndOfGameChecker {
    private boolean lastTurn;
    private int winner;
    private boolean endOfGame;
    private static EndOfGameChecker instance;

    /**
     * EndOfGameChecker private constructor, for the singleton
     */
    private EndOfGameChecker() {
        lastTurn=false;
        winner=-1;
        endOfGame=false;
    }

    /**
     * if there's no instance, creates it
     * @return the instance of the singleton
     */
    public static EndOfGameChecker instance() {
        if(instance==null) instance = new EndOfGameChecker();
        return instance;
    }

    /**
     * checks the conditions for the end of the game
     * @param b board, where it can get information about archipelagos & professors
     * @param players where it searches if a player built all of his towers
     */
    public void updateEOG(Board b, ArrayList<Player> players) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getDashboard().getNumTowers()==0) {
                System.out.println("END OF GAME ALL TOWERS BUILT");
                endOfGame=true;
                winner = i;
                return;
            }
        }
        if (b.getIslandManager().getArchipelagos().size()<=3){
            System.out.println("END OF GAME 3 ARCHIPELAGOS");
            endOfGame = true;
            checkWinner(players, b.getProfessorGroup());
        }
    }

    /**
     * if this was the last turn, ends the game (empty bag, player with no assistant card left)
     * @param b board
     * @param players list of players
     */
    public void updateEOGLastTurn(Board b, ArrayList<Player> players) {
        if(lastTurn) {
            System.out.println("END OF GAME LAST TURN");
            endOfGame = true;
            checkWinner(players, b.getProfessorGroup());
        }
    }

    /**
     * if the game ends, checkWinner() sets the index of the player who won, -1 if there's no winner
     * @param players list of players
     * @param professors professor, needed if two or more players built the same number of towers
     */
    private void checkWinner(ArrayList<Player> players, ProfessorGroup professors) {
        int minTowers = 10;
        int winnerIndex = -1;
        //finds the player who built most towers
        for(int i=0;i<players.size();i++) {
            if(players.get(i).getDashboard().getNumTowers()<minTowers) {
                minTowers = players.get(i).getDashboard().getNumTowers();
                winnerIndex = i;
            } else if(players.get(i).getDashboard().getNumTowers()==minTowers) {
                winnerIndex = -1;
            }
        }

        //if found, sets winner, else, checks for the one with most professors
        if(winnerIndex>-1) {
            winner = winnerIndex;
        } else {
            Tower maxProfessorsTower = null;
            int maxProfessors = -1;
            int numProfessors;
            for (Tower t: Tower.values()) {
                numProfessors = 0;
                for (Colour c: Colour.values()) {
                    if(professors.getTower(c)==t) {
                        numProfessors++;
                    }
                }
                if(numProfessors>maxProfessors) {
                    maxProfessors = numProfessors;
                    maxProfessorsTower = t;
                } else if(numProfessors==maxProfessors) {
                    maxProfessorsTower = null;
                }
            }
            //if found, sets the winner, else, nobody won
            if (maxProfessorsTower!=null) {
                for (int i=0;i< players.size();i++) {
                    if(players.get(i).getTower()==maxProfessorsTower) {
                        winner = i;
                        return;
                    }
                }
            }
        }
    }

    public static void resetInstance() {
        instance = null;
    }

    //getters
    public boolean isLastTurn() {
        return lastTurn;
    }
    public void setLastTurn(boolean lastTurn) {
        System.out.println("THI WILL BE THE LAST TURN!!!");
        this.lastTurn = lastTurn;
    }
    public int getWinner() {
        return winner;
    }
    public boolean isEndOfGame() {
        return endOfGame;
    }
}
