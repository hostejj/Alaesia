package GUIs;

import GameBoard.GameMap;
import GameBoard.Tile;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Displays a GameMap Object on a Pane.
 */
public class BoardPane extends Pane {

    private GameMap gameMap;
    private BoardPaneObserver boardPaneObserver;
    private BoardPaneButton[][] boardPaneButtons;
    private final int DELTAX = 33;
    private final int DELTAY = 20;

    /**
     * Constructor for BoardPane. Takes each tile in the gameMap
     * and adds them to the children list of the Pane.
     * @param gameMap The gameMap used to create the Pane graphics.
     */
    public BoardPane(GameMap gameMap, BoardPaneObserver boardPaneObserver){
        super();
        this.gameMap = gameMap;
        this.boardPaneObserver = boardPaneObserver;
        this.boardPaneButtons = new BoardPaneButton[gameMap.getWidth()][gameMap.getHeight()];
        for(int j=0; j<gameMap.getHeight(); j++){
            for(int i=0; i<gameMap.getWidth(); i++){
                boardPaneButtons[i][j] = new BoardPaneButton(gameMap.getMapCells()[i][j], this);
                this.getChildren().add(boardPaneButtons[i][j]);
            }
        }
        setMinSize(DELTAX*(gameMap.getWidth())+(DELTAX/3-2), (2*DELTAY)*(gameMap.getHeight())+DELTAY);
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
            if(x/DELTAX >= this.gameMap.getWidth()) {
                if((x/DELTAX)%2==1){
                    y += 2*DELTAY;
                } else {
                    y += DELTAY;
                }
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

    public BoardPaneButton[][] getBoardPaneButtons() {
        return boardPaneButtons;
    }

    public void update(BoardPaneButton boardPaneButton, Integer click) {
        boardPaneObserver.updateBoard(boardPaneButton, click);
    }
}
