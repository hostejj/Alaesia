package GUIs;

import GameBoard.Tile;

public interface BoardPaneObserver {

    //method to update the observer, used by subject
    public void updateBoard(Tile tile);
}
