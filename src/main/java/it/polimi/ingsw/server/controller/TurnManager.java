package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.TurnStatus;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import it.polimi.ingsw.server.model.players.AssistantCard;
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

    public ArrayList<Integer> getPlanningOrder() {
        return planningOrder;
    }

    public ArrayList<Integer> getActionOrder() {
        return actionOrder;
    }

    public int getNumOfMovedStudents() {
        return numOfMovedStudents;
    }

    /**
     * TurnManager constructor, starts with the planning phase, random player starts
     * @param numPlayers number of players
     */
    public TurnManager (int numPlayers, Board board) {
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
        board.fillClouds();
    }

    /**
     * Sets the next phase of the game, so that the game manager checks if a command is activated on the correct phase of the game
     * @param players the list of players
     */
    public void nextPhase(Board b, ArrayList<Player> players) {
        if (phase == Phase.PLANNING) {
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
                for (CharacterCard card: b.getCharacterCards()) card.setActivated(false);
                players.get(actionOrder.get(currentPlayer)).setCcActivatedThisTurn(false);
                if(currentPlayer==players.size()-1) {
                    EndOfGameChecker.instance().updateEOGLastTurn(b,players);
                    toPlanningPhase(players, b);
                } else {
                    currentPlayer++;
                    numOfMovedStudents = 0;
                    cloudSelectionPhase = false;
                    moveStudentsPhase = true;
                }
            }
        }
        if(phase==Phase.ACTION) {
            if(!players.get(actionOrder.get(currentPlayer)).isConnected()) nextPhase(b,players);
        } else {
            if(!players.get(planningOrder.get(currentPlayer)).isConnected()) nextPhase(b,players);
        }
    }

    /**
     * sets the order of players to the planning phase
     * @param players list of players
     */
    private void toPlanningPhase(ArrayList<Player> players, Board board) {
        planningOrder = new ArrayList<>();
        phase = Phase.PLANNING;
        currentPlayer = 0;
        board.fillClouds();
        int playerIndex = actionOrder.get(0);
        for (int i=playerIndex;i<players.size();i++) planningOrder.add(i);
        for(int i=0;i<playerIndex;i++) planningOrder.add(i);
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
            if(players.get(i).getLastPlayedCard()==null) actionWeight = 11;
            else actionWeight = players.get(i).getLastPlayedCard().getInfo().getTurnWeight();
            for(j=0;j<actionOrder.size();j++) {
                AssistantCard ac = players.get(actionOrder.get(j)).getLastPlayedCard();
                if(ac!=null) {
                    if(ac.getInfo().getTurnWeight()>actionWeight) break;
                } else {
                    if(11>actionWeight) break;
                }
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

    public void nextPlayer(ArrayList<Player> players, Board b) {
        int p = currentPlayer;
        while (p==currentPlayer) {
            nextPhase(b, players);
        }
    }

    //methods for testing purposes
    public void setPhase(Phase phase){this.phase = phase;}
    public void setCurrentPlayer(int p){currentPlayer = p;}
    public void setActionOrder(ArrayList<Integer> action){this.actionOrder = action;}
    public void setMotherNaturePhase(boolean b){this.motherNaturePhase = b;}
    public void setMoveStudentsPhase(boolean b){this.moveStudentsPhase=b;}

    public void setCloudSelectionPhase(boolean cloudSelectionPhase) {
        this.cloudSelectionPhase = cloudSelectionPhase;
    }

    public void setPlanningOrder(ArrayList<Integer> planningOrder) {
        this.planningOrder = planningOrder;
    }

    public void setNumOfMovedStudents(int numOfMovedStudents) {
        this.numOfMovedStudents = numOfMovedStudents;
    }
}
