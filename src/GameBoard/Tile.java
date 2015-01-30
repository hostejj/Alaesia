package GameBoard;

import java.io.Serializable;

public class Tile implements Serializable {
    private Terrain terrain;
    private String imageName;

    private Integer x;
    private Integer y;

    public Tile(String imageName, int x, int y){
        this.imageName = imageName;
        this.terrain = new Terrain();
        this.x = x;
        this.y = y;
    }

    public Tile(String imageName, Terrain terrain, int x, int y){
        this.imageName = imageName;
        this.terrain = terrain;
        this.x = x;
        this.y = y;
    }

    public Tile(Tile t){
        terrain = new Terrain(t.getTerrain());
        imageName = t.getImageName();
        x = t.getX();
        y = t.getY();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getX() {
        return x;
    }
    public Integer getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Tile{" +
                ", imageName='" + imageName + '\'' +
                ", x=" + x +
                ", y=" + y +
                "}\n\t" + terrain.toString();
    }
}
