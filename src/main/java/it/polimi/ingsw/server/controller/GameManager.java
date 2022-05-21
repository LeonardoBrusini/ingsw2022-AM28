package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.board.Archipelago;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.Cloud;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.enumerations.AssistantCardInfo;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import it.polimi.ingsw.server.model.players.AssistantCard;
import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;

public class GameManager {
    private ArrayList<Player> players;
    private Board board;
    private TurnManager turnManager;
    private boolean gameStarted;
    private boolean expertMode;

    public GameManager() {
        players = new ArrayList<>();
        gameStarted = false;
    }

    /**
     * adds a new player if the lobby is not full (no more than 3 players)
     */
    public synchronized void addPlayer(){
        if(players.size()<3) {
            players.add(new Player(Tower.values()[players.size()]));
        }
    }

    /**
     * initializes the board, which objects on it have attributes which are initialized based on the number of players
     */
    public synchronized void newGame(boolean expertMode, int numPlayers){
       this.expertMode = expertMode;
       if(numPlayers==2 && players.size()==3) players.remove(2);
       board = new Board(players.size());
       if(players.size()==2) {
           for (Player p: players) {
               p.getDashboard().setNumTowers(8);
               p.getDashboard().fillEntrance(new StudentGroup(board.getBag().removeStudents(7)));
           }
       } else if (players.size()==3) {
           for (Player p: players) {
               p.getDashboard().setNumTowers(6);
               p.getDashboard().fillEntrance(new StudentGroup(board.getBag().removeStudents(9)));
           }
       }
       turnManager = new TurnManager(players.size(), board);
       gameStarted = true;
       // to be continued
    }


