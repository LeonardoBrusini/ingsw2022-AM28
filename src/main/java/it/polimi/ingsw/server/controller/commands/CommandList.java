package it.polimi.ingsw.server.controller.commands;

public enum CommandList {
    MOVETOISLAND(new MoveToIslandCommand()),
    MOVETOHALL(new MoveToHallCommand()),
    MOVEMOTHERNATURE(new MoveMotherNatureCommand()),
    PLAYASSISTANTCARD(new PlayAssistantCardCommand()),
    PLAYCHARACTERCARD(new PlayCharacterCardCommand()),
    TAKEFROMCLOUD(new TakeFromCloudCommand());
    private final CommandStrategy cmd;
    CommandList(CommandStrategy cmd) {
        this.cmd = cmd;
    }
    public CommandStrategy getCmd() {
        return cmd;
    }
}
