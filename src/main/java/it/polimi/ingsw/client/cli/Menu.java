package it.polimi.ingsw.client.cli;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private CurrentStatus currentStatus;
    private Command command;
    private boolean firstPlayer;
    private String username;
    private ParameterHandler parameterHandler;
    private boolean busyScanner;
    private PrintWriter writer;
    private Scanner scanner;

    public Menu() {
        firstPlayer = false;
        busyScanner = false;
    }

    public synchronized String generateCommand(String userInput) {
        return userInput;
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
        if(c.getGame()!=null) updateGameStatus(c.getGame());
    }

    public synchronized void printResult(String line) {

        System.out.println(line);
    }


    public void printMenu() {
        if(currentStatus==null) {
            if(username==null) {
                System.out.println("Insert your username: ");
                return;
            }
            /*if(firstPlayer) {
                askParameters();
                return;
            }*/
            System.out.println("Waiting for the game to begin...");
            return;
        }
        if(currentStatus.getTurn().getPlayer().equals(currentStatus.getPlayerID())) {
            if(currentStatus.getTurn().getPhase().equals("PLANNING")) {
                System.out.println("1) Play Assistant Card");
                System.out.println("Chose your move: ");
                return;
            }
            System.out.println("1) Move Student on an Island");
            System.out.println("2) Move Student on your Hall");
            System.out.println("3) Move mother Nature");
            System.out.println("4) Take students from a cloud");
            if(currentStatus.getGame().getCharacterCards()!=null) System.out.println("5) Activate Character Card effect");
            return;
        }
        System.out.println("Waiting for other players to make their moves...");
    }

    private synchronized String askParameters(){
        int numPlayers;
        System.out.println("How many players? (min 2 - max 3):");
        numPlayers = scanner.nextInt();
        while (numPlayers!=2 && numPlayers!=3){
            System.out.println("How many players? (min 2 - max 3):");
            numPlayers = scanner.nextInt();
        }
        String gameMode;
        System.out.println("type \"expert\" for expert game mode, \"simple\" for simple game mode: ");
        gameMode = scanner.nextLine();
        while (!gameMode.equals("expert") && !gameMode.equals("simple")){
            System.out.println("type \"expert\" for expert game mode, \"simple\" for simple game mode: ");
            gameMode = scanner.nextLine();
        }
        GameParameters gm = new GameParameters();
        gm.setGameMode(gameMode);
        gm.setNumPlayers(numPlayers);
        busyScanner = false;
        System.out.println(new Gson().toJson(gm));
        return new Gson().toJson(gm);
    }

    public synchronized void printStatus() {
        if(currentStatus.getWinner()!=null) {
            System.out.println("PLAYER "+currentStatus.getWinner()+" WON!");
        }
        System.out.println("PLAYERS:");
        for(PlayerStatus ps : currentStatus.getGame().getPlayers()) {
            System.out.println(ps.getNickName()+": "+ps.getTowerColour()+" towers, "+ps.getCoins()+" coins");
            System.out.println("Dashboard: Entrance: "+ps.getStudentsOnEntrance()+", Hall: "+ps.getStudentsOnHall()+ ", Towers: "+ps.getNumTowers());
            System.out.println("Assistant Cards left: "+printAssistantCards(ps));
        }
        System.out.println("BOARD");
        for(ArchipelagoStatus as : currentStatus.getGame().getArchipelagos()) {
            String s = "Archipelago "+as.getIndex();
            if(currentStatus.getGame().getMotherNatureIndex()==as.getIndex()) s+=" MN";
            System.out.println(s+":");
            for (IslandStatus is:as.getIslands()) {
                s = "Island "+is.getIslandIndex()+": ";
                s+=is.getStudents();
                if(is.getTowerColour()!=null) s+=" Tower: "+is.getTowerColour();
                System.out.println(s);
            }
        }
        if(currentStatus.getGame().getCharacterCards()!=null) {
            System.out.println("Character cards:");
            for(CharacterCardStatus cs : currentStatus.getGame().getCharacterCards()) {
                String s = "Card "+cs.getIndex()+", Name: "+cs.getFileName();
                if(cs.getNoEntryTiles()!=null) s+=", NoEntryTiles: "+cs.getNoEntryTiles();
                if(cs.getStudents()!=null) s+=", Students: "+cs.getStudents();
                System.out.println(s);
            }
        }
        System.out.println("Clouds");
        for(CloudStatus cs : currentStatus.getGame().getClouds()) {
            System.out.println("Cloud "+cs.getIndex()+", Students: "+cs.getStudents());
        }
        System.out.println("Professors: "+currentStatus.getGame().getProfessors());
        System.out.println("Current Player: "+currentStatus.getTurn().getPlayer()+", Phase: "+currentStatus.getTurn().getPhase());
    }

    private String printAssistantCards(PlayerStatus ps) {
        String s="[";
        for(int i=0;i<ps.getAssistantCards().length-1;i++) {
            if(ps.getAssistantCards()[i]) {
                s+=(i+",");
            }
        }
        if(ps.getAssistantCards()[ps.getAssistantCards().length-1]) s+=ps.getAssistantCards().length-1;
        s+="]";
        return s;
    }

    private void updateGameStatus(GameStatus g) {
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
        if(g.getClouds()!=null) updateCloudStatus(g.getClouds());
        if(g.getArchipelagos()!=null) updateArchipelagoStatus(g.getArchipelagos());
        if(g.getPlayers()!=null) updatePlayerStatus(g.getPlayers());
        if(g.getCharacterCards()!=null) updateCardStatus(g.getCharacterCards());
    }
    private void updateCloudStatus(ArrayList<CloudStatus> clouds) {
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
    private void updateArchipelagoStatus(ArrayList<ArchipelagoStatus> archipelagos){
        if(currentStatus.getGame().getArchipelagos()==null || archipelagos.size()>1) {
            currentStatus.getGame().setArchipelagos(archipelagos);
            return;
        }
        ArrayList<ArchipelagoStatus> archipelagosStatus = currentStatus.getGame().getArchipelagos();
        for(ArchipelagoStatus as: archipelagos) {
            for (int i=0;i<archipelagosStatus.size();i++) {
                if(archipelagosStatus.get(i).getIndex()==as.getIndex()) {
                    if(as.getNoEntryTiles()!=null) archipelagosStatus.get(i).setNoEntryTiles(as.getNoEntryTiles());
                    if(as.getIslands()!=null) updateIslandStatus(i,as.getIslands());
                    break;
                }
            }
        }
    }
    private void updateIslandStatus(int i, ArrayList<IslandStatus> islands){
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
    private void updateCardStatus(ArrayList<CharacterCardStatus> characterCards){
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
    private void updatePlayerStatus(ArrayList<PlayerStatus> players){
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

    public synchronized String manageReceivedLine(String line) {
        System.out.println(line);
        if(currentStatus==null) {
            try {
                AddPlayerResponse apr = new Gson().fromJson(line,AddPlayerResponse.class);
                if(apr.getStatus()!=0) {
                    System.out.println("ERROR: Status code "+apr.getStatus()+", "+apr.getErrorMessage());
                    username=null;
                }
                if(apr.isFirst()) {
                    return askParameters();
                }
                busyScanner = false;
                return null;
            } catch (JsonSyntaxException e) {
                try {
                    updateStatus(new Gson().fromJson(line, CurrentStatus.class));
                    parameterHandler = new ParameterHandler(currentStatus.getPlayerID());
                    System.out.flush();
                    printStatus();
                    printMenu();
                } catch (JsonSyntaxException g){
                    username=null;
                    System.out.println("ERROR: received unreadable message");
                }
                return null;
            }
        } else {
            try {
                CurrentStatus cs = new Gson().fromJson(line, CurrentStatus.class);
                if(cs.getStatus()==105) {
                    busyScanner = true;
                    return askParameters();
                }
                if(cs.getStatus()!=0) {
                    System.out.println("ERROR: Status code "+cs.getStatus()+", "+cs.getErrorMessage());
                }
                updateStatus(cs);
                System.out.flush();
                printStatus();
                printMenu();
            } catch (JsonSyntaxException e) {
                System.out.println("ERROR: received unreadable message");
            }
            return null;
        }
    }

    public String manageInputLine(String line) {
        if(currentStatus==null && username==null) {
            username = line;
            busyScanner = true;
            return line;
        }
        if(currentStatus==null) return null;
        if(currentStatus.getTurn().getPlayer().equals(currentStatus.getPlayerID())) {
            busyScanner = true;
            if(currentStatus.getTurn().getPhase().equals("PLANNING")) {
                if(line.equals("1")) {
                    return parameterHandler.askAssistantCardParameters();
                }
                return null;
            }
            switch (line) {
                case "1": return parameterHandler.askMoveStudentToIslandParameters();
                case "2": return parameterHandler.askMoveStudentToHallParameters();
                case "3":
                    int lacShifts=1;
                    for(PlayerStatus ps: currentStatus.getGame().getPlayers()) {
                        if (currentStatus.getPlayerID() == ps.getIndex()) {
                            lacShifts = ps.getLastAssistantCardPlayed()/2+1;
                            if(ps.getAddedShifts()) lacShifts+=2;
                            break;
                        }
                    }
                    return parameterHandler.askMoveMotherNatureParameters(lacShifts);
                case "4":
                    int numPlayers = currentStatus.getGame().getPlayers().size();
                    return parameterHandler.askTakeStudentsFromCloudParameters(numPlayers);
                case "5":
                    if(currentStatus.getGame().getCharacterCards()==null) return null;
                    return parameterHandler.askPlayCharacterCardStatus();
                default: return null;
            }
        }
        return null;
    }

    public boolean isBusyScanner() {
        return busyScanner;
    }

    public void setWriter(PrintWriter out) {
        writer = out;
    }

    public void setScanner(Scanner stdIn) {
        scanner = stdIn;
    }
}
