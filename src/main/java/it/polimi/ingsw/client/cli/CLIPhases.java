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
    WIN("GAME ENDED, THERE IS A WINNER!!!"),
    DRAW("GAME ENDED IN A DRAW"),
    P_STUDENT_COLOUR("Select the colour of the student from your entrance (yellow,green,blue,pink,red): "),
    P_ISLAND_INDEX("Select the index of the island (1-12): "),
    P_CCARD_INDEX("Select the index of the card (1-3): "),
    P_CLOUD_INDEX("Select the index of the cloud: "),
    P_MNSHIFTS("Select how many steps Mother Nature must move: "),
    PCC_ISLAND_INDEX("Select the index of the island (1-12): "),
    PCC_STUDENT_COLOUR("Select the colour of the student (yellow,green,blue,pink,red): "),
    PCC_GROUP_ON_CARD("How many students you want to take from this card? (MAX 3): "),
    PCC_GROUP_ON_ENTRANCE("Take the same amount of students from your entrance."),
    PCC_GROUP_ON_HALL("How many students you want to take from your hall? (MAX 2): "),
    PCC_STUDENT_ON_CARD("Select the colour of the student from the card (yellow,green,blue,pink,red): "),
    PCC_STUDENT_ON_ENTRANCE("Select the colour of the student from your entrance (yellow,green,blue,pink,red): "),
    PCC_STUDENT_ON_HALL("Select the colour of the student from your hall (yellow,green,blue,pink,red): "),
    PCC_SUBMIT_GROUP_FROM("Students selected..."),
    PCC_SUBMIT_GROUP_TO("Students selected..."),
    SENDCOMMAND("Sending command to server...");

    private String menuPrompt;
    CLIPhases(String menuPrompt) {
        this.menuPrompt = menuPrompt;
    }

    public String getMenuPrompt() {
        return menuPrompt;
    }

    public void setMenuPrompt(String menuPrompt) {
        this.menuPrompt = menuPrompt;
    }
}
