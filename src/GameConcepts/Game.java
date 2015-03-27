package GameConcepts;

import GameBoard.GameMap;
import GameBoard.MapCell;
import GameBoard.Tile;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private ArrayList<Player> players;
    private GameMap gameMap;
    private Integer maxArmyValue;
    private Integer maxTurnPoints;
    private Integer turnLimit;

    private String errorMessage = "";
    private boolean errorFlag = false;

    public static enum GameState {
        PLACEMENTPHASE, BATTLE, UNITMOVE, GAMEOVER
    }

    private transient ReadOnlyObjectWrapper<Player> currentPlayer;
    private transient ReadOnlyObjectWrapper<GameState> gameState;

    public Game(GameMap gameMap, Integer maxArmyValue, Integer maxTurnPoints, Integer turnLimit){
        this.gameMap = new GameMap(gameMap);
        this.maxArmyValue = maxArmyValue;
        this.maxTurnPoints = maxTurnPoints;
        this.turnLimit = turnLimit;

        players = new ArrayList<Player>();
        for (int i=0;i<gameMap.getMaxPlayers(); i++){ players.add(new Player()); }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Integer getMaxArmyValue() {
        return maxArmyValue;
    }

    public Integer getMaxTurnPoints() {
        return maxTurnPoints;
    }

    public Player findOwner(Unit unit){
        for(Player player: players){
            for(Unit u: player.getArmy()){
                if(u == unit){
                    return player;
                }
            }
        }
        return null;
    }

    public void setPropertiesOnLoad(){
        this.currentPlayer = new ReadOnlyObjectWrapper<Player>(this, "currentPlayer", players.get(0));
        this.gameState = new ReadOnlyObjectWrapper<GameState>(this, "gameState", GameState.PLACEMENTPHASE);
    }

    /**
     * The player whose turn it currently is. This changes after each action.
     * @return
     */
    public ReadOnlyObjectProperty<Player> currentPlayerProperty() {
        return currentPlayer.getReadOnlyProperty();
    }
    public Player getCurrentPlayer() {
        return currentPlayer.get();
    }
    public void nextPlayer(){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i) == getCurrentPlayer()){
                if(i < players.size() - 1){
                    currentPlayer.set(players.get(i + 1));
                    return;
                } else {
                    currentPlayer.set(players.get(0));
                    return;
                }
            }
        }
        System.err.println("No available next player !!!!");
        return;
    }

    /**
     * The status of the game.
     * @return
     */
    public ReadOnlyObjectProperty<GameState> gameStateProperty() {
        return gameState.getReadOnlyProperty();
    }
    public GameState getGameState() {
        return gameState.get();
    }
    public void setGameState(GameState gameState) {
        this.gameState.set(gameState);
    }

    /**
     * During the unit placement phase of the game, place the selected unit(if it is owned by the current
     * player and if it is not already on the board) on the chosen tile(if the tile is empty). Choose the placement
     * in the current player is a computer. Check to see if the unit placement phase of the game has ended.
     */
    public void placeUnit(Unit unit, Tile tile){
        if(tile == null){
            errorFlag = true;
            errorMessage = "No tile selected.";
            return;
        }
        if(unit == null){
            errorFlag = true;
            errorMessage = "No unit selected.";
            return;
        }
        if (!getGameState().equals(GameState.PLACEMENTPHASE)) { //game is in the placement phase
            errorFlag = true;
            errorMessage = "The game is not in the placement phase.";
            return;
        }
        Player player = findOwner(unit);
        if(player != getCurrentPlayer()) { // the unit belongs to the current player
            errorFlag = true;
            errorMessage = "The selected unit does not belong to you.";
            return;
        }
        if (gameMap.getMapCells()[tile.getX()][tile.getY()].getUnit() != null) { //tile is  not empty
            errorFlag = true;
            errorMessage = "This tile already has a unit on it.";
            return;
        }
        if(gameMap.locateUnit(unit) != null){ // the unit is already placed on the map
            errorFlag = true;
            errorMessage = "This unit is already placed on the map.";
            return;
        }

        boolean tileFound = false;
        Integer pindex = -1;
        for (int i = 0; i < players.size(); i++){
            if(players.get(i) == getCurrentPlayer()){
                pindex = i;
                break;
            }
        }
        if(pindex != -1) {
            for (MapCell mc : gameMap.getStartLocs().get(pindex)) {
                if (mc.getTile() == tile) {
                    tileFound = true;
                    break;
                }
            }
        }
        if(!tileFound){ // the selected tile is not in the list of starting locations
            errorFlag = true;
            errorMessage = "The selected tile is not your starting location.";
            return;
        }
        gameMap.getMapCells()[tile.getX()][tile.getY()].setUnit(unit);
        if(checkPlacementPhaseOver()) { //determine if the placement phase has ended
            gameState.set(GameState.BATTLE);
        }
    }

    public ArrayList<MapCell> actionableMCs(Unit unit){
        ArrayList<MapCell> legitMCs = new ArrayList<MapCell>();
        ArrayList<MapCell> currentList = new ArrayList<MapCell>();

        MapCell unitLocation = gameMap.locateUnit(unit);

        if(unitLocation != null){
            currentList.add(unitLocation); // add the base location to the list of checked cells
            //need to track movement
            Integer movementLeft = unit.getCurMOV();

            actionRecurseHelper(unitLocation, unit, movementLeft, legitMCs);
        }

        return legitMCs;
    }

    private void actionRecurseHelper(MapCell mapCell, Unit unit, Integer movementLeft, ArrayList<MapCell> legitMCs){
        ArrayList<MapCell> neighbors;

        if(movementLeft == 0){
            //dead end
        } else if ( movementLeft == 1){
            //actionable tiles are neighbors that can be moved too
            neighbors = gameMap.getNeighboringCells(mapCell, 1);
            for(MapCell tmpMC: neighbors){
                if(tmpMC.getTile().getTerrain().getMovMod() <= 0){
                    if(tmpMC.getUnit() == null) {
                        if (!legitMCs.contains(tmpMC)) {
                            legitMCs.add(tmpMC);
                        }
                    }
                }
            }

            //actionable tiles are also tiles within range of an attack
            for(int i = unit.getMinRANGE(); i <= unit.getMaxRANGE(); i++){
                neighbors = gameMap.getNeighboringCells(mapCell, i);
                for(MapCell tmpMC: neighbors){
                    if(tmpMC.getUnit() != null){
                        if(findOwner(unit) != findOwner(tmpMC.getUnit())) {
                            if (!legitMCs.contains(tmpMC)) {
                                legitMCs.add(tmpMC);
                            }
                        }
                    }
                }
            }
        } else if ( movementLeft > 1){
            neighbors = gameMap.getNeighboringCells(mapCell, 1);
            //recurse on each reachable tile // otherwise evaluate attack range
            for(MapCell tmpMC: neighbors){
                if(movementLeft >= (1 + tmpMC.getTile().getTerrain().getMovMod())){
                    if(tmpMC.getUnit()==null) {
                        Integer newMovement = movementLeft - (1 + tmpMC.getTile().getTerrain().getMovMod());
                        if (!legitMCs.contains(tmpMC)) {
                            legitMCs.add(tmpMC);
                        }
                        actionRecurseHelper(tmpMC,unit,newMovement,legitMCs);
                    }
                }
            }
            ArrayList<MapCell> tmpNeighs;
            //actionable tiles are also tiles within range of an attack
            for(int i = unit.getMinRANGE(); i <= unit.getMaxRANGE(); i++){
                tmpNeighs = gameMap.getNeighboringCells(mapCell, i);
                for(MapCell tmpMCa: tmpNeighs){
                    if(tmpMCa.getUnit() != null){
                        if(findOwner(unit) != findOwner(tmpMCa.getUnit())) {
                            if (!legitMCs.contains(tmpMCa)) {
                                legitMCs.add(tmpMCa);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean checkPlacementPhaseOver(){
        for (Player player: players){
            for(Unit unit: player.getArmy()){
                if(gameMap.locateUnit(unit) == null){
                    return false;
                }
            }
        }
        return true;
    }

    public Integer getTurnLimit() {
        return turnLimit;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    public Integer getPlayerIndex(Player player){
        for(int i = 0; i < players.size(); i++){
            if(player == players.get(i)){
                return i;
            }
        }
        return -1; //error
    }
}
