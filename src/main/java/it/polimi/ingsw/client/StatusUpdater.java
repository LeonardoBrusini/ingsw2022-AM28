package it.polimi.ingsw.client;

import it.polimi.ingsw.network.*;

import java.util.ArrayList;

public class StatusUpdater {
    private CurrentStatus currentStatus;
    private static StatusUpdater instance;
    private StatusUpdater() {
        currentStatus = null;
    }

    public static StatusUpdater instance() {
        if(instance==null) instance=new StatusUpdater();
        return instance;
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public synchronized void updateStatus(CurrentStatus c) {
        if(currentStatus==null) {
            currentStatus = c;
            return;
        }
        if(c.getGameMode()!=null) currentStatus.setGameMode(c.getGameMode());
        if(c.getPlayerID()!=null) currentStatus.setPlayerID(c.getPlayerID());
        if(c.getWinner()!=null) currentStatus.setWinner(c.getWinner());
        if(c.getTurn()!=null) {
            if(currentStatus.getTurn()==null) currentStatus.setTurn(new TurnStatus());
            if(c.getTurn().getPlayer()!=null) currentStatus.getTurn().setPlayer(c.getTurn().getPlayer());
            if(c.getTurn().getPhase()!=null) currentStatus.getTurn().setPhase(c.getTurn().getPhase());
        }
        if(c.getGame()!=null) updateGameStatus(currentStatus,c.getGame());
    }

    public static synchronized CurrentStatus updateStatus(CurrentStatus currentStatus, CurrentStatus c) {
        if(currentStatus==null) {
            currentStatus = c;
            return currentStatus;
        }
        if(c.getGameMode()!=null) currentStatus.setGameMode(c.getGameMode());
        if(c.getPlayerID()!=null) currentStatus.setPlayerID(c.getPlayerID());
        if(c.getWinner()!=null) currentStatus.setWinner(c.getWinner());
        if(c.getTurn()!=null) {
            if(currentStatus.getTurn()==null) currentStatus.setTurn(new TurnStatus());
            if(c.getTurn().getPlayer()!=null) currentStatus.getTurn().setPlayer(c.getTurn().getPlayer());
            if(c.getTurn().getPhase()!=null) currentStatus.getTurn().setPhase(c.getTurn().getPhase());
        }
        if(c.getGame()!=null) updateGameStatus(currentStatus,c.getGame());
        return currentStatus;
    }
    private static void updateGameStatus(CurrentStatus currentStatus, GameStatus g) {
        if(currentStatus.getGame()==null) {
            currentStatus.setGame(g);
            return;
        }
        GameStatus gameStatus = currentStatus.getGame();
        if(g.getMotherNatureIndex()!=null) gameStatus.setMotherNatureIndex(g.getMotherNatureIndex());
        if(g.getProfessors()!=null) {
            if(gameStatus.getProfessors()==null) gameStatus.setProfessors(new int[g.getProfessors().length]);
            for(int i=0;i<g.getProfessors().length;i++){
                gameStatus.getProfessors()[i] = g.getProfessors()[i];
            }
        }
        if(g.getClouds()!=null) updateCloudStatus(currentStatus,g.getClouds());
        if(g.getArchipelagos()!=null) updateArchipelagoStatus(currentStatus,g.getArchipelagos());
        if(g.getPlayers()!=null) updatePlayerStatus(currentStatus,g.getPlayers());
        if(g.getCharacterCards()!=null) updateCardStatus(currentStatus,g.getCharacterCards());
    }
    private static void updateCloudStatus(CurrentStatus currentStatus, ArrayList<CloudStatus> clouds) {
        if(currentStatus.getGame().getClouds()==null) {
            currentStatus.getGame().setClouds(clouds);
            return;
        }
        ArrayList<CloudStatus> cloudsStatus = currentStatus.getGame().getClouds();
        for(CloudStatus cs: clouds) {
            for (CloudStatus status : cloudsStatus) {
                if (status.getIndex() == cs.getIndex()) {
                    if (cs.getStudents() != null) {
                        status.setStudents(cs.getStudents());
                        break;
                    }
                }
            }
        }
    }
    private static void updateArchipelagoStatus(CurrentStatus currentStatus, ArrayList<ArchipelagoStatus> archipelagos){
        if(currentStatus.getGame().getArchipelagos()==null || archipelagos.size()>1) {
            currentStatus.getGame().setArchipelagos(archipelagos);
            return;
        }
        ArrayList<ArchipelagoStatus> archipelagosStatus = currentStatus.getGame().getArchipelagos();
        for(ArchipelagoStatus as: archipelagos) {
            for (int i=0;i<archipelagosStatus.size();i++) {
                if(archipelagosStatus.get(i).getIndex()==as.getIndex()) {
                    if(as.getNoEntryTiles()!=null) archipelagosStatus.get(i).setNoEntryTiles(as.getNoEntryTiles());
                    if(as.getIslands()!=null) updateIslandStatus(currentStatus,i,as.getIslands());
                    break;
                }
            }
        }
    }
    private static void updateIslandStatus(CurrentStatus currentStatus, int i, ArrayList<IslandStatus> islands){
        ArchipelagoStatus as = currentStatus.getGame().getArchipelagos().get(i);
        if(as.getIslands()==null || islands.size()>1) {
            as.setIslands(islands);
            return;
        }
        for (IslandStatus is: islands) {
            for (int j=0;j<as.getIslands().size();j++) {
                if(as.getIslands().get(j).getIslandIndex()==is.getIslandIndex()) {
                    if(is.getStudents()!=null) as.getIslands().get(j).setStudents(is.getStudents());
                    if(is.getTowerColour()!=null) as.getIslands().get(j).setTowerColour(is.getTowerColour());
                    break;
                }
            }
        }
    }
    private static void updateCardStatus(CurrentStatus currentStatus,ArrayList<CharacterCardStatus> characterCards){
        ArrayList<CharacterCardStatus> characterCardsStatus = currentStatus.getGame().getCharacterCards();
        if(characterCardsStatus==null) {
            currentStatus.getGame().setCharacterCards(characterCards);
            return;
        }
        for(CharacterCardStatus cs : characterCards) {
            for (CharacterCardStatus cardsStatus : characterCardsStatus) {
                if (cardsStatus.getIndex() == cs.getIndex()) {
                    if (cs.getStudents() != null) cardsStatus.setStudents(cs.getStudents());
                    if (cs.getFileName() != null) cardsStatus.setFileName(cs.getFileName());
                    if (cs.getNoEntryTiles() != null) cardsStatus.setNoEntryTiles(cs.getNoEntryTiles());
                    break;
                }
            }
        }
    }
    private static void updatePlayerStatus(CurrentStatus currentStatus, ArrayList<PlayerStatus> players){
        ArrayList<PlayerStatus> playersStatus = currentStatus.getGame().getPlayers();
        if(playersStatus==null) {
            currentStatus.getGame().setPlayers(players);
            return;
        }
        for(PlayerStatus ps : players) {
            for (PlayerStatus status : playersStatus) {
                if (status.getIndex() == ps.getIndex()) {
                    if (ps.getStudentsOnHall() != null) status.setStudentsOnHall(ps.getStudentsOnHall());
                    if (ps.getStudentsOnEntrance() != null) status.setStudentsOnEntrance(ps.getStudentsOnEntrance());
                    if (ps.getAssistantCards() != null) status.setAssistantCards(ps.getAssistantCards());
                    if (ps.getAddedShifts() != null) status.setAddedShifts(ps.getAddedShifts());
                    if (ps.getCoins() != null) status.setCoins(ps.getCoins());
                    if (ps.getLastAssistantCardPlayed() != null)
                        status.setLastAssistantCardPlayed(ps.getLastAssistantCardPlayed());
                    if (ps.getNumTowers() != null) status.setNumTowers(ps.getNumTowers());
                    if (ps.getTowerColour() != null) status.setTowerColour(ps.getTowerColour());
                    if (ps.getNickName() != null) status.setNickName(ps.getNickName());
                    break;
                }
            }
        }
    }
}
