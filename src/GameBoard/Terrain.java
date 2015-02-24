package GameBoard;

import java.io.Serializable;

public class Terrain implements Serializable {

    private Integer defMod;
    private Integer evaMod;
    private Integer movMod;
    private Integer retMod;

    public Terrain(){
        this.defMod = 0;
        this.evaMod = 0;
        this.movMod = 0;
        this.retMod = 0;
    }

    public Terrain(int defMod, int evaMod, int movMod, int retMod){
        this.defMod = defMod;
        this.evaMod = evaMod;
        this.movMod = movMod;
        this.retMod = retMod;
    }

    public Terrain(Terrain t){
        defMod = t.defMod;
        evaMod = t.evaMod;
        movMod = t.movMod;
        retMod = t.retMod;
    }

    public Integer getDefMod() {
        return defMod;
    }
    public void setDefMod(int defMod) {
        this.defMod = defMod;
    }
    public Integer getEvaMod() {
        return evaMod;
    }
    public void setEvaMod(int evaMod) {
        this.evaMod = evaMod;
    }
    public Integer getMovMod() {
        return movMod;
    }
    public void setMovMod(int movMod) {
        this.movMod = movMod;
    }
    public Integer getRetMod() {
        return retMod;
    }
    public void setRetMod(int retMod) {
        this.retMod = retMod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Terrain)) return false;

        Terrain terrain = (Terrain) o;

        if (!defMod.equals(terrain.defMod)) return false;
        if (!evaMod.equals(terrain.evaMod)) return false;
        if (!movMod.equals(terrain.movMod)) return false;
        if (!retMod.equals(terrain.retMod)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = defMod.hashCode();
        result = 31 * result + evaMod.hashCode();
        result = 31 * result + movMod.hashCode();
        result = 31 * result + retMod.hashCode();
        return result;
    }

    @Override
    public String toString(){
        return "def = " +  defMod + "| eva = " + evaMod + "| mov = " + movMod + "| ret = " + retMod + "\n";
    }
}
