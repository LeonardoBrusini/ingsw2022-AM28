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

    public void setMotherNatureIndex(Integer motherNatureIndex) {
        this.motherNatureIndex = motherNatureIndex;
    }

    public void setArchipelagos(ArchipelagoStatus[] archipelagos) {
        this.archipelagos = archipelagos;
    }

    public void setClouds(CloudStatus[] clouds) {
        this.clouds = clouds;
    }

    public void setCharacterCards(CharacterCardStatus[] characterCards) {
        this.characterCards = characterCards;
    }

    public void setPlayers(PlayerStatus[] players) {
        this.players = players;
    }
}
