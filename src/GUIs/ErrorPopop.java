package GUIs;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public abstract class ErrorPopop {
    /**
     * Displays a popup to the user with the reason that an action
     * is currently invalid.
     */
    public static void newPopup(String message){
        final Stage popup = new Stage();
        GridPane popupGrid = new GridPane();
        Label messageL = new Label(message);
        Button okay = new Button("Okay");
        popup.getIcons().setAll(new Image(new File("Resources/InterfaceImages/IconImage.png").toURI().toString()));

        popupGrid.setPadding(new Insets(10,10,10,10));
        messageL.setWrapText(true);
        okay.setPrefWidth(Integer.MAX_VALUE);
        okay.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popup.close();
            }
        });

        popupGrid.add(messageL,0,0);
        popupGrid.add(okay,0,1);

        Scene scene = new Scene(popupGrid, 300, 130);
        popup.setScene(scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.show();
    }
}
