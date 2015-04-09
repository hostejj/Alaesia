package GUIs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class PrimaryPane extends Application {
    private TransferModel transferModel = new TransferModel();

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Alaesia");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });

        //setup game icon
        try {
            stage.getIcons().addAll(new Image(new File("Resources/InterfaceImages/IconImage.png").toURI().toString()));
        } catch (Exception e){
            System.err.println(e.toString());
        }

        stage.setScene(
                createScene(
                        loadMainPane()
                )
        );

        stage.show();
    }

    @Override
    public void stop(){
        //clean up any open connections on the close
        if(transferModel.getTransferModel().getHostController() != null){
            if(transferModel.getTransferModel().getHostController().getHostServer() != null) {
                transferModel.getTransferModel().getHostController().endHost();
            }
        }
        if(transferModel.getTransferModel().getJoinController() != null){
            transferModel.getTransferModel().getJoinController().endConnection();
        }
    }

    /**
     * Loads the main fxml layout.
     * Sets up the scene switching Navigator.
     * Loads the first scene into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane mainPane = loader.load(
                getClass().getResourceAsStream(
                        Navigator.PRIMARY
                )
        );

        PrimaryController primaryController = loader.getController();

        Navigator.setMainController(primaryController);
        Navigator.loadScene(Navigator.MAINMENU);

        return mainPane;
    }

    /**
     * Creates the main application scene.
     *
     * @param mainPane the main application layout.
     *
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene( mainPane );

        scene.getStylesheets().setAll(
                getClass().getResource("PrimaryPane.css").toExternalForm()
        );

        return scene;
    }

    public static void main(String args[]){
        launch(args);
    }
}
