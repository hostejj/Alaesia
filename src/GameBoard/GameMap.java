package GameBoard;

import java.io.Serializable;

public class GameMap implements Serializable {
    private final int DEFAULTHEIGHT = 14;
    private final int DEFAULTWIDTH = 19;

    private Tile[][] tiles;

    public GameMap(){
        tiles = new Tile[DEFAULTHEIGHT][DEFAULTWIDTH];
        for(int j=0; j<tiles[0].length; j++){
            for(int i=0; i<tiles.length; i++){
                tiles[i][j] = new Tile("Resources/TileImages/1.png", i, j);
            }
        }
    }

    public GameMap(int height, int width){
        tiles = new Tile[height][width];
        for(int j=0; j<tiles[0].length; j++){
            for(int i=0; i<tiles.length; i++){
                tiles[i][j] = new Tile("Resources/TileImages/1.png", i, j);
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getDEFAULTWIDTH() {
        return DEFAULTWIDTH;
    }

    public int getDEFAULTHEIGHT() {
        return DEFAULTHEIGHT;
    }
}