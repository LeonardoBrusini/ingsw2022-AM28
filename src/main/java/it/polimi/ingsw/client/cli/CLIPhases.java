package it.polimi.ingsw.client.cli;

public enum CLIPhases {
    USERNAME("Insert your username: "),
    NUM_PLAYERS("Insert the number of players"),
    GAME_MODE("Select the game mode, either expert or simple: "),
    PRE_WAIT("Waiting for other players to connect..."),
    PLANNING_COMMAND("Select assistant card to play (1-10): "),
    ACTION_COMMAND("1) Move Student on an Island\n2) Move Student on your Hall\n3) Move mother Nature\n4) Take students from a cloud"),
    //in ACTION_COMMAND 5) Activate Character Card effect ONLY IF EXPERT GAME MODE
    WAIT("Not your turn, wait for other players..."),
    WIN("WINNER!!!"),
    DRAW("GAME ENDED IN A DRAW"),
    P_STUDENT_COLOUR("Select the colour of the student: "),
    P_ISLAND_INDEX("Select the index of the island (1-12): "),
    P_CCARD_INDEX("Select the index of the card (1-3): "),
    P_CLOUD_INDEX("Select the index of the cloud: "),
    P_MNSHIFTS("Select how many steps Mother Nature must move: "),
    PCC_ISLAND_INDEX("Select the index of the island (1-12): "),
    PCC_STUDENT_COLOUR("Select the colour of the student: "),
    PCC_SFROM("Select the colour of the student: "),
    PCC_STO("Select the colour of the student: "),
    SENDCOMMAND("Sending command to server...");

    private String menuPrompt;
    CLIPhases(String menuPrompt) {
        this.menuPrompt = menuPrompt;
    }

    public String getMenuPrompt() {
        return menuPrompt;
    }
}