    /**
     * the selected player plays an assistant card
     * @param p the player who wants to play a card
     * @param c index of the card the player wants to play
     */
    public synchronized void playAssistantCard(int p, int c) throws WrongPhaseException, WrongTurnException, IllegalArgumentException, AlreadyPlayedException{
        if(p<0 || p>=players.size() || c<0 || c>=AssistantCardInfo.values().length) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.PLANNING) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=p)throw new WrongTurnException();
        boolean alreadyPlayed = false;
        ArrayList<AssistantCardInfo> alreadyPlayedCards = new ArrayList<>();
        for(int i=0; i<turnManager.getPlanningOrder().size(); i++) {
            if(turnManager.getPlanningOrder().get(i)==p) break;
            if(players.get(turnManager.getPlanningOrder().get(i)).getLastPlayedCard()!=null) {
                alreadyPlayedCards.add(players.get(turnManager.getPlanningOrder().get(i)).getLastPlayedCard().getInfo());
                if(players.get(turnManager.getPlanningOrder().get(i)).getLastPlayedCard().getInfo().ordinal()==c) {
                    alreadyPlayed = true;
                }
            }
        }
        if(alreadyPlayed) {
            for(AssistantCard ac: players.get(p).getCards()) {
                boolean notPlayedByOthers = true;
                for(AssistantCardInfo aci: alreadyPlayedCards) {
                    if(ac.getInfo()==aci && !ac.isPlayed()) notPlayedByOthers = false;
                }
                if(notPlayedByOthers) throw new IllegalArgumentException();
            }
        }
        players.get(p).playCard(c);
        turnManager.nextPhase(board,players);
    }
    /**
     * the selected player moves a student from the entrance to the hall, then checks for the professor
     * @param p the player who wants to move a student
     * @param c the colour of the student
     */
    public synchronized void moveStudentsToHall(int p, Colour c) throws FullHallException, NoStudentsException, WrongPhaseException, WrongTurnException{
        if (p<0 || p>=players.size() || c==null) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMoveStudentsPhase()) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=p) throw new WrongTurnException();
        players.get(p).moveToHall(c);
        checkProfessors(c);
        turnManager.nextPhase(board,players);
    }

    /**
     * checks which player gets the professor of colour c (modifies behaviour if CARD2 is activated)
     * @param c the colour of the professor
     */
    public synchronized void checkProfessors(Colour c) {
        int maxStudents = 0;
        Player maxStudentsPlayer = null;
        for(Player p: players) {
            if(p.getDashboard().getHall().getQuantityColour(c)>maxStudents) {
                maxStudents = p.getDashboard().getHall().getQuantityColour(c);
                maxStudentsPlayer = p;
            } else if (p.getDashboard().getHall().getQuantityColour(c) == maxStudents) {
                //look for CARD2
                CharacterCard card2 = null;
                for(CharacterCard card : board.getCharacterCards()) {
                    if(card.getCardInfo()==CharacterCardInfo.CARD2) {
                        card2 = card;
                    }
                }
                if(card2!=null && card2.isActivated()) {
                    if(p == card2.getPlayerThisTurn()) maxStudentsPlayer = p;
                } else  {
                    maxStudentsPlayer = null;
                }
            }
        }
        if(maxStudentsPlayer!=null) {
            board.assignProfessor(c,maxStudentsPlayer.getTower());
        }
    }

    /**
     * the selected player moves a student from the entrance to the selected island
     * @param p the player who wants to move a student
     * @param c the colour of the student
     * @param is index of the island
     */
    public synchronized void moveStudentToIsland(int p, Colour c, int is) throws WrongTurnException, WrongPhaseException, NoStudentsException{
        if(p<0 || p>=players.size() || c==null || is<1 || is>12) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMoveStudentsPhase()) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=p) throw new WrongTurnException();
        players.get(p).moveToIsland(c,board.getIslandManager().getIslandByIndex(is));
        turnManager.nextPhase(board,players);
    }

    /**
     * this method must be activated when the player asks to move mother nature
     * after being moved, it checks the player with most influence on the archipelago mother nature is on and eventually changes the towers
     * and checks for aggregation.
     * @param moves the number of archipelagos mother nature has to move forward
     */
    public synchronized void moveMotherNature(int moves) throws WrongPhaseException, IllegalArgumentException{
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isMotherNaturePhase()) throw new WrongPhaseException();
        if(moves<1 || moves>players.get(turnManager.getCurrentPlayer()).getLastPlayedCard().getInfo().getMotherNatureShifts()) throw new IllegalArgumentException();
        board.moveMotherNature(moves);
        checkInfluence(); //check if this method works properly
        turnManager.nextPhase(board,players);
    }

    /**
     * checks the player with most influence on the archipelago and build towers on it if needed
     */
    public synchronized void checkInfluence() {
        Archipelago a = board.getIslandManager().getArchipelagoByIslandIndex(board.getMotherNature().getIslandIndex());
        if(a.getNoEntryTiles()>0) {
            for (CharacterCard c: board.getCharacterCards()) {
                if (c.getCardInfo() == CharacterCardInfo.CARD5) c.setNoEntryTiles(c.getNoEntryTiles()+1);
            }
            a.setNoEntryTiles(a.getNoEntryTiles()-1);
        }
        Player p = board.getMotherNature().playerWithMostInfluence(players,board.getIslandManager(),board.getProfessorGroup(),board.getCharacterCards());
        if(p!=null) {
            for(Island i: a.getIslands()) {
                if(i.getTower()==null){
                    p.getDashboard().buildTower();
                    board.getIslandManager().setTowerOnIsland(p.getTower(),i.getIslandIndex());
                } else if(i.getTower()!=p.getTower()) {
                    Player opponent = findPlayerByTower(i.getTower());
                    opponent.getDashboard().addTower(); //may produce NullPointerException
                    p.getDashboard().buildTower();
                    board.getIslandManager().setTowerOnIsland(p.getTower(),i.getIslandIndex());
                }
            }
        }
    }

    /**
     * takes the students from a cloud and puts them on the entrance of the player's dashboard
     * @param cloudIndex index of the cloud the player selected
     * @param playerIndex player who asked to take the students
     */
    public synchronized void takeStudentsFromCloud(int cloudIndex, int playerIndex) throws WrongPhaseException, IllegalArgumentException, WrongTurnException, NoStudentsException{
        if(cloudIndex<0 || cloudIndex>=players.size() || playerIndex<0 || playerIndex>=players.size()) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || !turnManager.isCloudSelectionPhase()) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=playerIndex) throw new WrongTurnException();
        ArrayList<Cloud> clouds = board.getClouds();
        StudentGroup sg = clouds.get(cloudIndex).clearStudents();
        if(sg.empty()) {
            for (Cloud cloud : clouds) {
                if (!cloud.empty()) {
                    throw new NoStudentsException();
                }
            }
        }
        players.get(playerIndex).fillDashboardEntrance(sg);
        turnManager.nextPhase(board,players);
    }

    /**
     *
     * @param t the colour of the tower
     * @return the player who has that tower colour
     */
    private synchronized Player findPlayerByTower(Tower t) {
        for (Player p: players){
            if(p.getTower()==t) return p;
        }
        return null;
    }

    //getters & setters methods
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * method used to play one of the following cards: CARD2, CARD4, CARD6, CARD8
     * @param index player index
     * @param posCharacterCard character card index
     */
    public synchronized void playCharacterCard(int index, int posCharacterCard) throws IllegalArgumentException, WrongPhaseException, AlreadyPlayedException, NotEnoughCoinsException, WrongTurnException {
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || (!turnManager.isMoveStudentsPhase() && !turnManager.isMotherNaturePhase())) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=index) throw new WrongTurnException();
        Player p = players.get(index);
        if(p.isCcActivatedThisTurn()) throw new AlreadyPlayedException();

        CharacterCard card = board.getCharacterCards().get(posCharacterCard);
        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard);
            card.setPlayerThisTurn(p);
            card.setGameManager(this);
            card.setBoard(board);
            card.getCardInfo().getEffect().resolveEffect(card);
            p.setCcActivatedThisTurn(true);
        } catch (NotEnoughCoinsException exception) {
            throw new NotEnoughCoinsException();
            //error, player does not have enough coins
        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException();
        }
    }

    /**
     * method used to play one of the following cards: CARD9, CARD11, CARD12
     * @param index player index
     * @param posCharacterCard character card index
     * @param colour colour of the student
     */
    public synchronized void playCharacterCard(int index, int posCharacterCard, Colour colour) throws IllegalArgumentException, WrongPhaseException, AlreadyPlayedException, NotEnoughCoinsException, WrongTurnException {
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || colour==null) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || (!turnManager.isMoveStudentsPhase() && !turnManager.isMotherNaturePhase())) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=index) throw new WrongTurnException();
        Player p = players.get(index);
        if(p.isCcActivatedThisTurn()) throw new AlreadyPlayedException();

        CharacterCard card = board.getCharacterCards().get(posCharacterCard);
        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard, colour);
            card.setPlayerThisTurn(p);
            card.setGameManager(this);
            card.setBoard(board);
            card.setSelectedColour(colour);
            card.getCardInfo().getEffect().resolveEffect(card);
            p.setCcActivatedThisTurn(true);
        } catch (NotEnoughCoinsException exception) {
            throw new NotEnoughCoinsException();
            //error, player does not have enough coins
        }catch (IllegalArgumentException h){
            throw new IllegalArgumentException();
        }
    }

    /**
     * method used to play one of the following cards: CARD1
     * @param index player index
     * @param posCharacterCard character card index
     * @param colour colour of the student
     * @param islandIndex index of the island
     */
    public synchronized void playCharacterCard(int index, int posCharacterCard, Colour colour, int  islandIndex) throws IllegalArgumentException, WrongPhaseException, AlreadyPlayedException, NotEnoughCoinsException, WrongTurnException {
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || colour==null || islandIndex<1 || islandIndex>12) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || (!turnManager.isMoveStudentsPhase() && !turnManager.isMotherNaturePhase())) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=index) throw new WrongTurnException();
        Player p = players.get(index);
        if(p.isCcActivatedThisTurn()) throw new AlreadyPlayedException();
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);

        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard,colour,islandIndex);
            card.setPlayerThisTurn(p);
            card.setBoard(board);
            card.setGameManager(this);
            card.setSelectedColour(colour);
            card.setSelectedIsland(board.getIslandManager().getIslandByIndex(islandIndex));
            card.getCardInfo().getEffect().resolveEffect(card);
            p.setCcActivatedThisTurn(true);
        } catch (NotEnoughCoinsException exception) {
            throw new NotEnoughCoinsException();
            //error, player does not have enough coins
        }catch (IllegalArgumentException h){
            throw new IllegalArgumentException();
        }
    }

    /**
     * method used to play one of the following cards: CARD3, CARD5
     * @param index player index
     * @param posCharacterCard character card index
     * @param islandIndex index of the island
     */
    public synchronized void playCharacterCard(int index, int posCharacterCard,  int  islandIndex) throws IllegalArgumentException, WrongPhaseException, AlreadyPlayedException, NotEnoughCoinsException, WrongTurnException {
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || islandIndex<1 || islandIndex>12) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || (!turnManager.isMoveStudentsPhase() && !turnManager.isMotherNaturePhase())) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=index) throw new WrongTurnException();
        Player p = players.get(index);
        if(p.isCcActivatedThisTurn()) throw new AlreadyPlayedException();
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);
        if(card.getCardInfo()==CharacterCardInfo.CARD5 && card.getNoEntryTiles()==0) throw new IllegalArgumentException();

        try {
            p.spendCoins(card.getPrice());
            //board.playCharacterCard(posCharacterCard,islandIndex);
            card.setGameManager(this);
            card.setPlayerThisTurn(p);
            card.setBoard(board);
            card.setSelectedIsland(board.getIslandManager().getIslandByIndex(islandIndex));
            card.getCardInfo().getEffect().resolveEffect(card);
            p.setCcActivatedThisTurn(true);
        } catch (NotEnoughCoinsException exception) {
            throw new NotEnoughCoinsException();
            //error, player does not have enough coins
        }catch (IllegalArgumentException h){
            throw new IllegalArgumentException();
        }
    }

    /**
     * method used to play one of the following cards: CARD7, CARD10
     * @param index player index
     * @param posCharacterCard character card index
     * @param studentGroupFrom first group of students
     * @param studentGroupTo second group of students
     */
    public synchronized void playCharacterCard(int index, int posCharacterCard, StudentGroup studentGroupFrom, StudentGroup studentGroupTo) throws IllegalArgumentException, WrongPhaseException, AlreadyPlayedException, NotEnoughCoinsException, WrongTurnException {
        if(index<0 || index>=players.size() || posCharacterCard<0 || posCharacterCard>=3 || studentGroupFrom==null || studentGroupTo==null || studentGroupFrom.getTotalStudents()!=studentGroupTo.getTotalStudents()) throw new IllegalArgumentException();
        if(turnManager.getPhase()!=Phase.ACTION || (!turnManager.isMoveStudentsPhase() && !turnManager.isMotherNaturePhase())) throw new WrongPhaseException();
        if(turnManager.getCurrentPlayer()!=index) throw new WrongTurnException();
        Player p = players.get(index);
        if(p.isCcActivatedThisTurn()) throw new AlreadyPlayedException();
        CharacterCard card = board.getCharacterCards().get(posCharacterCard);

        p.spendCoins(card.getPrice());
        card.setGameManager(this);
        card.setPlayerThisTurn(p);
        card.setBoard(board);
        card.setSelectedStudentsFrom(studentGroupFrom);
        card.setSelectedStudentsTo(studentGroupTo);
        card.getCardInfo().getEffect().resolveEffect(card);
        p.setCcActivatedThisTurn(true);
    }

    /**
     * creates report of the game to send to clients
     * @return full current state of the game
     */
    public synchronized CurrentStatus getFullCurrentStatus() {
        CurrentStatus status = new CurrentStatus();
        status.setStatus(0);
        if(expertMode) status.setGameMode("expert");
        else status.setGameMode("simple");
        status.setTurn(turnManager.getTurnStatus());
        GameStatus gs = new GameStatus();
        gs.setMotherNatureIndex(board.getMotherNature().getIslandIndex());
        gs.setArchipelagos(board.getIslandManager().getFullArchipelagosStatus());
        gs.setClouds(board.getCloudsStatus());
        ArrayList<PlayerStatus> ps = new ArrayList<>();
        for(int i=0;i<players.size();i++) {
            PlayerStatus psTemp = new PlayerStatus();
            psTemp.setIndex(i);
            psTemp.setNickName(players.get(i).getNickname());
            if(expertMode) {
                psTemp.setCoins(players.get(i).getCoins());
            }
            psTemp.setTowerColour(players.get(i).getTower().toString());
            psTemp.setNumTowers(players.get(i).getDashboard().getNumTowers());
            psTemp.setStudentsOnEntrance(players.get(i).getDashboard().getEntrance().getStatus());
            psTemp.setStudentsOnHall(players.get(i).getDashboard().getHall().getStatus());
            if(players.get(i).getLastPlayedCard()!=null) psTemp.setLastAssistantCardPlayed(players.get(i).getLastPlayedCard().getInfo().ordinal());
            int numCards = players.get(i).getCards().size();
            boolean[] ac = new boolean[numCards];
            for(int j=0;j<numCards;j++) {
                ac[j] = players.get(i).getCards().get(j).isPlayed();
            }
            psTemp.setAssistantCards(ac);
            ps.add(psTemp);
        }
        gs.setPlayers(ps);
        if(expertMode) {
            status.setGameMode("expert");
            ArrayList<CharacterCardStatus> ccs = new ArrayList<>();
            for(int i=0;i<board.getCharacterCards().size();i++) {
                CharacterCardStatus ccsTemp = new CharacterCardStatus();
                ccsTemp.setIndex(i);
                ccsTemp.setFileName(board.getCharacterCards().get(i).getCardInfo().getFileName());
                ccsTemp.setNoEntryTiles(board.getCharacterCards().get(i).getNoEntryTiles());
                ccsTemp.setCoinOnIt(board.getCharacterCards().get(i).isCoinOnIt());
                ccsTemp.setStudents(board.getCharacterCards().get(i).getStudentsOnCard().getStatus());
                ccs.add(ccsTemp);
            }
            gs.setCharacterCards(ccs);
        } else {
            status.setGameMode("simple");
        }
        gs.setProfessors(board.getProfessorGroup().getStatus());
        status.setGame(gs);
        return status;
    }


    public boolean isGameStarted() {
        return gameStarted;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}
