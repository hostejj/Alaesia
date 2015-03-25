package GameConcepts;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{
    private static final String DEFAULTPLAYERNAME = "Player";

    private String playerName;
    private ArrayList<Unit> army;
    private PlayerBehavior playerBehavior;
    private Integer turnPoints;

    public Player(){
        playerName = DEFAULTPLAYERNAME;
        army = new ArrayList<Unit>();
        turnPoints = 0;
    }

    public ArrayList<Unit> getArmy() {
        return army;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Integer getArmyValue(){
        Integer value = 0;
        for(Unit u: army){
            value += u.getTypeVal();
        }
        return value;
    }

    public Integer getTurnPoints() {
        return turnPoints;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerBehavior getPlayerBehavior() {
        return playerBehavior;
    }

    public void setPlayerBehavior(PlayerBehavior playerBehavior) {
        this.playerBehavior = playerBehavior;
    }
}
