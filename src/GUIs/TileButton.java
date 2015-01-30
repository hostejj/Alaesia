package GUIs;

import GameBoard.Tile;
import javafx.scene.image.ImageView;

public abstract class TileButton extends ImageView {

    protected Tile tile;

    protected final String STYLE_NORMAL = "-fx-effect: null";
    protected final String STYLE_HOVER = "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.2), 60, 0, 0, 0)";
    protected final String STYLE_CLICKED = "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.4), 60, 0, 0, 0)";

    public TileButton() {
        super();
    }

    public Tile getTile() {
        return tile;
    }
}
