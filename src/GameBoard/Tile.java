package GameBoard;

import java.io.Serializable;

public class Tile implements Serializable {
    private Terrain terrain;
    private String imageName;
    private int x;
    private int y;

    public Tile(String imageName, int x, int y){
        this.terrain = new Terrain();
        this.imageName = imageName;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
