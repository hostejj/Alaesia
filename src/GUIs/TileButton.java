package GUIs;

import GameBoard.Tile;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public abstract class TileButton extends StackPane {

    protected Tile tile;
    protected ImageView tileImage;
    protected ArrayList<ImageView> shades = new ArrayList<ImageView>();
    protected ImageView unitImage;

    public TileButton() {
        super();
    }

    public Tile getTile() {
        return tile;
    }

    public ImageView getTileImage() { return tileImage; }

    public ImageView getUnitImage() {
        return unitImage;
    }

    public void setUnitImage(ImageView unitImage) {
        this.unitImage = unitImage;
    }
}
