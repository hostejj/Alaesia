package EditorPane;

import GUIs.TileButton;
import GameBoard.Tile;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class TileListPaneButton extends TileButton {

    private TileListPane observer;

    public TileListPaneButton(Tile t, TileListPane tileListPane) {
        super();
        this.tile = t;

        try {
            tileImage = new ImageView(new Image(new File(t.getImageName()).toURI().toString()));
            this.getChildren().add(tileImage);
        } catch (Exception iae){
            System.err.println("Could not find the tile image file. ");
        }

        observer = tileListPane;

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                observer.update(tile);
            }
        });
    }
}
