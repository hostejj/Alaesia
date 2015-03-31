package GameConcepts;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{
    private static final String DEFAULTPLAYERNAME = "Player";

    private String playerName;
    private ArrayList<Unit> army;
    private Integer turnPoints;
    private Integer maxTurnPoints;
    private boolean human;
    private AIDecisions aiDecisions;
    private Unit unitInUse;

    public Player() {
        playerName = DEFAULTPLAYERNAME;
        army = new ArrayList<Unit>();
        turnPoints = 0;
        maxTurnPoints = 0;
        human = true;
        aiDecisions = new AIDecisions(this);
        unitInUse = null;
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

    public void setTurnPoints(Integer turnPoints) {
        this.turnPoints = turnPoints;
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

    public Unit getUnitInUse() {
        return unitInUse;
    }

    public void setUnitInUse(Unit unitInUse) {
        this.unitInUse = unitInUse;
    }

    public AIDecisions makeMove(){
        return aiDecisions;
    }

    public Integer getMaxTurnPoints() {
        return maxTurnPoints;
    }

    public void setMaxTurnPoints(Integer maxTurnPoints) {
        this.maxTurnPoints = maxTurnPoints;
    }

    public void removeUnit(Unit u){
        if(army.contains(u)){
            army.remove(u);
        }
        if(unitInUse == u){
            unitInUse = null;
        }
    }

    public void refreshPlayer(){
        turnPoints = maxTurnPoints;
        for(Unit unit:army){
            unit.refreshUnit();
        }
    }
}
