package EditorPane;

import GameBoard.Tile;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.File;

public class TileListPaneButton extends StackPane {

    private TileListPane observer;
    private Tile tile;
    private ImageView tileImage;

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
