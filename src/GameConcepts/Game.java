package GameConcepts;

import GameBoard.GameMap;
import GameBoard.MapCell;
import GameBoard.Tile;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable {
    private ArrayList<Player> players;
    private GameMap gameMap;
    private Integer maxArmyValue;
    private Integer maxTurnPoints;
    private Integer turnLimit;

    private String errorMessage = "";
    private String logMessage = "";
    private boolean errorFlag = false;
    private final static Integer HITPERCENT = 100;
    private final static Integer CRITDIV = 15;
    private final static Double CRITMULT = 1.5;
    private final static Integer MINBOUND = 8;
    private final static Integer MAXBOUND = 12;
    private final static Integer ATTEXP = 1;
    private final static Integer HITEXP = 2;
    private final static Integer KILLEXP = 5;
    private Player winner;

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
        for (int i=0;i<gameMap.getMaxPlayers(); i++){
            players.add(new Player());
            players.get(i).setTurnPoints(maxTurnPoints);
            players.get(i).setMaxTurnPoints(maxTurnPoints);
        }
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
                    getCurrentPlayer().refreshPlayer();
                    currentPlayer.set(players.get(i + 1));
                    return;
                } else {
                    getCurrentPlayer().refreshPlayer();
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
        logMessage = player.getPlayerName() + " placed " + unit.toString() +
                " at location " + tile.getX() + ", " + tile.getY() + ".\n";
        if(checkPlacementPhaseOver()) { //determine if the placement phase has ended
            gameState.set(GameState.BATTLE);
        }
    }

    /**
     * During the battle phase of the game, move the unit in use of the current player.
     * The unit should move to a MC that is currently empty and in the list of actionable MCs.
     */
    public void moveUnit(MapCell mapCell){
        //use the current player unit in use
        Unit u = getCurrentPlayer().getUnitInUse();
        if(u != null){
            if(mapCell != null){
                if (mapCell.getUnit() != null){
                    errorFlag = true;
                    errorMessage = "Cannot move to a tile that already has a unit on it.";
                    return;
                }
                ArrayList<MapCell> path = gameMap.findPathToMC(mapCell, u);
                if(path.isEmpty()){
                    errorFlag = true;
                    errorMessage = "Cannot find a path to the selected cell.";
                    return;
                }

                //found a path so its okay to move the unit
                Integer moveCost = 0;
                for(MapCell mc: path){
                    moveCost += (1 + mc.getTile().getTerrain().getMovMod());
                }
                u.setCurMOV(u.getCurMOV() - moveCost);
                MapCell sourceLoc = gameMap.locateUnit(u);
                sourceLoc.setUnit(null); // remove unit from its last location
                gameMap.getMapCells()[mapCell.getTile().getX()][mapCell.getTile().getY()].setUnit(u);
                logMessage = getCurrentPlayer().getPlayerName() + "'s " + u.toString() +
                        " moved from location " + sourceLoc.getTile().getX() + ", " + sourceLoc.getTile().getY() +
                        " to " + mapCell.getTile().getX() + ", " + mapCell.getTile().getY() + ".\n";
            }
        }
    }

    /**
     * With the unit in use attack the unit on a target map cell. The target must
     * be withing the range of the unit in use.
     * @param target The cell that is being attacked.
     */
    public void attackUnit(MapCell target) {

        if (!attackValid(target)) {
            return;
        }

        //use the current player unit in use
        Unit u = getCurrentPlayer().getUnitInUse();

        //the attack is valid so do the attack
        Integer moveCost = 0;
        u.setCurMOV(u.getCurMOV() - 1); // an attack costs 1 movement

        Random random = new Random();
        boolean died = false;

        //see if the attack hits the oppenent
        Integer hitChance = u.getACC() - (target.getUnit().getEVA() + target.getTile().getTerrain().getEvaMod());
        if(random.nextInt(HITPERCENT) < hitChance){

            //the attack was successful check for a critical hit
            boolean crit = false;
            if(random.nextInt(HITPERCENT) < (hitChance/CRITDIV)){
                crit = true;
            }

            Integer damage = u.getSTR() - (target.getUnit().getDEF() + target.getTile().getTerrain().getDefMod());

            if(crit){
                damage = ((Double) (damage.doubleValue() * ((random.nextInt(MAXBOUND
                        - MINBOUND) + MINBOUND)/10.0) * CRITMULT.doubleValue())).intValue();
                logMessage = getCurrentPlayer().getPlayerName() + "'s " + u.toString() +
                        " critically attacked " + target.getUnit().toString() +
                        " for " + damage + " damage.\n";
            } else {
                damage = ((Double) (damage.doubleValue() * ((random.nextInt(MAXBOUND - MINBOUND) + MINBOUND)/10.0))).intValue();
                logMessage = getCurrentPlayer().getPlayerName() + "'s " + u.toString() +
                        " attacked " + target.getUnit().toString() +
                        " for " + damage + " damage.\n";
            }

            target.getUnit().setCurHP(target.getUnit().getCurHP() - damage);

            if(target.getUnit().getCurHP() <= 0){ //the target died
                died = true;
                u.addExperience(KILLEXP);
            }
            u.addExperience(HITEXP);
        } else {
            logMessage = getCurrentPlayer().getPlayerName() + "'s " + u.toString() +
                    " attacked and missed.\n";
        }
        u.addExperience(ATTEXP);

        if(!died){
            //opposing unit can retaliate if within range
            if((target.getUnit().getCurRET() + target.getTile().getTerrain().getRetMod()) > 0){

                //find out if the attacker is in range of defender retaliation
                if(inAttackRange(gameMap.locateUnit(u), gameMap.locateUnit(target.getUnit()), target.getUnit())) {
                    target.getUnit().setCurRET(target.getUnit().getCurRET() - 1);

                    hitChance = target.getUnit().getACC() - (u.getEVA() + gameMap.locateUnit(u).getTile().getTerrain().getEvaMod());
                    if(random.nextInt(HITPERCENT) < hitChance){
                        //the attack was successful check for a critical hit
                        boolean crit = false;
                        if(random.nextInt(HITPERCENT) < hitChance/CRITDIV){
                            crit = true;
                        }

                        Integer damage = target.getUnit().getSTR() - (u.getDEF() + gameMap.locateUnit(u).getTile().getTerrain().getDefMod());

                        if(crit){
                            damage = ((Double) (damage.doubleValue() * ((random.nextInt(MAXBOUND
                                    - MINBOUND) + MINBOUND)/10.0) * CRITMULT.doubleValue())).intValue();
                            logMessage += findOwner(target.getUnit()).getPlayerName() + "'s " + target.getUnit().toString() +
                                    " critically retaliated " + u.toString() + " for " + damage + " damage.\n";
                        } else {
                            damage = ((Double) (damage.doubleValue() * ((random.nextInt(MAXBOUND - MINBOUND) + MINBOUND)/10.0))).intValue();
                            logMessage += findOwner(target.getUnit()).getPlayerName() + "'s " + target.getUnit().toString() +
                                    " retaliated " + u.toString() + " for " + damage + " damage.\n";
                        }

                        u.setCurHP(u.getCurHP() - damage);
                        if(u.getCurHP() <= 0){ //the target died
                            target.getUnit().addExperience(KILLEXP);
                        }

                        target.getUnit().addExperience(HITEXP);
                    } else{
                        logMessage += findOwner(target.getUnit()).getPlayerName() + "'s " + target.getUnit().toString() +
                                " retaliated and missed.\n";
                    }
                    target.getUnit().addExperience(ATTEXP);
                }
            }
        }
    }

    public void unitDeath(Unit unit){
        Player owner = findOwner(unit);
        MapCell location = gameMap.locateUnit(unit);

        owner.removeUnit(unit);
        location.setUnit(null);

        logMessage = owner.getPlayerName() + "'s " + unit.toString() + " died.\n";

        if(owner.getArmy().size() == 0){
            playerDeath(owner);
        }
    }

    public void playerDeath(Player player){
        players.remove(player);
        logMessage += player.getPlayerName() + " has no units left and is out of the game.\n";

        if (players.size() == 1){
            winner = players.get(0);
            logMessage += winner.getPlayerName() + " wins!\n";
            setGameState(GameState.GAMEOVER);
        }
    }

    public boolean attackValid(MapCell target) {
        //use the current player unit in use
        Unit u = getCurrentPlayer().getUnitInUse();
        if (u != null) {
            if (target != null) {
                if (target.getUnit() == null) {
                    errorFlag = true;
                    errorMessage = "Cannot attack a tile that already has a no on it.";
                    return false;
                }
                if (findOwner(target.getUnit()) == getCurrentPlayer()) {
                    errorFlag = true;
                    errorMessage = "Cannot attack a tile that already has your own unit on it.";
                    return false;
                }
                if (u.getCurMOV() < 1) {
                    errorFlag = true;
                    errorMessage = "Cannot attack because the unit does not have enough movement.";
                    return false;
                }
                if (!inAttackRange(target, gameMap.locateUnit(u), u)) {
                    errorFlag = true;
                    errorMessage = "The selected tile is not in range of being attacked.";
                    return false;
                }

                // the attack is valid
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Determines whether or not a target tile is within the attack range of a unit;
     * @param target The tile to be attacked.
     * @param source The tile the unit is originating from
     * @param u The unit doing the attacking
     * @return
     */
    public boolean inAttackRange(MapCell target, MapCell source, Unit u){
        ArrayList<MapCell> attackable = new ArrayList<MapCell>();
        for (int i = u.getMinRANGE(); i <= u.getMaxRANGE(); i++) {
            ArrayList<MapCell> neighbors = gameMap.getNeighboringCells(source, i);
            for (MapCell tmpmc : neighbors) {
                if (!attackable.contains(tmpmc)) {
                    attackable.add(tmpmc);
                }
            }
        }

        if(attackable.contains(target)){
            return true;
        }
        return false;
    }

    public ArrayList<MapCell> moveableMCs(Unit unit){
        ArrayList<MapCell> moveableMCs = new ArrayList<MapCell>();
        for(MapCell mc: actionableMCs(unit)){
            if(mc.getUnit() == null){
                moveableMCs.add(mc);
            }
        }
        return moveableMCs;
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

    public Player getWinner() {
        return winner;
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

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
