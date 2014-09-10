package GUIs;

import javafx.application.Platform;

public class MainMenuController {

    public void handleExitButtonAction() {
        Platform.exit();
    }

    public void handlePreferencesButtonAction() {
    }

    public void handleEditorButtonAction() {
        Navigator.loadScene(Navigator.EDITOR);
    }

    public void handleMultiplayerButtonAction() {
    }

    public void handleSinglePlayerButtonAction() {
    }
}
