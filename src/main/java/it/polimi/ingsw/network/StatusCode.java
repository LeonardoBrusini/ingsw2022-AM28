package it.polimi.ingsw.network;

public enum StatusCode {
    ALREADYLOGGED(101,"Already logged with this name."),
    FULL_LOBBY(102,"Server has already reached the maximum number of players."),
    GAMESTARTED(103,"Game has already started."),
    KICKEDOUT(104, "Host chose to play a game of 2 players, you were the third."),
    ILLEGALARGUMENT(201,"Please correct parameters"),
    ALREADYPLAYEDAC(202,"You already played this card! Choose another one."),
    ALREADYPLAYEDCC(203,"You already playerd a character card! Wait until next turn"),
    NOTENOUGHCOINS(204, "You don't have enough coins to play this card."),
    FULLHALL(205,"Hall is full."),
    NOSTUDENTS(206,"Not enough students."),
    WRONGTURN(207,"You can only play during your turn"),
    WRONGPHASE(208,"You can't do this move on the current phase of the turn");

    private final int statusCode;
    private final String errorMessage;

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    StatusCode(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
    }