package GameConcepts;

import GameBoard.MapCell;
import GameBoard.Tile;
import com.sun.javafx.collections.MappingChange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class AIDecisions implements Serializable{
    private Unit neededUnit;
    private Tile neededTile;
    private MapCell neededMapCell;
    private Player owner;

    public AIDecisions(Player player){
        this.owner = player;
    }

    public boolean placeUnit(Game game){
        boolean unitsLeft = false;
        boolean pickedUnit = false;
        boolean pickedTile = false;

        for (Unit u: owner.getArmy()){
            if(game.getGameMap().locateUnit(u) == null){
                unitsLeft = true;
                break;
            }
        }

        if(!unitsLeft){
            return false;
        }

        Random random = new Random();
        while (!pickedUnit){
            Integer rand = random.nextInt(owner.getArmy().size());
            if(game.getGameMap().locateUnit(owner.getArmy().get(rand)) == null){
                neededUnit = owner.getArmy().get(rand);
                pickedUnit = true;
            }
        }

        Integer pindex = game.getPlayerIndex(owner);
        while (!pickedTile){
            Integer rand = random.nextInt(game.getGameMap().getStartLocs().get(pindex).size());
            Integer slx = game.getGameMap().getStartLocs().get(pindex).get(rand).getTile().getX();
            Integer sly = game.getGameMap().getStartLocs().get(pindex).get(rand).getTile().getY();
            if(game.getGameMap().getMapCells()[slx][sly].getUnit() == null){
                neededTile = game.getGameMap().getStartLocs().get(pindex).get(rand).getTile();
                pickedTile = true;
            }
        }

        game.placeUnit(neededUnit, neededTile);
        return true;
    }

    /**
     * Choose the best move to make. General structure of decision making is currently as follows.
     * If the AI can kill an opposing unit it will try to do so first.
     * If the AI can attack without fear of retaliation it will do so second.
     * If the AI can attack a unit without fear of death for the attacking unit it will do so.
     * In all attacks that are even in the above choices the AI will attack the lower HP unit first.
     * The AI will attack with its units that have more hit points remaining first.
     * The AI will move units along the shortest path towards enemies.  ***this decision is weak
     * The AI will strive to make its units stay adjacent.              ***not currently implemented
     * The AI will run with units that would be killed by the opponent. ***maybe weak
     * When the AI chooses to pass it sets its turn points to 0.
     * @param game The game on which the AI is basing its decision.
     */
    public void chooseMove(Game game){
        neededMapCell = null;
        neededTile = null;
        neededUnit = null;
        ArrayList<Unit> availableUnits = getActionableUnits();
        if(unitKillable(game, availableUnits)){
            owner.setUnitInUse(neededUnit);
            owner.useTurnPoints(neededUnit.getCPA());
            if(game.inAttackRange(neededMapCell, game.getGameMap().locateUnit(neededUnit), neededUnit)){
                game.attackUnit(game.getGameMap().getMapCells()[neededTile.getX()][neededTile.getY()]);
            } else {
                MapCell setupMoveCell = preAttackMoveCell(game);
                game.moveUnit(setupMoveCell);
                game.attackUnit(neededMapCell);
            }
            owner.setUnitInUse(null);
        }
        else if(freeAttack(game, availableUnits)){
            owner.setUnitInUse(neededUnit);
            owner.useTurnPoints(neededUnit.getCPA());

            MapCell setupMoveCell = preAttackMoveCell(game);
            if(setupMoveCell != game.getGameMap().locateUnit(neededUnit)) {
                game.moveUnit(setupMoveCell);
            }
            game.attackUnit(neededMapCell);

            owner.setUnitInUse(null);
        }
        else if(standardAttack(game, availableUnits)){
            owner.setUnitInUse(neededUnit);
            owner.useTurnPoints(neededUnit.getCPA());
            if(game.inAttackRange(neededMapCell, game.getGameMap().locateUnit(neededUnit), neededUnit)){
                game.attackUnit(game.getGameMap().getMapCells()[neededTile.getX()][neededTile.getY()]);
            } else {
                MapCell setupMoveCell = preAttackMoveCell(game);
                game.moveUnit(setupMoveCell);
                game.attackUnit(neededMapCell);
            }
            owner.setUnitInUse(null);
        }
        else if(moveForward(game, availableUnits)){
            owner.setUnitInUse(neededUnit);
            owner.useTurnPoints(neededUnit.getCPA());
            game.moveUnit(neededMapCell);
            owner.setUnitInUse(null);
        }
        else if(runAway(game, availableUnits)){

        }
        else {
            //no good moves pass the turn;
            owner.setTurnPoints(0);
        }
    }

    /**
     * Determines in there are any killable opposing units.
     * @param game The game that the AI decision is based on.
     * @param availableUnits A list of units that are able to move.
     * @return Returns true if there are killable units.
     */
    public boolean unitKillable(Game game, ArrayList<Unit> availableUnits){
        ArrayList<Unit> attackableUnits;

        for (Unit attacker: availableUnits){
            attackableUnits = getAttackableUnits(game, attacker);
            for(Unit defender: attackableUnits){
                Tile defenderTile = game.getGameMap().locateUnit(defender).getTile();
                if(defender.getCurHP() <= (attacker.getSTR() - (defender.getDEF() + defenderTile.getTerrain().getDefMod()))){
                    if (neededUnit == null) {
                        neededUnit = attacker;
                        neededTile = defenderTile;
                        neededMapCell = game.getGameMap().getMapCells()[neededTile.getX()][neededTile.getY()];
                    } else {
                        if(attacker.getCurHP() >= neededUnit.getCurHP()){
                            neededUnit = attacker;
                            neededTile = defenderTile;
                            neededMapCell = game.getGameMap().getMapCells()[neededTile.getX()][neededTile.getY()];
                        }
                    }
                    break;
                }
            }
        }

        if (neededUnit != null) {
            return true;
        }
        return false;
    }

    /**
     * Determines if there are any units able to attack that won't be retaliated against.
     * @param game The game that the AI decision is based on.
     * @param availableUnits A list of units that are able to move.
     * @return True if possible, false otherwise.
     */
    public boolean freeAttack(Game game, ArrayList<Unit> availableUnits){
        ArrayList<Unit> attackableUnits;

        for (Unit attacker: availableUnits) {
            attackableUnits = getAttackableUnits(game, attacker);
            for(Unit defender: attackableUnits){
                // proxy move to each movable tile
                for (MapCell attackMC : game.moveableMCs(attacker)) {
                    //check if there is a tile you can move, attack from, and not be attacked back
                    if ((game.inAttackRange(game.getGameMap().locateUnit(defender), attackMC, attacker)) &&
                            (!game.inAttackRange(attackMC, game.getGameMap().locateUnit(defender), defender))) {
                        if (neededUnit == null) {
                            neededUnit = attacker;
                            neededMapCell = game.getGameMap().locateUnit(defender);
                            neededTile = neededMapCell.getTile();
                            break;
                        } else {
                            if(attacker.getCurHP() > neededUnit.getCurHP()){
                                neededUnit = attacker;
                                neededMapCell = game.getGameMap().locateUnit(defender);
                                neededTile = neededMapCell.getTile();
                                break;
                            }
                        }
                    }
                }
                if(neededUnit != null){
                    break;
                }
            }
            if(neededUnit != null){
                if(attacker.getCurHP() <= neededUnit.getCurHP()) {
                    break;
                }
            }
        }

        if(neededUnit != null){
            return true;
        }

        return false;
    }

    /**
     * Determines if there are any units available to attack.
     * @param game The game that the AI bases its decision on.
     * @param availableUnits A list of units that are able to move.
     * @return True if an attack is possible, false otherwise.
     */
    public boolean standardAttack(Game game, ArrayList<Unit> availableUnits){
        ArrayList<Unit> attackableUnits;

        for (Unit attacker: availableUnits){
            attackableUnits = getAttackableUnits(game, attacker);
            for(Unit defender: attackableUnits){
                if(neededMapCell == null){
                    neededUnit = attacker;
                    neededMapCell = game.getGameMap().locateUnit(defender);
                    neededTile = neededMapCell.getTile();
                } else if(defender.getCurHP() < neededMapCell.getUnit().getCurHP()){
                    neededUnit = attacker;
                    neededMapCell = game.getGameMap().locateUnit(defender);
                    neededTile = neededMapCell.getTile();
                } else if((attacker.getCurHP() > neededUnit.getCurHP()) && (neededMapCell == game.getGameMap().locateUnit(defender))){
                    neededUnit = attacker;
                    neededMapCell = game.getGameMap().locateUnit(defender);
                    neededTile = neededMapCell.getTile();
                }
            }
        }
        if(neededUnit != null){
            return true;
        }
        return false;
    }

    /**
     * Determines if a unit should move towards enemy units.
     * @param game The game that the AI bases its decision on.
     * @param availableUnits A list of units that are able to move.
     * @return True if movement is recommended, false otherwise.
     */
    public boolean moveForward(Game game, ArrayList<Unit> availableUnits){
        // find the closest enemy unit
        // find the tile that would leave the moving unit the closest
        Unit closestEnemy = null;
        MapCell closestEnemyLocation = null;
        Integer closestEnemyXY = Integer.MAX_VALUE; // initialize to a large value
        MapCell enemyLocation;
        Integer enemyXY;
        Unit closestUnit = null;
        MapCell closestUnitLocation;
        Integer closestUnitXY = 0;
        MapCell unitLocation;
        Integer unitXY;
        MapCell targetLocation = null;
        Integer targetXY = 0;
        Integer currentXY = 0;

        for(Unit unit: availableUnits){
            if(closestUnit == null){
                closestUnit = unit;
                closestUnitLocation = game.getGameMap().locateUnit(closestUnit);
                closestUnitXY = closestUnitLocation.getTile().getX() + closestUnitLocation.getTile().getY();
            }
            unitLocation = game.getGameMap().locateUnit(unit);
            unitXY = unitLocation.getTile().getX() + unitLocation.getTile().getY();
            for (Player player: game.getPlayers()){
                if(player != owner){
                    for(Unit enemy: player.getArmy()){
                        if(closestEnemy == null){
                            closestEnemy = enemy;
                            closestEnemyLocation = game.getGameMap().locateUnit(closestEnemy);
                            closestEnemyXY = closestEnemyLocation.getTile().getX() + closestEnemyLocation.getTile().getY();
                        }
                        enemyLocation = game.getGameMap().locateUnit(enemy);
                        enemyXY = enemyLocation.getTile().getX() + enemyLocation.getTile().getY();
                        if(Math.abs(enemyXY - unitXY) < Math.abs(closestEnemyXY - closestUnitXY)){
                            closestEnemy = enemy;
                            closestEnemyLocation = game.getGameMap().locateUnit(closestEnemy);
                            closestEnemyXY = closestEnemyLocation.getTile().getX() + closestEnemyLocation.getTile().getY();
                            closestUnit = unit;
                            closestUnitLocation = game.getGameMap().locateUnit(closestUnit);
                            closestUnitXY = closestUnitLocation.getTile().getX() + closestUnitLocation.getTile().getY();
                        }
                    }
                }
            }
        }

        if(closestEnemyLocation != null){
            neededUnit = closestUnit;

            for (MapCell currentLocation: game.moveableMCs(neededUnit)){
                if(targetLocation == null) {
                    targetLocation = currentLocation;
                    targetXY = targetLocation.getTile().getX() + targetLocation.getTile().getY();
                }
                currentXY = currentLocation.getTile().getX() + currentLocation.getTile().getY();

                if(Math.abs(closestEnemyXY - currentXY) < Math.abs(closestEnemyXY - targetXY)){
                    targetLocation = currentLocation;
                    targetXY = currentXY;
                }
            }
        }

        if(targetLocation != null){
            neededMapCell = targetLocation;
            neededTile = neededMapCell.getTile();
            return true;
        }

        return false;
    }

    /**
     * Determines if a unit should run from enemy units.
     * @param game The game that the AI bases its decision on.
     * @param availableUnits A list of units that are able to move.
     * @return True if movement is recommended, false otherwise.
     */
    public boolean runAway(Game game, ArrayList<Unit> availableUnits){
        boolean atRisk = false;
        boolean saveable = false;
        for (Unit unit: availableUnits){
            if(unit.getCurHP() < unit.getHP()/4){
                continue;
            }
            for (Player player: game.getPlayers()){
                if(player != owner){
                    for (Unit enemy: player.getArmy()){
                        ArrayList<MapCell> enemyActions = game.actionableMCs(enemy);
                        if(enemyActions.contains(game.getGameMap().locateUnit(unit))){
                            atRisk = true;
                            break;
                        }
                    }
                }
                if(atRisk == true){
                    break;
                }
            }
            ArrayList<MapCell> unitMoves = game.moveableMCs(unit);
            for(MapCell possibleEscape: unitMoves){
                boolean safe = true;
                for (Player player: game.getPlayers()) {
                    if (player != owner) {
                        for (Unit enemy : player.getArmy()) {
                            if(game.actionableMCs(enemy).contains(possibleEscape)){
                                safe = false;
                                break;
                            }
                        }
                    }
                    if(!safe){
                        break;
                    }
                }
                if(safe){
                    neededMapCell = possibleEscape;
                    neededTile = possibleEscape.getTile();
                    neededUnit = unit;
                    saveable = true;
                    break;
                }
            }
            if(saveable){
                break;
            } else {
                atRisk = false;
            }
        }

        if (saveable){
            return true;
        }

        return false;
    }

    /**
     * A move exists for the AI player when that player has a unit with actions left, and has enough turn points to
     * activate that unit.
     * @return True if there is a valid move, false otherwise.
     */
    public boolean moveExists(){
        if (getActionableUnits().size() >= 1){
            return true;
        }
        return false;
    }

    /**
     * Find a list of units owned by the AI player that are capable of taking an action.
     * @return The list of units found.
     */
    public ArrayList<Unit> getActionableUnits(){
        ArrayList<Unit> actionableUnits = new ArrayList<Unit>();

        for (Unit u: owner.getArmy()){
            if(u.getCurACT() > 0){
                if(owner.getTurnPoints() >= u.getCPA()){
                    actionableUnits.add(u);
                }
            }
        }

        return actionableUnits;
    }

    /**
     * Returns the list of units that are capable of being attacked by the attacker.
     * @param game The game for which the AI is deciding on.
     * @param attacker The unit that is doing the attacking.
     * @return A list of units that are capable of being attacked by the attacker.
     */
    public ArrayList<Unit> getAttackableUnits(Game game, Unit attacker){
        ArrayList<Unit> attackableUnits = new ArrayList<Unit>();
        ArrayList<MapCell> actionableTiles = game.actionableMCs(attacker);

        for(MapCell target: actionableTiles){
            if(target.getUnit() != null){
                attackableUnits.add(target.getUnit());
            }
        }

        return attackableUnits;
    }

    /**
     * Finds a mapcell that the unit attack a unit currently out of range so move too to cause the defender to be within
     * the range of attack.
     * @param game The game that the AI is basing the decision on.
     * @return The cell to move to.
     */
    public MapCell preAttackMoveCell(Game game){
        MapCell preAttackCell = null;
        boolean unreachable = false;

        for(MapCell mapCell: game.moveableMCs(neededUnit)) {
            if (game.inAttackRange(neededMapCell, mapCell, neededUnit)) {
                ArrayList<MapCell> path = game.getGameMap().findPathToMC(mapCell, neededUnit);
                Integer moveCost = 0;
                for(MapCell mc: path){
                    moveCost += (1 + mc.getTile().getTerrain().getMovMod());
                }
                if((neededUnit.getCurMOV() - moveCost) >= 1){
                    //the cell is usable
                    if(preAttackCell == null){
                        preAttackCell = mapCell;
                    } else if(!game.inAttackRange(mapCell, neededMapCell, neededMapCell.getUnit())) { //unreachable from defender
                        preAttackCell = mapCell; // found a better tile
                        unreachable = true;
                    }
                    if(neededUnit.getCurMOV() - moveCost >= 2){ //possible chance for second attack
                        if(!unreachable){
                            preAttackCell = mapCell;
                        } else {
                            if(!game.inAttackRange(mapCell, neededMapCell, neededMapCell.getUnit())) {
                                preAttackCell = mapCell; // found a better tile
                                break; // set of the best cells
                            }
                        }
                    }
                }
            }
        }

        return preAttackCell;
    }

    public Unit getNeededUnit() {
        return neededUnit;
    }

    public Tile getNeededTile() {
        return neededTile;
    }
}
