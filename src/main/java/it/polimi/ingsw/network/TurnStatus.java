package it.polimi.ingsw.network;

/**
 * It represents the current status of the Turn during the match
 */
public class TurnStatus {
    private int player;
    private String phase;

    public int getPlayer(){
        return this.player;
    }

    public String getPhase(){
        return this.phase;
    }

    public void setPlayer(int player){
        this.player=player;
    }

    public void setPhase(String phase){
        this.phase=phase;
    }

}
