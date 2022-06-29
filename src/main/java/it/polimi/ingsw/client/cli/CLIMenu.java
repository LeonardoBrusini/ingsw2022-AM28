package it.polimi.ingsw.client.cli;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.client.ClientObserver;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.StatusUpdater;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.Colour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CLIMenu implements ClientObserver {
    private final ArrayList<GamePhases> phases;
    private CurrentStatus currentStatus;
    private GameParameters parameters;
    private Command command;
    private final Gson parser;
    private final HashMap<String,String> characterCardDescriptions;
    private int[] temporaryGroup;

    public CLIMenu() {
        characterCardDescriptions = new HashMap<>();
        fillDescriptions(characterCardDescriptions);
        phases = new ArrayList<>();
        phases.add(GamePhases.USERNAME);
        parser = new Gson();
    }

    private void fillDescriptions(HashMap<String, String> characterCardDescriptions) {
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

    public void printMenu() {
        if(phases.get(0)== GamePhases.SENDCOMMAND) return;
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
            s+=", Assistant Cards left [weight, (max mother nature shifts)]: "+printAssistantCards(ps);
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
            if(!currentStatus.getWinner().equals("")) System.out.println(GamePhases.DRAW.getMenuPrompt());
            else System.out.println("\uD83C\uDF89PLAYER "+currentStatus.getWinner()+" WON!\uD83C\uDF89");
            System.exit(0);
        }
    }

    private String printAssistantCards(PlayerStatus ps) {
        String s="[";
        int i;
        for(i=0;i<ps.getAssistantCards().length-1;i++) {
            if(!ps.getAssistantCards()[i]) {
                s+=((i+1)+" ("+(i/2+1)+"),");
            }
        }
        if(!ps.getAssistantCards()[i]) s+=ps.getAssistantCards().length;
        s+=" ("+(i/2+1)+")]";
        return s;
    }

    public synchronized void manageReceivedLine(String line) {
        //System.out.println(line);
        switch (phases.get(0)) {
            case USERNAME,PRE_WAIT,GAME_MODE,NUM_PLAYERS -> {
                try {
                    AddPlayerResponse apr = parser.fromJson(line,AddPlayerResponse.class);
                    if(apr.isFirst()==null && apr.getErrorMessage()==null) {
                        manageCS(line);
                        if(currentStatus.getGameMode().equals("expert")) GamePhases.ACTION_COMMAND.setMenuPrompt(GamePhases.ACTION_COMMAND.getMenuPrompt()+"\n5) Play a Character Card");
                        return;
                    }
                    System.out.println("UPDATE APR");
                    if(apr.getStatus()==105) {
                        phases.add(GamePhases.PRE_WAIT);
                        nextPhase();
                        printMenu();
                        return;
                    }
                    if(apr.getStatus()!=0) {
                        System.out.println("ERROR: Status code "+apr.getStatus()+", "+apr.getErrorMessage());
                        printMenu();
                        return;
                    }
                    if(phases.get(0)!= GamePhases.NUM_PLAYERS && phases.get(0)!= GamePhases.GAME_MODE) {
                        if(apr.isFirst()) {
                            phases.add(GamePhases.NUM_PLAYERS);
                            phases.add(GamePhases.GAME_MODE);
                        } else {
                            phases.add(GamePhases.PRE_WAIT);
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
                    phases.add(GamePhases.DRAW);
                } else {
                    phases.add(GamePhases.WIN);
                }
            } else {
                if(!currentStatus.getTurn().getPlayer().equals(currentStatus.getPlayerID())) {
                    phases.add(GamePhases.WAIT);
                } else if(currentStatus.getTurn().getPhase().equals("PLANNING")) {
                    phases.add(GamePhases.PLANNING_COMMAND);
                } else {
                    phases.add(GamePhases.ACTION_COMMAND);
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
                        phases.add(GamePhases.SENDCOMMAND);
                        nextPhase();
                        //System.out.println("Sending: "+parser.toJson(command)+" in phase: "+phases.get(0).toString());
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
                            phases.add(GamePhases.ACTION_COMMAND);
                        } else {
                            temporaryGroup=new int[5];
                            for (int j = 0; j < i; j++) phases.add(GamePhases.PCC_STUDENT_ON_CARD);
                            phases.add(GamePhases.PCC_SUBMIT_GROUP_FROM);
                            phases.add(GamePhases.PCC_GROUP_ON_ENTRANCE);
                            for (int j = 0; j < i; j++) phases.add(GamePhases.PCC_STUDENT_ON_ENTRANCE);
                            phases.add(GamePhases.PCC_SUBMIT_GROUP_TO);
                            phases.add(GamePhases.SENDCOMMAND);
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
                            phases.add(GamePhases.ACTION_COMMAND);
                        } else {
                            temporaryGroup=new int[5];
                            for(int j=0;j<i;j++) phases.add(GamePhases.PCC_STUDENT_ON_HALL);
                            phases.add(GamePhases.PCC_SUBMIT_GROUP_TO);
                            phases.add(GamePhases.PCC_GROUP_ON_ENTRANCE);
                            for(int j=0;j<i;j++) phases.add(GamePhases.PCC_STUDENT_ON_ENTRANCE);
                            phases.add(GamePhases.PCC_SUBMIT_GROUP_FROM);
                            phases.add(GamePhases.SENDCOMMAND);
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
        if(phases.get(0)== GamePhases.PCC_GROUP_ON_ENTRANCE) {
            temporaryGroup = new int[5];
            nextPhase();
            printMenu();
        }
        if(phases.get(0)== GamePhases.SENDCOMMAND) {
            //System.out.println("SENDCOMMAND: returning the command json");
            //System.out.println(parser.toJson(command));
            return parser.toJson(command);
        }
        return null;
    }

    private void setCardRequirements(int i) {
        CharacterCardStatus ccs = currentStatus.getGame().getCharacterCards().get(i);
        switch (ccs.getFileName()) {
            case "P01.jpg" -> {
                phases.add(GamePhases.PCC_STUDENT_COLOUR);
                phases.add(GamePhases.PCC_ISLAND_INDEX);
                phases.add(GamePhases.SENDCOMMAND);
            }
            case "P03.jpg","P05.jpg" -> {
                phases.add(GamePhases.PCC_ISLAND_INDEX);
                phases.add(GamePhases.SENDCOMMAND);
            }
            case "P07.jpg" -> phases.add(GamePhases.PCC_GROUP_ON_CARD);
            case "P10.jpg" -> phases.add(GamePhases.PCC_GROUP_ON_HALL);
            case "P09.jpg","P11.jpg","P12.jpg" -> {
                phases.add(GamePhases.PCC_STUDENT_COLOUR);
                phases.add(GamePhases.SENDCOMMAND);
            }
            default -> phases.add(GamePhases.SENDCOMMAND);
        }
    }

    private void manageActionCommand(String line) throws NumberFormatException{
        int i = Integer.parseInt(line);
        command = new Command();
        command.setPlayerIndex(currentStatus.getPlayerID());
        switch (i) {
            case 1 -> {
                command.setCmd("MOVETOISLAND");
                phases.add(GamePhases.P_STUDENT_COLOUR);
                phases.add(GamePhases.P_ISLAND_INDEX);
                phases.add(GamePhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 2 -> {
                command.setCmd("MOVETOHALL");
                phases.add(GamePhases.P_STUDENT_COLOUR);
                phases.add(GamePhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 3 -> {
                command.setCmd("MOVEMOTHERNATURE");
                phases.add(GamePhases.P_MNSHIFTS);
                phases.add(GamePhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 4 -> {
                command.setCmd("TAKEFROMCLOUD");
                phases.add(GamePhases.P_CLOUD_INDEX);
                phases.add(GamePhases.SENDCOMMAND);
                nextPhase();
                printMenu();
            }
            case 5 -> {
                if(currentStatus.getGameMode().equals("expert")) {
                    command.setCmd("PLAYCHARACTERCARD");
                    phases.add(GamePhases.P_CCARD_INDEX);
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

    @Override
    public void manageMessage(String line) {
        manageReceivedLine(line);
    }

    @Override
    public void manageDisconnection() {
        System.out.println("Connection error, can't reach server");
        System.exit(0);
    }
}
