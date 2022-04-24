package it.polimi.ingsw.model.players;

import it.polimi.ingsw.enumerations.AssistantCardInfo;

public class AssistantCard {
    private AssistantCardInfo info;
    private boolean played;

    public AssistantCard(AssistantCardInfo info){
        this.info = info;
        played = false;
    }

    /**
     * The method gives information about the card's  metadata
     * @return card's metadata
     */
    public AssistantCardInfo getInfo() {
        return info;
    }

    /**
     * The method set information about the card's  metadata
     * @param info card's metadata
     */
    public void setInfo(AssistantCardInfo info) {
        this.info = info;
    }

    /**
     * The method gives information about the card, if it has been player or not
     * @return boolean that shows if the card has already been played
     */
    public boolean isPlayed() {
        return played;
    }

    /**
     * The method set information about the card, if it has been player or not
     * @param played boolean that shows if the card has already been played
     */
    public void setPlayed(boolean played) {
        this.played = played;
    }

}
