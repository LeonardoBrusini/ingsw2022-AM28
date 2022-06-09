package it.polimi.ingsw.client.gui;

public enum GUIScene {
    TITLE_SCREEN("titleScreen2.fxml"),
    USERNAME("usernameScene.fxml"),
    GAME_PARAMETERS("gameParametersScene.fxml"),
    PLANNING_2_EXPERT("PlanningSceneExpert1080.fxml"),
    PLANNING_3_EXPERT("PlanningSceneExpertThreePlayers1080.fxml"),
    PLANNING_2_SIMPLE("PlanningSceneSimple1080.fxml"),
    PLANNING_3_SIMPLE("PlanningSceneSimpleThreePlayers1080.fxml"),
    ACTION_2_EXPERT("ActionSceneExpert1080.fxml"),
    ACTION_3_EXPERT("ActionSceneExpertThreePlayers.fxml"),
    ACTION_2_SIMPLE("ActionSceneSimple1080.fxml"),
    ACTION_3_SIMPLE("ActionScene3Simple1080.fxml");

    private String fileName;
    GUIScene(String s) {
        fileName = s;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
