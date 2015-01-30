package GUIs;

import GameBoard.Tile;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class BoardPaneButton extends TileButton {

    private BoardPane observer;

    public BoardPaneButton(Tile t, BoardPane boardPane) {
        super();
        this.tile = new Tile(t);

        try {
            this.setImage(new Image(t.getImageName()));
        } catch (Exception e){
            System.err.println("Could not find the tile image file. ");
        }

        observer = boardPane;

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
