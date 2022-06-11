package it.polimi.ingsw.network;

import it.polimi.ingsw.server.model.players.Player;

import java.util.ArrayList;

/**
 * It represents the current status of all the Game during the match
 */
public class GameStatus {
    Integer motherNatureIndex;
    ArrayList<ArchipelagoStatus> archipelagos;
    ArrayList<CloudStatus> clouds;
    ArrayList<CharacterCardStatus> characterCards;
    ArrayList<PlayerStatus> players;
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
    public void setArchipelagos(ArrayList<ArchipelagoStatus> archipelagos) {
        this.archipelagos = archipelagos;
    }

    /**
     * It sets the current Status of all Clouds
     * @param clouds CloudStatus to set
     */
    public void setClouds(ArrayList<CloudStatus> clouds) {
        this.clouds = clouds;
    }

    /**
     * It sets the current Status of all CharacterCards
     * @param characterCards CharacterCardStatus to set
     */
    public void setCharacterCards(ArrayList<CharacterCardStatus> characterCards) {
        this.characterCards = characterCards;
    }

    /**
     * It sets the current Status of all Players
     * @param players PlayerStatus to set
     */
    public void setPlayers(ArrayList<PlayerStatus> players) {
        this.players = players;
    }

    public Integer getMotherNatureIndex() {
        return motherNatureIndex;
    }

    public ArrayList<ArchipelagoStatus> getArchipelagos() {
        return archipelagos;
    }

    public ArrayList<CloudStatus> getClouds() {
        return clouds;
    }

    public ArrayList<CharacterCardStatus> getCharacterCards() {
        return characterCards;
    }

    public ArrayList<PlayerStatus> getPlayers() {
        return players;
    }

    public int[] getProfessors() {
        return professors;
    }

    public void setProfessors(int[] professors) {
        this.professors = professors;
    }

}
