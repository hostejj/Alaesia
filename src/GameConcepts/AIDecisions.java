package GameConcepts;

import GameBoard.Tile;

import java.io.Serializable;
import java.util.Random;

public class AIDecisions implements Serializable{
    private Unit neededUnit;
    private Tile neededTile;
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
            if(game.getGameMap().getStartLocs().get(pindex).get(rand).getUnit() == null){
                neededTile = game.getGameMap().getStartLocs().get(pindex).get(rand).getTile();
                pickedTile = true;
            }
        }

        game.placeUnit(neededUnit, neededTile);
        return true;
    }

    public Unit getNeededUnit() {
        return neededUnit;
    }

    public Tile getNeededTile() {
        return neededTile;
    }
}
