package GameConcepts;

import java.util.ArrayList;

public class Army {
    private ArrayList<Unit> units;
    private Integer armyValue;

    public Army() {
        units = new ArrayList<Unit>();
        armyValue = 0;
        for(Unit u: units){
            armyValue += u.getVALUE();
        }
    }
}