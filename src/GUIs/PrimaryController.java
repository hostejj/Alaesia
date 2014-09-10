package GUIs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Main controller class for the entire layout.
 */
public class PrimaryController {

    /** Holder of a switchable scene. */
    @FXML
    private StackPane sceneHolder;

    /**
     * Replaces the scene displayed in the scene holder with a new scene.
     *
     * @param node the scene node to be swapped in.
     */
    public void setScene(Node node) {
        sceneHolder.getChildren().setAll(node);
    }

}