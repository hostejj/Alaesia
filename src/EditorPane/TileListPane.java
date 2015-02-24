package EditorPane;

import GameBoard.Terrain;
import GameBoard.Tile;
import javafx.scene.layout.GridPane;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * The TileListPane is a compilation of all the tile types which have both
 * a image file and a terrain data file. The TileListPane is situated in the
 * map editor and is what the user select to change which tile is being used
 * by the editor brush tools.
 */
public class TileListPane extends GridPane {

    private EditorController observer;

    private final String tileListDirName = "Resources/TileImages/";
    private final String tileDatDirName = "Resources/TileData/";
    private final int MAXWIDTH = 3;
    private static int columnIndex = 0;
    private static int rowIndex = 0;

    public ArrayList<String> tileList = new ArrayList<String>();
    public ArrayList<TileListPaneButton> tiles = new ArrayList<TileListPaneButton>();

    /**
     * Default constructor for TileListPane
     */
    public TileListPane() {
        super();
        loadTiles();
        addTiles();
    }

    /**
     * Add the generated tiles into the Pane.
     */
    public void addTiles(){
        for(TileListPaneButton button: tiles){
            if(columnIndex > MAXWIDTH){
                columnIndex = 0;
                rowIndex++;
            }
            add(button , columnIndex++, rowIndex);
        }
    }

    /**
     * Loads the data from a file about a tile's terrain into a terrain variable.
     * @param fileName The name of the file that contains a terrain info.
     * @return Return true if the load into a terrain variable was successful.
     *         Returns false otherwise.
     */
    public boolean loadData(String fileName){
        try{
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            String fileData = new String(encoded, Charset.defaultCharset());
            String peeledData = new String(fileData);
            {
                String defMod = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String evaMod = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String movMod = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String retMod = peeledData.substring(0,peeledData.indexOf(';'));
                try {
                    Integer def = Integer.parseInt(defMod);
                    Integer eva = Integer.parseInt(evaMod);
                    Integer mov = Integer.parseInt(movMod);
                    Integer ret = Integer.parseInt(retMod);

                    Tile t = new Tile((fileName.replace(tileDatDirName, tileListDirName)).replace(".dat", ".png"),new Terrain(def, eva, mov, ret),-1,-1);
                    TileListPaneButton tileListPaneButton = new TileListPaneButton(t, this);
                    tiles.add(tileListPaneButton);
                } catch (NumberFormatException nfe){
                    System.err.println("There was an error with the numerical data in the file " + fileName);
                    return false;
                }
            }
            return true;
        } catch (IOException ioe){
            System.err.println("There was an error reading the data file " + fileName);
            return false;
        }
    }

    /**
     * Search through the resource directories to load the available (having a png
     * and dat file) tiles into the tileList Pane. Also load the associated data
     * from the dat file about the tile terrain.
     */
    public void loadTiles(){
        File dir = new File(tileListDirName);
        if(dir.isDirectory()){
            File[] directoryListing = dir.listFiles();
            for (File child : directoryListing) {
                if(child.getName().endsWith(".png")){
                    try {
                        String tileData = (tileListDirName + child.getName()).replace(tileListDirName, tileDatDirName).replace(".png", ".dat");
                        File datFile = new File(tileData);
                        if (datFile.isFile()) {
                            if (loadData(tileData)) {
                                tileList.add(tileListDirName + child.getName());
                            }
                        }
                    } catch (Exception e){
                        System.err.println(e.toString());
                    }
                }
            }
        }
    }

    /**
     * Notifies the TileListPane that a tile button in the pane has been clicked.
     * @param tile The tile that was clicked.
     */
    public void update(Tile tile){
        observer.updateBrushTile(tile);
    }


    public void register(EditorController editorController){
        this.observer = editorController;
    }
}
