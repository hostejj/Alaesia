package GameConcepts;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{
    private static final String DEFAULTPLAYERNAME = "Player";

    private String playerName;
    private ArrayList<Unit> army;
    private Integer turnPoints;
    private boolean human;
    private AIDecisions aiDecisions;

    public Player() {
        playerName = DEFAULTPLAYERNAME;
        army = new ArrayList<Unit>();
        turnPoints = 0;
        human = true;
        aiDecisions = new AIDecisions(this);
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

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public AIDecisions makeMove(){
        return aiDecisions;
    }
}
