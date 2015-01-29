package GameBoard;

import java.io.Serializable;

public class Tile implements Serializable {
    private Terrain terrain;
    private String imageName;

    private Integer x;
    private Integer y;

    public Tile(String imageName, int x, int y){
        this.terrain = new Terrain();
        this.imageName = imageName;
        this.x = x;
        this.y = y;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getImageName() {
        return imageName;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
