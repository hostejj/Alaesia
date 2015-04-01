package GameBoard;

import GameConcepts.Unit;
import jdk.internal.org.objectweb.asm.tree.InnerClassNode;

import javax.crypto.Mac;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

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
    private MapCell[][] mapCells;
    private int width;
    private int height;
    private ArrayList<Unit> units;
    private Integer maxPlayers;
    private ArrayList<ArrayList<MapCell>> startLocs;

    /**
     * Default constructor for GameMap class which creates a unit-less map of default tiles
     * and no defined starting locations.
     */
    public GameMap(){
        mapName = DEFAULTNAME;

        mapCells = new MapCell[DEFAULTWIDTH][DEFAULTHEIGHT];
        for(int j=0; j<mapCells[0].length; j++){
            for(int i=0; i<mapCells.length; i++){
                mapCells[i][j] = new MapCell(new Tile(i,j));
            }
        }
        width = DEFAULTWIDTH;
        height = DEFAULTHEIGHT;
        maxPlayers = DEFAULTPLAYERS;

        units = new ArrayList<Unit>();

        startLocs = new ArrayList<ArrayList<MapCell>>();
        for(int i = 0; i < maxPlayers; i++){
            startLocs.add(new ArrayList<MapCell>());
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

        mapCells = new MapCell[width][height];
        for(int j=0; j<mapCells[0].length; j++){
            for(int i=0; i<mapCells.length; i++){
                mapCells[i][j] = new MapCell(new Tile(i, j));
            }
        }

        this.width = width;
        this.height = height;
        this.maxPlayers = maxPlayers;

        units = new ArrayList<Unit>();

        startLocs = new ArrayList<ArrayList<MapCell>>();
        for(int i = 0; i < this.maxPlayers; i++){
            startLocs.add(new ArrayList<MapCell>());
        }
    }

    /**
     * Copy constructor for GameMap
     * @param gameMap The GameMap to be copied.
     */
    public GameMap(GameMap gameMap){
        this.mapName = gameMap.mapName;

        this.mapCells = new MapCell[gameMap.width][gameMap.height];
        for(int j=0; j<this.mapCells[0].length; j++){
            for(int i=0; i<this.mapCells.length; i++){
                this.mapCells[i][j] = gameMap.getMapCells()[i][j];
            }
        }

        this.width = gameMap.width;
        this.height = gameMap.height;
        this.maxPlayers = gameMap.maxPlayers;

        this.units = new ArrayList<Unit>();
        for(Unit u: gameMap.getUnits()){
            units.add(new Unit(u));
        }

        this.startLocs = new ArrayList<ArrayList<MapCell>>();
        for(int i = 0; i < this.maxPlayers; i++){
            this.startLocs.add(new ArrayList<MapCell>());
            for (MapCell mc: gameMap.startLocs.get(i)){
                this.startLocs.get(i).add(mc);
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

    public MapCell[][] getMapCells() {
        return mapCells;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public ArrayList<ArrayList<MapCell>> getStartLocs() {
        return startLocs;
    }

    public MapCell locateUnit(Unit unit){
        for (MapCell[] mca: mapCells) {
            for (MapCell mc : mca) {
                if (mc.getUnit() == unit) {
                    return mc;
                }
            }
        }
        return null;
    }

    public ArrayList<MapCell> getNeighboringCells(MapCell mc, Integer iterations){
        ArrayList<MapCell> intermediary = new ArrayList<MapCell>();
        ArrayList<MapCell> temp = new ArrayList<MapCell>(); // so no concurrent modification exception
        ArrayList<MapCell> neighbors = new ArrayList<MapCell>();
        intermediary.add(mc);
        Integer radius = 1;

        for (int i = 1; i <= iterations; i++) {
            for(MapCell mapCell: intermediary) {
                Integer cx = mapCell.getTile().getX();
                Integer cy = mapCell.getTile().getY();

                //finds neighbors one square out
                for (int y = cy - radius; y <= cy + radius; y++) {
                    for (int x = cx - radius; x <= cx + radius; x++) {
                        if ((x >= 0) && (y >= 0) && (x < width) && (y < height) && (!((x == cx) && (y == cy)))) {
                            if (cx % 2 == 1) {
                                if (x % 2 == 1) {
                                    if ((y >= cy - radius) && (y <= (cy + radius))) {
                                        if(i == iterations){
                                            if(!intermediary.contains(mapCells[x][y])){
                                                neighbors.add(mapCells[x][y]);
                                            }
                                        } else {
                                            if(!intermediary.contains(mapCells[x][y])){
                                                temp.add(mapCells[x][y]);
                                            }
                                        }
                                    }
                                } else {
                                    if ((y > cy - radius) && (y <= (cy + radius))) {
                                        if(i == iterations){
                                            if(!intermediary.contains(mapCells[x][y])){
                                                neighbors.add(mapCells[x][y]);
                                            }
                                        } else {
                                            if(!intermediary.contains(mapCells[x][y])){
                                                temp.add(mapCells[x][y]);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (x % 2 == 1) {
                                    if ((y >= cy - radius) && (y < (cy + radius))) {
                                        if(i == iterations){
                                            if(!intermediary.contains(mapCells[x][y])){
                                                neighbors.add(mapCells[x][y]);
                                            }
                                        } else {
                                            if(!intermediary.contains(mapCells[x][y])){
                                                temp.add(mapCells[x][y]);
                                            }
                                        }
                                    }
                                } else {
                                    if ((y >= (cy - radius)) && (y <= (cy + radius))) {
                                        if(i == iterations){
                                            if(!intermediary.contains(mapCells[x][y])){
                                                neighbors.add(mapCells[x][y]);
                                            }
                                        } else {
                                            if(!intermediary.contains(mapCells[x][y])){
                                                temp.add(mapCells[x][y]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for(MapCell tmc: temp){
                intermediary.add(tmc);
            }
        }

        return neighbors;
    }

    /**
     * Finds the shortest path from a given unit to a given MapCell provided the unit has enough movement to reach it.
     * @param target The map cell to find a path to.
     * @param u The unit that the path is from.
     * @return
     */
    public ArrayList<MapCell> findPathToMC(MapCell target, Unit u){
        ArrayList<MapCell> shortestPath = new ArrayList<MapCell>();
        ArrayList<MapCell> tempPath = new ArrayList<MapCell>();
        MapCell unitLocation = locateUnit(u);
        Integer unitMovement = u.getCurMOV();

        findPathRecurseHelper(shortestPath, tempPath, target, unitLocation, unitMovement);

        return shortestPath;
    }

    private void findPathRecurseHelper(ArrayList<MapCell> sPath, ArrayList<MapCell> tmpPath,
                                            MapCell target, MapCell source, Integer movement){
        ArrayList<MapCell> neighbors;

        if(source != null){
            neighbors = getNeighboringCells(source, 1);

            for(MapCell tmc: neighbors){
                if(tmc.getUnit() == null){//only count the mapcell if it can  be walked on.
                    // only count the tile if the unit could make it to the tile
                    if(movement >= (1 + tmc.getTile().getTerrain().getMovMod())){
                        if(tmc == target){ //found the desired tile
                            tmpPath.add(tmc);
                            if(isShorter(sPath, tmpPath)){
                                sPath.clear();
                                for(MapCell mc: tmpPath){
                                    sPath.add(mc); // new shortest path
                                }
                            }
                            tmpPath.remove(tmpPath.size() - 1);
                        } else {
                            //recurse on the neighbors of the MC
                            tmpPath.add(tmc); // add to the temporary path
                            Integer newMove = movement - (1 + tmc.getTile().getTerrain().getMovMod());
                            findPathRecurseHelper(sPath, tmpPath, target, tmc, newMove);
                        }
                    }
                }
            }
            if(tmpPath.contains(source)){
                tmpPath.remove(source);
            }
        }
    }

    /**
     * Determines which path of cells is shorter.
     * @param shortest The current shortest path.
     * @param challenger The path that might be shorter.
     * @return true if challenger is shorter, false otherwise.
     */
    public boolean isShorter(ArrayList<MapCell> shortest, ArrayList<MapCell> challenger){
        if(challenger.isEmpty()){ // must have at least one mc
            return false;
        }
        if(shortest.isEmpty()){ // must have at least one mc
            return true;
        }
        Integer sLength = 0;
        Integer cLength = 0;

        for(MapCell mc: shortest){
            if(mc != null){
                sLength += (1 + mc.getTile().getTerrain().getMovMod());
            }
        }

        for(MapCell mc: challenger){
            if(mc != null){
                cLength += (1 + mc.getTile().getTerrain().getMovMod());
            }
        }

        return (sLength > cLength);
    }

    /**
     * Builds a string representation of the current class instance.
     * @return The current instance in String notation.
     */
    @Override
    public String toString() {
        String tileString = "";
        for(MapCell[] y: mapCells){
            for (MapCell x : y) {
                tileString += x.toString();
            }
            tileString += "\t";
        }

        String unitString = "";
        for(Unit u: units){
            unitString += u.toString();
        }

        String slString = "";
        for (ArrayList<MapCell> staLoc: startLocs){
            for (MapCell mc: staLoc){
                slString += mc.toString();
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