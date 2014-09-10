package GUIs;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;

public class ImageButton extends Button {

    private final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 1 1 1 1;";
    private final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 2 0 0 2;";
    private final String STYLE_HOVERING = "-fx-background-color: transparent; -fx-color: #FFFFFF; -fx-opacity: 0.8";

    public ImageButton(String imageurl) {
        setGraphic(new ImageView(new Image(imageurl)));
        setStyle(STYLE_NORMAL);

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.err.println("adsfa");
                setStyle(STYLE_PRESSED);
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle(STYLE_NORMAL);
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setStyle(STYLE_HOVERING);
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setStyle(STYLE_NORMAL);
            }
        });
    }
}