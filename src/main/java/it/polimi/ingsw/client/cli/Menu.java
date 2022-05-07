package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.CurrentStatus;
import it.polimi.ingsw.network.GameStatus;
import it.polimi.ingsw.network.TurnStatus;

public class Menu {
    private CurrentStatus currentStatus;
    private Command command;

    public Menu() {
        currentStatus = new CurrentStatus();
    }

    public synchronized String generateCommand(String userInput) {



        return userInput;
    }

    public synchronized void updateStatus(CurrentStatus c) {
        if(c.getGameMode()!=null) currentStatus.setGameMode(c.getGameMode());
        if(c.getPlayerID()!=null) currentStatus.setPlayerID(c.getPlayerID());
        if(c.getWinner()!=null) currentStatus.setWinner(c.getWinner());
        if(c.getTurn()!=null) {
            if(currentStatus.getTurn()==null) currentStatus.setTurn(new TurnStatus());
            if(c.getTurn().getPlayer()!=null) currentStatus.getTurn().setPlayer(c.getTurn().getPlayer());
            if(c.getTurn().getPhase()!=null) currentStatus.getTurn().setPhase(c.getTurn().getPhase());
        }
        if(c.getGame()!=null) updateGameStatus(c.getGame());
    }

    public synchronized void printResult(String line) {

        System.out.println(line);
    }

    private void updateGameStatus(GameStatus g) {
        if(currentStatus.getGame()==null) currentStatus.setGame(new GameStatus());
        GameStatus gameStatus = currentStatus.getGame();
        if(g.getMotherNatureIndex()!=null) gameStatus.setMotherNatureIndex(g.getMotherNatureIndex());
        if(g.getProfessors()!=null) {
            if(gameStatus.getProfessors()==null) gameStatus.setProfessors(new int[g.getProfessors().length]);
            for(int i=0;i<g.getProfessors().length;i++){
                gameStatus.getProfessors()[i] = g.getProfessors()[i];
            }
        }
        if(g.getClouds()!=null) updateCloudStatus();
        if(g.getArchipelagos()!=null) updateArchipelagoStatus();
        if(g.getPlayers()!=null) updatePlayerStatus();
        if(g.getCharacterCards()!=null) updateCardStatus();

    }
    private void updateCloudStatus() {

    }
    private void updateArchipelagoStatus(){

    }
    private void updateIslandStatus(){

    }
    private void updateCardStatus(){

    }
    private void updatePlayerStatus(){

    }
}
