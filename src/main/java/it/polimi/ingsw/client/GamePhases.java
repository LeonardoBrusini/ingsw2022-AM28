package it.polimi.ingsw.client;

public enum GamePhases {
    USERNAME("Insert your username: ",""),
    NUM_PLAYERS("Insert the number of players",""),
    GAME_MODE("Select the game mode, either expert or simple: ",""),
    PRE_WAIT("Waiting for other players to connect...",""),
    PLANNING_COMMAND("Select assistant card to play (1-10): ",""),
    ACTION_COMMAND("1) Move Student on an Island\n2) Move Student on your Hall\n3) Move mother Nature\n4) Take students from a cloud",""),
    //in ACTION_COMMAND 5) Activate Character Card effect ONLY IF EXPERT GAME MODE
    WAIT("Not your turn, wait for other players...","Not your turn, wait for other players!"),
    WIN("GAME ENDED, THERE IS A WINNER!!!","GAME ENDED, THERE IS A WINNER!!!"),
    DRAW("GAME ENDED IN A DRAW","GAME ENDED IN A DRAW"),
    P_STUDENT_COLOUR("Select the colour of the student from your entrance (yellow,green,blue,pink,red): ","Select a student from your entrance!"),
    P_ISLAND_INDEX("Select the index of the island (1-12): ","Select one of the island!"),
    P_CCARD_INDEX("Select the index of the card (1-3): ","Select one of the cards!"),
    P_CLOUD_INDEX("Select the index of the cloud: ","Select one of the clouds!"),
    P_MNSHIFTS("Select how many steps Mother Nature must move: ","Select an island!"),
    PCC_ISLAND_INDEX("Select the index of the island (1-12): ","Select one of the island!"),
    PCC_STUDENT_COLOUR("Select the colour of the student (yellow,green,blue,pink,red): ","Select one of the colours"),
    PCC_GROUP_ON_CARD("How many students you want to take from this card? (MAX 3): ","How many students you want to swap?"),
    PCC_GROUP_ON_ENTRANCE("Take the same amount of students from your entrance.","Take the same amount of students from your entrance."),
    PCC_GROUP_ON_HALL("How many students you want to take from your hall? (MAX 2): ","How many students you want to swap?"),
    PCC_STUDENT_ON_CARD("Select the colour of the student from the card (yellow,green,blue,pink,red): ","Select a student from your card!"),
    PCC_STUDENT_ON_ENTRANCE("Select the colour of the student from your entrance (yellow,green,blue,pink,red): ","Select a student from your entrance!"),
    PCC_STUDENT_ON_HALL("Select the colour of the student from your hall (yellow,green,blue,pink,red): ","Select a student from your hall!"),
    PCC_SUBMIT_GROUP_FROM("Students selected...","Students selected..."),
    PCC_SUBMIT_GROUP_TO("Students selected...","Students selected..."),
    SENDCOMMAND("Sending command to server...","Sending command to server..."),
    GUI_STUDENT_ON_CARD("","Select a student from the card"),
    GUI_GROUPS_CARD_ENTRANCE("","Select up to 3 students from the card and the same number from your entrance."),
    GUI_GROUPS_ENTRANCE_HALL("","Select up to 2 students from the hall and the same number from your entrance.");


    private String menuPrompt,GUIPrompt;
    GamePhases(String menuPrompt, String GUIPrompt) {
        this.menuPrompt = menuPrompt;
        this.GUIPrompt = GUIPrompt;
    }

    public String getMenuPrompt() {
        return menuPrompt;
    }
    public void setMenuPrompt(String menuPrompt) {
        this.menuPrompt = menuPrompt;
    }
    public String getGUIPrompt() {
        return GUIPrompt;
    }
    public void setGUIPrompt(String GUIPrompt) {
        this.GUIPrompt = GUIPrompt;
    }
}
