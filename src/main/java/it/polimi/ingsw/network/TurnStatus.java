package it.polimi.ingsw.network;

/**
 * It represents the current status of the Turn during the match
 */
public class TurnStatus {
    private Integer player;
    private String phase;

    public Integer getPlayer(){
        return this.player;
    }

    public String getPhase(){
        return this.phase;
    }

    public void setPlayer(Integer player){
        this.player=player;
    }

    public void setPhase(String phase){
        this.phase=phase;
    }

}
