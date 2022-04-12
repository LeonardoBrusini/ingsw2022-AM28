package it.polimi.ingsw.model.players;

public class AssistantCard {
    private AssistantCardInfo info;
    private boolean played;

    public AssistantCard(AssistantCardInfo info){
        this.info = info;
        played = false;
    }

    public AssistantCardInfo getInfo() {
        return info;
    }

    public void setInfo(AssistantCardInfo info) {
        this.info = info;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

}
