package GameBoard;

import GameConcepts.Unit;

public class MapCell {
    Tile tile;
    Unit unit; // may or may not be null

    public MapCell(Tile tile) {
        this.tile = tile;
    }

    public MapCell(Tile tile, Unit unit) {
        this.tile = tile;
        this.unit = unit;
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
}
