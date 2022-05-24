package it.polimi.ingsw.client.gui;

public enum GUIScene {
    TITLE_SCREEN("titleScreen.fxml"),
    USERNAME("usernameScene.fxml"),
    GAME_PARAMETERS("gameParametersScene.fxml"),
    PLANNING_2_EXPERT("planning2ExpertScene.fxml"),
    PLANNING_3_EXPERT("planning3ExpertScene.fxml"),
    PLANNING_2_SIMPLE("planning2SimpleScene.fxml"),
    PLANNING_3_SIMPLE("planning3SimpleScene.fxml"),
    ACTION_2_EXPERT("action2ExpertScene.fxml"),
    ACTION_3_EXPERT("action3ExpertScene.fxml"),
    ACTION_2_SIMPLE("action2SimpleScene.fxml"),
    ACTION_3_SIMPLE("action3SimpleScene.fxml");

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
