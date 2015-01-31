package GameBoard;

import GameConcepts.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The GameMap is the bean class which houses all of the game data on units and their location. It contains
 * utility functions to locate specific units. In addition the GameMap class stores the information related to
 * initial unit placement in a game.
 */
public class GameMap implements Serializable {
    private final String DEFAULTNAME = "Untitled Map";

    private final int DEFAULTWIDTH = 20;
    private final int MINWIDTH = 10;
    private final int MAXWIDTH = 60;

    private final int DEFAULTHEIGHT = 15;
    private final int MINHEIGHT = 6;
    private final int MAXHEIGHT = 50;

    private final int DEFAULTPLAYERS = 2;
    private final int MINPLAYERS = 2;
    private final int MAXPLAYERS = 4;

    private String mapName;
    private Tile[][] tiles;
    private int width;
    private int height;
    private ArrayList<Unit> units;
    private Integer maxPlayers;
    private ArrayList<ArrayList<Tile>> startLocs;

    /**
     * Default constructor for GameMap class which creates a unit-less map of default tiles
     * and no defined starting locations.
     */
    public GameMap(){
        mapName = DEFAULTNAME;

        tiles = new Tile[DEFAULTWIDTH][DEFAULTHEIGHT];
        for(int j=0; j<tiles[0].length; j++){
            for(int i=0; i<tiles.length; i++){
                tiles[i][j] = new Tile(i, j);
            }
        }
        width = DEFAULTWIDTH;
        height = DEFAULTHEIGHT;
        maxPlayers = DEFAULTPLAYERS;

        units = new ArrayList<Unit>();

        startLocs = new ArrayList<ArrayList<Tile>>();
        for(int i = 0; i < maxPlayers; i++){
            startLocs.add(new ArrayList<Tile>());
        }
    }

    /**
     * Constructor for GameMap which creates a unit-less map of a specified size of default tiles and maximum number of players.
     * Starting locations for players are not defined.
     * @param mapName The name of the map.
     * @param width The width of the map in tiles.
     * @param height The height of the map in tiles.
     * @param maxPlayers The maximum number of players the map will support.
     */
    public GameMap(String mapName, int width, int height, int maxPlayers){
        this.mapName = mapName;

        tiles = new Tile[width][height];
        for(int j=0; j<tiles[0].length; j++){
            for(int i=0; i<tiles.length; i++){
                tiles[i][j] = new Tile(i, j);
            }
        }

        this.width = width;
        this.height = height;
        this.maxPlayers = maxPlayers;

        units = new ArrayList<Unit>();

        startLocs = new ArrayList<ArrayList<Tile>>();
        for(int i = 0; i < this.maxPlayers; i++){
            startLocs.add(new ArrayList<Tile>());
        }
    }

    /**
     * Copy constructor for GameMap
     * @param gameMap The GameMap to be copied.
     */
    public GameMap(GameMap gameMap){
        this.mapName = gameMap.mapName;

        this.tiles = new Tile[gameMap.width][gameMap.height];
        for(int j=0; j<this.tiles[0].length; j++){
            for(int i=0; i<this.tiles.length; i++){
                this.tiles[i][j] = new Tile(gameMap.getTiles()[i][j]);
            }
        }

        this.width = gameMap.width;
        this.height = gameMap.height;
        this.maxPlayers = gameMap.maxPlayers;

        this.units = new ArrayList<Unit>();
        for(Unit u: gameMap.getUnits()){
            units.add(new Unit(u));
        }

        this.startLocs = new ArrayList<ArrayList<Tile>>();
        for(int i = 0; i < this.maxPlayers; i++){
            this.startLocs.add(new ArrayList<Tile>());
            for (Tile t: gameMap.startLocs.get(i)){
                this.startLocs.get(i).add(new Tile(t));
            }
        }
    }

    public String getDEFAULTNAME() {
        return DEFAULTNAME;
    }

    public int getDEFAULTWIDTH() {
        return DEFAULTWIDTH;
    }

    public int getMINWIDTH() {
        return MINWIDTH;
    }

    public int getMAXWIDTH() {
        return MAXWIDTH;
    }

    public int getDEFAULTHEIGHT() {
        return DEFAULTHEIGHT;
    }

    public int getMINHEIGHT() {
        return MINHEIGHT;
    }

    public int getMAXHEIGHT() {
        return MAXHEIGHT;
    }

    public int getDEFAULTPLAYERS() {
        return DEFAULTPLAYERS;
    }

    public int getMINPLAYERS() {
        return MINPLAYERS;
    }

    public int getMAXPLAYERS() {
        return MAXPLAYERS;
    }

    public String getMapName() {
        return mapName;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public ArrayList<ArrayList<Tile>> getStartLocs() {
        return startLocs;
    }

    /**
     * Builds a string representation of the current class instance.
     * @return The current instance in String notation.
     */
    @Override
    public String toString() {
        String tileString = "";
        for(Tile[] y: tiles){
            for (Tile x : y) {
                tileString += x.toString();
            }
            tileString += "\t";
        }

        String unitString = "";
        for(Unit u: units){
            unitString += u.toString();
        }

        String slString = "";
        for (ArrayList<Tile> staLoc: startLocs){
            for (Tile t: staLoc){
                slString += t.toString();
            }
            slString += "\t";
        }

        return "GameMap{" +
                "mapName='" + mapName + '\'' +
                ", tiles=" + tileString +
                ", units=" + unitString +
                ", maxPlayers=" + maxPlayers +
                ", startLocs=" + slString +
                '}';
    }
}