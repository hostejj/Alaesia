package EditorPane;

import GUIs.TileButton;
import GameBoard.Tile;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class TileListPaneButton extends TileButton {

    private TileListPane observer;

    public TileListPaneButton(Tile t, TileListPane tileListPane) {
        super();
        this.tile = t;

        try {
            this.setImage(new Image(t.getImageName()));
        } catch (Exception e){
            System.err.println("Could not find the tile image file. ");
        }

        observer = tileListPane;

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
                observer.update(tile);
            }
        });
    }
}
