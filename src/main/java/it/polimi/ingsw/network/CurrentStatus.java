package it.polimi.ingsw.network;

/**
 * It represents the current status of the turns and winner
 */
public class CurrentStatus {
    int status;
    String errorMessage;
    String winner;
    TurnStatus turn;
    GameStatus game;

    public void setStatus(int status) {
        this.status = status;
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

}
