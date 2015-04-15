package GameBoard;

import GameConcepts.Unit;

import java.io.Serializable;

public class MapCell implements Serializable{
    private Tile tile;
    private Unit unit; // may or may not be null
    private String div = "#";

    public MapCell(Tile tile) {
        this.tile = tile;
        this.unit = null;
    }

    public MapCell(Tile tile, Unit unit) {
        this.tile = tile;
        this.unit = unit;
    }

    public MapCell(String mapCellString){
        String peeledData = mapCellString;
        String data;
        if(peeledData.contains(div)){
            data = peeledData.substring(0,peeledData.indexOf(div));
            tile = new Tile(data);
            peeledData = peeledData.substring(peeledData.indexOf(div)+1);
        }
        if(peeledData.contains(div)){
            data = peeledData.substring(0,peeledData.indexOf(div));
            if(data != "null") {
                unit = new Unit(data);
            } else {
                unit = null;
            }
        }
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "MapCell{" +
                "tile=" + tile +
                ", unit=" + unit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapCell)) return false;

        MapCell mapCell = (MapCell) o;

        if (!tile.equals(mapCell.tile)) return false;
        if (unit != null ? !unit.equals(mapCell.unit) : mapCell.unit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tile.hashCode();
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    public String buildString(){
        String mapCellString = "";
        mapCellString += tile.buildString() + div;

        if(unit != null) {
            mapCellString += unit.buildString() + div;
        } else {
            mapCellString += "null" + div;
        }

        return mapCellString;
    }
}
