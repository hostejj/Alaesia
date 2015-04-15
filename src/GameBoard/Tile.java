package GameBoard;

import java.io.Serializable;

public class Tile implements Serializable {
    private final String DEFAULTTILEDIR = "Resources/TileImages/grass.png";

    private Terrain terrain;
    private String imageName;
    private String div = "@";

    private Integer x;
    private Integer y;

    public Tile(int x, int y){
        this.imageName = DEFAULTTILEDIR;
        this.terrain = new Terrain();
        this.x = x;
        this.y = y;
    }

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

    public Tile(String tileString){
        String peeledData = tileString;
        String data;
        if(peeledData.contains(div)){
            data = peeledData.substring(0, peeledData.indexOf(div));
            imageName = data;
            peeledData = peeledData.substring(peeledData.indexOf(div)+1);
        }
        if(peeledData.contains(div)){
            data = peeledData.substring(0, peeledData.indexOf(div));
            try {
                x = Integer.parseInt(data);
            } catch (NumberFormatException nfe){
                System.err.println(nfe);
            }
            peeledData = peeledData.substring(peeledData.indexOf(div)+1);
        }
        if(peeledData.contains(div)){
            data = peeledData.substring(0, peeledData.indexOf(div));
            try {
                y = Integer.parseInt(data);
            } catch (NumberFormatException nfe){
                System.err.println(nfe);
            }
            peeledData = peeledData.substring(peeledData.indexOf(div)+1);
        }
        if(peeledData.contains(div)){
            data = peeledData;
            terrain = new Terrain(data);
        }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        if (!imageName.equals(tile.imageName)) return false;
        if (!terrain.equals(tile.terrain)) return false;
        if (!x.equals(tile.x)) return false;
        if (!y.equals(tile.y)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = terrain.hashCode();
        result = 31 * result + imageName.hashCode();
        result = 31 * result + x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }

    public String buildString() {
        return imageName + div + x + div + y + div + terrain.buildString();
    }

    @Override
    public String toString() {
        return "Tile{" +
                "DEFAULTTILEDIR='" + DEFAULTTILEDIR + '\'' +
                ", terrain=" + terrain +
                ", imageName='" + imageName + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
