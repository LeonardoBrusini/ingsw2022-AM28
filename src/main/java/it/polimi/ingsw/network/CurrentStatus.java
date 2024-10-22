package it.polimi.ingsw.network;

/**
 * It represents the current status of the turns and winner
 */
public class CurrentStatus {
    private String lastCommand;
    private int statusCode;
    private Integer playerID;
    private String errorMessage;
    private String winner;
    private TurnStatus turn;
    private GameStatus game;
    private String gameMode;

    public void setStatus(int status) {
        this.statusCode = status;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setTurn(TurnStatus turn) {
        this.turn = turn;
    }

    public void setGame(GameStatus game) {
        this.game = game;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getStatus() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getWinner() {
        return winner;
    }

    public TurnStatus getTurn() {
        return turn;
    }

    public GameStatus getGame() {
        return game;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public String getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand(String lastCommand) {
        this.lastCommand = lastCommand;
    }
}
