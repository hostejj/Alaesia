package GameConcepts;

import GameBoard.GameMap;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable{
    private ArrayList<Player> players;
    private GameMap gameMap;
    private Integer maxArmyValue;
    private Integer maxTurnPoints;
    private Integer turnLimit;

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

    public Integer getTurnLimit() {
        return turnLimit;
    }
}
