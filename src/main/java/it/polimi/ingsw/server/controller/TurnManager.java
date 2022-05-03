package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.TurnStatus;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;
import java.util.Random;

public class TurnManager {
    private ArrayList<Integer> planningOrder;
    private ArrayList<Integer> actionOrder;
    private int currentPlayer;
    private Phase phase;
    private int numOfMovedStudents;
    private boolean moveStudentsPhase;
    private boolean motherNaturePhase;
    private boolean cloudSelectionPhase;


    /**
     * TurnManager constructor, starts with the planning phase, random player starts
     * @param numPlayers number of players
     */
    public TurnManager (int numPlayers) {
        planningOrder = new ArrayList<>();
        phase = Phase.PLANNING;
        currentPlayer = 0;
        int playerIndex = new Random().nextInt(numPlayers);
        planningOrder.add(playerIndex);
        for (int i=1;i<numPlayers;i++) {
            if(playerIndex==numPlayers-1) playerIndex = 0;
            else playerIndex++;
            planningOrder.add(playerIndex);
        }
    }

    /**
     * Sets the next phase of the game, so that the game manager checks if a command is activated on the correct phase of the game
     * @param players the list of players
     */
    public void nextPhase(Board b, ArrayList<Player> players) {
        if (phase==Phase.PLANNING) {
            if(currentPlayer==players.size()-1) {
                toActionPhase(players);
            } else {
                currentPlayer++;
            }
        } else {
            if(moveStudentsPhase) {
                if(numOfMovedStudents<players.size()) {
                    numOfMovedStudents++;
                } else {
                    moveStudentsPhase = false;
                    motherNaturePhase = true;
                }
            } else if(motherNaturePhase) {
                cloudSelectionPhase = true;
                motherNaturePhase = false;
                EndOfGameChecker.instance().updateEOG(b,players);
            } else {
                if(currentPlayer==players.size()-1) {
                    EndOfGameChecker.instance().updateEOGLastTurn(b,players);
                    toPlanningPhase(players);
                } else {
                    currentPlayer++;
                    numOfMovedStudents = 0;
                    cloudSelectionPhase = false;
                    moveStudentsPhase = true;
                }
            }
        }
    }

    /**
     * sets the order of players to the planning phase
     * @param players list of players
     */
    private void toPlanningPhase(ArrayList<Player> players) {
        planningOrder = new ArrayList<>();
        phase = Phase.PLANNING;
        currentPlayer = 0;
        int playerIndex = actionOrder.get(0);
        for (int i=1;i<players.size();i++) {
            if(playerIndex==players.size()-1) playerIndex = 0;
            else playerIndex++;
            planningOrder.add(playerIndex);
        }
        for (Player p: players) {
            p.setCcActivatedThisTurn(false);
        }
    }

    /**
     * sets the order of players to the action phase
     * @param players list of players
     */
    private void toActionPhase(ArrayList<Player> players) {
        actionOrder = new ArrayList<>();
        int actionWeight, i, j;
        for(i=0;i<players.size();i++) {
            actionWeight = players.get(i).getLastPlayedCard().getInfo().getTurnWeight();
            j = 0;
            while (j<actionOrder.size() && players.get(actionOrder.get(j)).getLastPlayedCard().getInfo().getTurnWeight()<=actionWeight){
                j++;
            }
            actionOrder.add(j,i);
        }
        phase = Phase.ACTION;
        currentPlayer = 0;
        numOfMovedStudents = 0;
        motherNaturePhase = false;
        cloudSelectionPhase = false;
        moveStudentsPhase = true;
    }

    //getters
    public int getCurrentPlayer() {
        if(phase == Phase.ACTION) return actionOrder.get(currentPlayer);
        else return planningOrder.get(currentPlayer);
    }

    public Phase getPhase() {
        return phase;
    }

    public boolean isMoveStudentsPhase() {
        return moveStudentsPhase;
    }

    public boolean isMotherNaturePhase() {
        return motherNaturePhase;
    }

    public boolean isCloudSelectionPhase() {
        return cloudSelectionPhase;
    }

    public TurnStatus getTurnStatus() {
        TurnStatus t = new TurnStatus();
        t.setPhase(phase.name());
        if(phase==Phase.ACTION) {
            t.setPlayer(actionOrder.get(currentPlayer));
        } else {
            t.setPlayer(planningOrder.get(currentPlayer));
        }
        return t;
    }
}
