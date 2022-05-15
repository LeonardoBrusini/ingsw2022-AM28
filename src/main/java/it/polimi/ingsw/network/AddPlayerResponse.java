package it.polimi.ingsw.network;

public class AddPlayerResponse {
    private int statusCode;
    private Boolean first;
    private String errorMessage;

    public int getStatus() {
        return statusCode;
    }

    public void setStatus(int status) {
        this.statusCode = status;
    }

    public Boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
