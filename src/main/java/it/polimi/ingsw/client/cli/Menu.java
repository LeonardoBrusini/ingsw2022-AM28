package it.polimi.ingsw.client.cli;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private ArrayList<CLIPhases> phases;
    private CurrentStatus currentStatus;
    private GameParameters parameters;
    private Command command;
    private final Gson parser;
    private boolean firstPlayer;
    private String username;
    private ParameterHandler parameterHandler;
    private boolean busyScanner;
    private PrintWriter writer;
    private Scanner scanner;

    public Menu() {
        firstPlayer = false;
        busyScanner = false;
        phases = new ArrayList<>();
        phases.add(CLIPhases.USERNAME);
        parser = new Gson();
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
        System.out.println(phases.get(0).getMenuPrompt());
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
        switch (phases.get(0)) {
            case USERNAME -> {
                //username response (addplayer response or error)
                //if ok response and first --> game parameters phases
                //if ok response --> pre wait phase
                //if error --> still username
            }
            case GAME_MODE -> {
                //first current status or error
                //if error wait for players --> pre wait phase
                //if other error, ask again for parameters
            }
            case SENDCOMMAND,WAIT -> {
                //update on currentsatus or error
                //if currentstatus and my turn --> set current phase planning/action
                //if currentstatus and not my turn --> set wait phase
                //if error --> set current phase planning/action
            }
            case PRE_WAIT -> {
                //firt current status or addplayer response
                //if addplayer response --> if first game parameters phase
                //if currentstatus and my turn --> set current phase planning/action
                //if currentstatus and not my turn --> set wait phase
            }
        }
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
        switch (phases.get(0)) {
            case USERNAME -> {
                return line;
                //UPDATE ON PHASES DEPENDS BY SERVER RESPONSE
            }
            case NUM_PLAYERS -> {
                if(line.equals("2") || line.equals("3")){
                    parameters = new GameParameters();
                    parameters.setNumPlayers(Integer.parseInt(line));
                    nextPhase();
                }
                printMenu();
            }
            case GAME_MODE -> {
                line = line.toLowerCase();
                if(line.equals("expert") || line.equals("simple")) {
                    parameters.setGameMode(line);
                    return parser.toJson(parameters);
                    //UPDATE ON PHASES DEPENDS BY SERVER RESPONSE
                }
            }
            case PLANNING_COMMAND -> {
                try {
                    int i = Integer.parseInt(line);
                    if(i<1 || i>10) {
                        command = new Command();
                        command.setPlayerIndex(currentStatus.getPlayerID());
                        command.setCmd("PLAYASSISTANTCARD");
                        command.setIndex(i-1);
                        return parser.toJson(command);
                        //UPDATE ON PHASES DEPENDS BY SERVER RESPONSE
                    } else {
                        printMenu();
                    }
                } catch (NumberFormatException e){
                    printMenu();
                }
            }
            case ACTION_COMMAND -> {
                try {
                    manageActionCommand(line);
                } catch (NumberFormatException e){
                    printMenu();
                }
            }
            case P_STUDENT_COLOUR -> {
                line = line.toUpperCase();
                try {
                    command.setStudentColour(Colour.valueOf(line).name());
                    nextPhase();
                    printMenu();
                } catch (IllegalArgumentException e) {
                    printMenu();
                }
            }
            case P_ISLAND_INDEX -> {
                try {
                    int i = Integer.parseInt(line);
                    if(i>=1 && i<=12) {
                        command.setIndex(i);
                        nextPhase();
                    }
                    printMenu();
                } catch (NumberFormatException e) {
                    printMenu();
                }
            }
            case P_CLOUD_INDEX -> {
                try {
                    int i = Integer.parseInt(line);
                    if(i>=1 && i<=currentStatus.getGame().getPlayers().size()) {
                        command.setIndex(i-1);
                        nextPhase();
                    }
                    printMenu();
                } catch (NumberFormatException e) {
                    printMenu();
                }
            }
            case P_CCARD_INDEX -> {
                //LOT TO DO HERE
            }
            case P_MNSHIFTS -> {
                try {
                    int i = Integer.parseInt(line);
                    int max = currentStatus.getGame().getPlayers().get(currentStatus.getPlayerID()).getLastAssistantCardPlayed()/2+1;
                    if(currentStatus.getGame().getPlayers().get(currentStatus.getPlayerID()).getAddedShifts()) max+=2;
                    if(i>=1 && i<=max) {
                        command.setMotherNatureShifts(i);
                        nextPhase();
                    }
                    printMenu();
                } catch (NumberFormatException e) {
                    printMenu();
                }
            }
            case PCC_ISLAND_INDEX -> {
                try {
                    int i = Integer.parseInt(line);
                    if(i>=1 && i<=12) {
                        command.setPIndex(i);
                        nextPhase();
                    }
                    printMenu();
                } catch (NumberFormatException e) {
                    printMenu();
                }
            }
            case PCC_STUDENT_COLOUR -> {
                line = line.toUpperCase();
                try {
                    command.setPColour(Colour.valueOf(line).name());
                    nextPhase();
                    printMenu();
                } catch (IllegalArgumentException e) {
                    printMenu();
                }
            }
            case PCC_SFROM -> {
                //STILL TO DO (based on the card can ask for a max of 2 or 3 students)
            }
            case PCC_STO -> {
                //STILL TO DO pt 2 (based on the card can ask for a max of 2 or 3 students)
            }
            case SENDCOMMAND -> {
                return parser.toJson(command);
                //UPDATE ON PHASES DEPENDS BY SERVER RESPONSE
            }
            default -> printMenu();
        }
        return null;
    }

    private void manageActionCommand(String line) throws NumberFormatException{
        int i = Integer.parseInt(line);
        command = new Command();
        command.setPlayerIndex(currentStatus.getPlayerID());
        switch (i) {
            case 1 -> {
                command.setCmd("MOVETOISLAND");
                phases.add(CLIPhases.P_STUDENT_COLOUR);
                phases.add(CLIPhases.P_ISLAND_INDEX);
                phases.add(CLIPhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 2 -> {
                command.setCmd("MOVETOHALL");
                phases.add(CLIPhases.P_STUDENT_COLOUR);
                phases.add(CLIPhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 3 -> {
                command.setCmd("MOVEMOTHERNATURE");
                phases.add(CLIPhases.P_MNSHIFTS);
                phases.add(CLIPhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 4 -> {
                command.setCmd("TAKEFROMCLOUD");
                phases.add(CLIPhases.P_CLOUD_INDEX);
                phases.add(CLIPhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 5 -> {
                if(currentStatus.getGameMode().equals("expert")) {
                    command.setCmd("PLAYCHARACTERCARD");
                    phases.add(CLIPhases.P_CCARD_INDEX);
                    nextPhase();
                }
                printMenu();
            }
            default -> printMenu();
        }
    }

    public void nextPhase() {
        phases.remove(0);
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
