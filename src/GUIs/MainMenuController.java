package GUIs;

import javafx.application.Platform;

public class MainMenuController {
    public void handleSinglePlayerButtonAction() {
        Navigator.loadScene(Navigator.STAGING);
    }
    public void handleHostButtonAction() { Navigator.loadScene(Navigator.HOST); }
    public void handleJoinButtonAction() { Navigator.loadScene(Navigator.JOIN); }
    public void handleEditorButtonAction() {
        Navigator.loadScene(Navigator.EDITOR);
    }
    public void handleExitButtonAction() {
        Platform.exit();
    }
}