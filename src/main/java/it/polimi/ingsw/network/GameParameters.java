package it.polimi.ingsw.network;

public class GameParameters {
    private int numPlayers;
    private String gameMode;

    public int getNumPlayers() {
        return numPlayers;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
