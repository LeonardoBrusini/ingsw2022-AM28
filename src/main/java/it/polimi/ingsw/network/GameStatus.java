package it.polimi.ingsw.network;

/**
 * It represents the current status of all the Game during the match
 */
public class GameStatus {
    Integer motherNatureIndex;
    ArchipelagoStatus[] archipelagos;
    CloudStatus[] clouds;
    CharacterCardStatus[] characterCards;
    PlayerStatus[] players;
    boolean[] professors;

    /**
     * It sets the MotherNature index to send
     * @param motherNatureIndex The index of the island on witch MotherNature is
     */
    public void setMotherNatureIndex(Integer motherNatureIndex) {
        this.motherNatureIndex = motherNatureIndex;
    }

    /**
     * It sets the current Status of all Archipelagos
     * @param archipelagos ArchipelagosStatus to set
     */
    public void setArchipelagos(ArchipelagoStatus[] archipelagos) {
        this.archipelagos = archipelagos;
    }

    /**
     * It sets the current Status of all Clouds
     * @param clouds CloudStatus to set
     */
    public void setClouds(CloudStatus[] clouds) {
        this.clouds = clouds;
    }

    /**
     * It sets the current Status of all CharacterCards
     * @param characterCards CharacterCardStatus to set
     */
    public void setCharacterCards(CharacterCardStatus[] characterCards) {
        this.characterCards = characterCards;
    }

    /**
     * It sets the current Status of all Players
     * @param players PlayerStatus to set
     */
    public void setPlayers(PlayerStatus[] players) {
        this.players = players;
    }

    /**
     * It sets if the Player can benefit from the professor of that colour
     * @param pos the position of the professor's colour in the enumeration
     * @param professor boolean to set true if the player can use the professor in the influence's computation
     */
    public void setProfessorsOfAColour(int pos, boolean professor){
        this.professors[pos] = professor;
    }

    /**
     * To set the professors
     * @param prof professors to set
     */
    public void setProfessors(boolean[] prof){
        this.professors=prof;
    }

}
