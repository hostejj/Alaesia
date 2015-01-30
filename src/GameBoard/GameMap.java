package GameBoard;

import java.io.Serializable;
import java.util.Arrays;

public class GameMap implements Serializable {
    private final int DEFAULTHEIGHT = 14;
    private final int DEFAULTWIDTH = 19;
    private final String DEFAULTTILEDIR = "Resources/TileImages/grass.png";

    private Tile[][] tiles;

    public GameMap(){
        tiles = new Tile[DEFAULTWIDTH][DEFAULTHEIGHT];
        for(int j=0; j<tiles[0].length; j++){
            for(int i=0; i<tiles.length; i++){
                tiles[i][j] = new Tile(DEFAULTTILEDIR, i, j);
            }
        }
    }

    public GameMap(int height, int width){
        tiles = new Tile[width][height];
        for(int j=0; j<tiles[0].length; j++){
            for(int i=0; i<tiles.length; i++){
                tiles[i][j] = new Tile(DEFAULTTILEDIR, i, j);
            }
        }
    }

    public GameMap(GameMap gameMap){
        tiles = new Tile[gameMap.getTiles().length][gameMap.getTiles()[0].length];
        for(int j=0; j<tiles[0].length; j++){
            for(int i=0; i<tiles.length; i++){
                tiles[i][j] = new Tile(gameMap.getTiles()[i][j]);
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

    public int getWidth(){ return tiles.length; }
    public int getHeight(){ return tiles[0].length; }

    @Override
    public String toString() {
        String tileString = "";
        for(Tile[] y: tiles){
            for (Tile x : y) {
                tileString += x.toString();
            }
        }
        return "GameMap{" +
                "tiles=" + tileString +
                '}';
    }
}