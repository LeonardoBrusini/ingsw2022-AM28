package it.polimi.ingsw.client.cli;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.client.network.StatusUpdater;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;

import java.util.ArrayList;
import java.util.HashMap;

public class CLIMenu {
    private final ArrayList<CLIPhases> phases;
    private CurrentStatus currentStatus;
    private GameParameters parameters;
    private Command command;
    private final Gson parser;
    private final HashMap<String,String> characterCardDescriptions;
    private int[] temporaryGroup;

    public CLIMenu() {
        characterCardDescriptions = new HashMap<>();
        fillDescriprions(characterCardDescriptions);
        phases = new ArrayList<>();
        phases.add(CLIPhases.USERNAME);
        parser = new Gson();
    }

    private void fillDescriprions(HashMap<String, String> characterCardDescriptions) {
        characterCardDescriptions.put("P01.jpg","Take 1 Student from this card and place it on an Island of your choice. Then draw a new Student from the Bag and place it on this card.");
        characterCardDescriptions.put("P02.jpg","During this turn, you take control of any number of Professor even if you have the same number of Students as the player who currently controls them.");
        characterCardDescriptions.put("P03.jpg","Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move and the Island where she ends her movement will also be resolved.");
        characterCardDescriptions.put("P04.jpg","You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.");
        characterCardDescriptions.put("P05.jpg","Place a No Entry tile on a Island of your choice. The first time Mother Nature ends her movement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers.");
        characterCardDescriptions.put("P06.jpg","When resolving a Conquering on an Island, Tower do not count towards influence.");
        characterCardDescriptions.put("P07.jpg","You may take up to 3 students from this card and replace them with the same number of Students from your Entrance.");
        characterCardDescriptions.put("P08.jpg","During the influence calculation this turn, you count as having 2 more influence.");
        characterCardDescriptions.put("P09.jpg","Choose a color of Student: during the influence calculation this turn, that color adds no influence.");
        characterCardDescriptions.put("P10.jpg","You may exchange up to 2 Students between your Entrance and your Dining Room.");
        characterCardDescriptions.put("P11.jpg","Take 1 Student from this card and place it in your Dining Room. Then, draw a new Student from the Bag and place it on this card.");
        characterCardDescriptions.put("P12.jpg","Choose a type of Student: every player must return 3 Students of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as many Students as they have.");
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

    public void printMenu() {
        if(phases.get(0)==CLIPhases.SENDCOMMAND) return;
        switch (phases.get(0)) {
            case WAIT,ACTION_COMMAND,PLANNING_COMMAND,WIN,DRAW -> {if(currentStatus!=null) printStatus();}
        }
        System.out.println(phases.get(0).getMenuPrompt());
    }

    public synchronized void printStatus() {
        System.out.println("PLAYERS:");
        for(PlayerStatus ps : currentStatus.getGame().getPlayers()) {
            String s = ps.getNickName()+": "+ps.getTowerColour()+" towers";
            if(currentStatus.getGameMode().equals("expert")) s+=", "+ps.getCoins()+" coins";
            s+=", Assistant Cards left: "+printAssistantCards(ps);
            System.out.println(s);
            CLIPrinter.printDashboard(ps.getStudentsOnEntrance(),ps.getStudentsOnHall(),ps.getNumTowers());
        }
        System.out.println("BOARD");
        for(ArchipelagoStatus as : currentStatus.getGame().getArchipelagos()) {
            CLIPrinter.printArchipelago(as, currentStatus.getGame().getMotherNatureIndex());
        }
        if(currentStatus.getGame().getCharacterCards()!=null) {
            System.out.println("Character cards:");
            for(CharacterCardStatus cs : currentStatus.getGame().getCharacterCards()) {
                CLIPrinter.printCCards(cs,characterCardDescriptions.get(cs.getFileName()));
            }
        }
        System.out.println("Clouds");
        CLIPrinter.printClouds(currentStatus.getGame().getClouds());
        CLIPrinter.printProfessors(currentStatus.getGame().getProfessors());
        System.out.println("Current Player: "+currentStatus.getTurn().getPlayer()+", Phase: "+currentStatus.getTurn().getPhase());
        if(currentStatus.getWinner()!=null) {
            System.out.println("\uD83C\uDF89PLAYER "+currentStatus.getWinner()+" WON!\uD83C\uDF89");
        }
    }

    private String printAssistantCards(PlayerStatus ps) {
        String s="[";
        for(int i=0;i<ps.getAssistantCards().length-1;i++) {
            if(!ps.getAssistantCards()[i]) {
                s+=((i+1)+",");
            }
        }
        if(!ps.getAssistantCards()[ps.getAssistantCards().length-1]) s+=ps.getAssistantCards().length;
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

    public synchronized void manageReceivedLine(String line) {
        System.out.println(line);
        switch (phases.get(0)) {
            case USERNAME,PRE_WAIT,GAME_MODE,NUM_PLAYERS -> {
                try {
                    AddPlayerResponse apr = parser.fromJson(line,AddPlayerResponse.class);
                    if(apr.isFirst()==null && apr.getErrorMessage()==null) {
                        manageCS(line);
                        if(currentStatus.getGameMode().equals("expert")) CLIPhases.ACTION_COMMAND.setMenuPrompt(CLIPhases.ACTION_COMMAND.getMenuPrompt()+"\n5) Play a Character Card");
                        return;
                    }
                    System.out.println("UPDATE APR");
                    if(apr.getStatus()==105) {
                        phases.add(CLIPhases.PRE_WAIT);
                        nextPhase();
                        printMenu();
                        return;
                    }
                    if(apr.getStatus()!=0) {
                        System.out.println("ERROR: Status code "+apr.getStatus()+", "+apr.getErrorMessage());
                        printMenu();
                        return;
                    }
                    if(phases.get(0)!=CLIPhases.NUM_PLAYERS && phases.get(0)!=CLIPhases.GAME_MODE) {
                        if(apr.isFirst()) {
                            phases.add(CLIPhases.NUM_PLAYERS);
                            phases.add(CLIPhases.GAME_MODE);
                        } else {
                            phases.add(CLIPhases.PRE_WAIT);
                        }
                        nextPhase();
                        printMenu();
                    }
                } catch (JsonSyntaxException e) {
                    System.out.println("ERROR: received unreadable message");
                    printStatus();
                }
            }
            case SENDCOMMAND,WAIT -> {
                try {
                    manageCS(line);
                } catch (JsonSyntaxException e) {
                    System.out.println("ERROR: received unreadable message");
                    printStatus();
                }
            }
        }
    }

    private void manageCS(String line) throws JsonSyntaxException{
        try {
            CurrentStatus cs = parser.fromJson(line, CurrentStatus.class);
            System.out.println("UPDATE CS");
            if(cs.getStatus()!=0) {
                System.out.println("ERROR: Status code "+cs.getStatus()+", "+cs.getErrorMessage());
            }
            currentStatus = StatusUpdater.updateStatus(currentStatus,cs);
            if(currentStatus.getWinner()!=null) {
                if(currentStatus.getWinner().equals("")) {
                    phases.add(CLIPhases.DRAW);
                } else {
                    phases.add(CLIPhases.WIN);
                }
            } else {
                if(!currentStatus.getTurn().getPlayer().equals(currentStatus.getPlayerID())) {
                    phases.add(CLIPhases.WAIT);
                } else if(currentStatus.getTurn().getPhase().equals("PLANNING")) {
                    phases.add(CLIPhases.PLANNING_COMMAND);
                } else {
                    phases.add(CLIPhases.ACTION_COMMAND);
                }
            }
            nextPhase();
            printMenu();
        } catch (JsonSyntaxException e) {
            System.out.println("ERR");
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
                    if(i>=1 && i<=10) {
                        command = new Command();
                        command.setPlayerIndex(currentStatus.getPlayerID());
                        command.setCmd("PLAYASSISTANTCARD");
                        command.setIndex(i-1);
                        phases.add(CLIPhases.SENDCOMMAND);
                        nextPhase();
                        System.out.println("Sending: "+parser.toJson(command)+" in phase: "+phases.get(0).toString());
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
                try {
                    int i = Integer.parseInt(line);
                    if(i>=1 && i<=3) {
                        command.setIndex(i-1);
                        setCardRequirements(i-1);
                        nextPhase();
                    }
                    printMenu();
                } catch (NumberFormatException e) {
                    printMenu();
                }
            }
            case P_MNSHIFTS -> {
                try {
                    int i = Integer.parseInt(line);
                    int max = currentStatus.getGame().getPlayers().get(currentStatus.getPlayerID()).getLastAssistantCardPlayed()/2+1;
                    Boolean gas = currentStatus.getGame().getPlayers().get(currentStatus.getPlayerID()).getAddedShifts();
                    if(gas!=null && gas) max+=2;
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
            case PCC_GROUP_ON_CARD -> {
                try {
                    int i = Integer.parseInt(line);
                    int[] studentsOnEntrance = currentStatus.getGame().getPlayers().get(currentStatus.getPlayerID()).getStudentsOnEntrance();
                    int sumE=0;
                    for(int s: studentsOnEntrance) sumE+=s;
                    if(i>=1 && i<=3) {
                        if(i>sumE) {
                            System.out.println("NOT ENOUGH STUDENTS IN YOUR ENTRANCE");
                            phases.add(CLIPhases.ACTION_COMMAND);
                        } else {
                            temporaryGroup=new int[5];
                            for (int j = 0; j < i; j++) phases.add(CLIPhases.PCC_STUDENT_ON_CARD);
                            phases.add(CLIPhases.PCC_SUBMIT_GROUP_FROM);
                            phases.add(CLIPhases.PCC_GROUP_ON_ENTRANCE);
                            for (int j = 0; j < i; j++) phases.add(CLIPhases.PCC_STUDENT_ON_ENTRANCE);
                            phases.add(CLIPhases.PCC_SUBMIT_GROUP_TO);
                            phases.add(CLIPhases.SENDCOMMAND);
                        }
                        nextPhase();
                    }
                    printMenu();
                } catch (NumberFormatException e) {
                    printMenu();
                }
            }
            case PCC_GROUP_ON_HALL -> {
                try {
                    int i = Integer.parseInt(line);
                    int[] studentsOnHall = currentStatus.getGame().getPlayers().get(currentStatus.getPlayerID()).getStudentsOnHall();
                    int[] studentsOnEntrance = currentStatus.getGame().getPlayers().get(currentStatus.getPlayerID()).getStudentsOnEntrance();
                    int sumH=0,sumE=0;
                    for(int s: studentsOnHall) sumH+=s;
                    for(int s: studentsOnEntrance) sumE+=s;
                    if(i>=1 && i<=2) {
                        if(i>sumH || i>sumE) {
                            System.out.println("NOT ENOUGH STUDENTS IN YOUR HALL OR ENTRANCE");
                            phases.add(CLIPhases.ACTION_COMMAND);
                        } else {
                            temporaryGroup=new int[5];
                            for(int j=0;j<i;j++) phases.add(CLIPhases.PCC_STUDENT_ON_HALL);
                            phases.add(CLIPhases.PCC_SUBMIT_GROUP_TO);
                            phases.add(CLIPhases.PCC_GROUP_ON_ENTRANCE);
                            for(int j=0;j<i;j++) phases.add(CLIPhases.PCC_STUDENT_ON_ENTRANCE);
                            phases.add(CLIPhases.PCC_SUBMIT_GROUP_FROM);
                            phases.add(CLIPhases.SENDCOMMAND);
                        }
                        nextPhase();
                    }
                    printMenu();
                } catch (NumberFormatException e) {
                    printMenu();
                }
            }
            case PCC_STUDENT_ON_CARD,PCC_STUDENT_ON_HALL,PCC_STUDENT_ON_ENTRANCE -> {
                line = line.toUpperCase();
                try {
                    temporaryGroup[Colour.valueOf(line).ordinal()]++;
                    nextPhase();
                    printMenu();
                } catch (IllegalArgumentException e) {
                    printMenu();
                }
            }
            case WIN,DRAW -> System.out.println(phases.get(0).getMenuPrompt());
            default -> printMenu();
        }
        switch (phases.get(0)) {
            case PCC_SUBMIT_GROUP_FROM -> {
                command.setPStudentsFrom(temporaryGroup);
                nextPhase();
                printMenu();
            }
            case PCC_SUBMIT_GROUP_TO -> {
                command.setPStudentsTo(temporaryGroup);
                nextPhase();
                printMenu();
            }
        }
        if(phases.get(0)==CLIPhases.PCC_GROUP_ON_ENTRANCE) {
            temporaryGroup = new int[5];
            nextPhase();
            printMenu();
        }
        if(phases.get(0)==CLIPhases.SENDCOMMAND) {
            System.out.println("SENDCOMMAND: returning the command json");
            System.out.println(parser.toJson(command));
            return parser.toJson(command);
        }
        return null;
    }

    private void setCardRequirements(int i) {
        CharacterCardStatus ccs = currentStatus.getGame().getCharacterCards().get(i);
        switch (ccs.getFileName()) {
            case "P01.jpg" -> {
                phases.add(CLIPhases.PCC_STUDENT_COLOUR);
                phases.add(CLIPhases.PCC_ISLAND_INDEX);
                phases.add(CLIPhases.SENDCOMMAND);
            }
            case "P03.jpg","P05.jpg" -> {
                phases.add(CLIPhases.PCC_ISLAND_INDEX);
                phases.add(CLIPhases.SENDCOMMAND);
            }
            case "P07.jpg" -> phases.add(CLIPhases.PCC_GROUP_ON_CARD);
            case "P10.jpg" -> phases.add(CLIPhases.PCC_GROUP_ON_HALL);
            case "P09.jpg","P11.jpg","P12.jpg" -> {
                phases.add(CLIPhases.PCC_STUDENT_COLOUR);
                phases.add(CLIPhases.SENDCOMMAND);
            }
        }
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

    private void nextPhase() {
        phases.remove(0);
    }
}