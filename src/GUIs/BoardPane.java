package GUIs;

import GameBoard.GameMap;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Displays a GameMap Object on a Pane.
 */
public class BoardPane extends Pane {

    private GameMap gameMap;
    private final int DELTAX = 33;
    private final int DELTAY = 20;

    /**
     * Constructor for BoardPane. Takes each tile in the gameMap
     * and adds them to the children list of the Pane.
     * @param gameMap The gameMap used to create the Pane graphics.
     */
    public BoardPane(GameMap gameMap){
        super();
        this.gameMap = gameMap;
        for(int j=0; j<gameMap.getTiles()[0].length; j++){
            for(int i=0; i<gameMap.getTiles().length; i++){
                this.getChildren().add(new EditorTileButton(gameMap.getTiles()[i][j].getImageName()));
            }
        }
    }

    @Override
    /**
     * Lays out the tiles of the Game map into a hexogonal grid.
     */
    protected void layoutChildren(){
        ObservableList<Node> boardTiles = this.getChildren();
        int x = DELTAX/2;
        int y = DELTAY/2;

        for(Node n:boardTiles){
            layoutInArea(n,x,y,DELTAX/3-1,DELTAY,0, HPos.CENTER, VPos.CENTER);
            x += DELTAX;
            if(x/DELTAX >= this.gameMap.getDEFAULTWIDTH()) {
                y += 2*DELTAY;
                x = DELTAX/2;
            } else {
                if((x/DELTAX)%2==1){
                    y += DELTAY;
                } else {
                    y -= DELTAY;
                }
            }
        }
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}
