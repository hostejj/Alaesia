package GUIs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

/**
 * Utility class for controlling navigation between vistas.
 *
 * All methods on the navigator are static to facilitate
 * simple access from anywhere in the application.
 */
public class Navigator {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String EDITOR = "../EditorPane/EditorPane.fxml";
    public static final String MAINMENU = "MainMenu.fxml";
    public static final String PRIMARY = "PrimaryPane.fxml";
    public static final String STAGING = "../StagingPane/StagingPane.fxml";
    public static final String SPGAME = "../SPGamePane/SPGamePane.fxml";

    /** The main application layout controller. */
    private static PrimaryController primaryController;

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param primaryController the main application layout controller.
     */
    public static void setMainController(PrimaryController primaryController) {
        Navigator.primaryController = primaryController;
    }

    /**
     * Loads the scene specified by the fxml file into the
     * sceneHolder pane of the main application layout.
     *
     * Previously loaded scene for the same fxml file are not cached.
     * The fxml is loaded anew and a new scene hierarchy generated
     * every time this method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     * cache FXMLLoaders
     * cache loaded scenes, so they can be recalled or reused
     * allow a user to specify scene reuse or new creation
     * allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadScene(String fxml) {
        try {
            primaryController.setScene( (Node) FXMLLoader.load( Navigator.class.getResource( fxml ) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
