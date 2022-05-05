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
    int[] professors;

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

    public Integer getMotherNatureIndex() {
        return motherNatureIndex;
    }

    public ArchipelagoStatus[] getArchipelagos() {
        return archipelagos;
    }

    public CloudStatus[] getClouds() {
        return clouds;
    }

    public CharacterCardStatus[] getCharacterCards() {
        return characterCards;
    }

    public PlayerStatus[] getPlayers() {
        return players;
    }

    public int[] getProfessors() {
        return professors;
    }

    public void setProfessors(int[] professors) {
        this.professors = professors;
    }
}
