package GUIs;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class EditorTileButton extends TileButton {

    private final String STYLE_NORMAL = "-fx-effect: null";
    private final String STYLE_HOVER = "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.2), 60, 0, 0, 0)";
    private final String STYLE_CLICKED = "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.4), 60, 0, 0, 0)";

    public EditorTileButton(String imageName) {
        super(new Image(imageName));

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                setStyle(STYLE_HOVER);
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                setStyle(STYLE_NORMAL);
            }
        });

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                setStyle(STYLE_CLICKED);
            }
        });
    }
}
