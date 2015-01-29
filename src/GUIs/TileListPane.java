package GUIs;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.File;
import java.util.ArrayList;

public class TileListPane extends GridPane {
    private final String tileListDirName = "src/Resources/TileImages/";
    private final String listDirName = "Resources/TileImages/";
    private final int MAXWIDTH = 3;
    private static int columnIndex = 0;
    private static int rowIndex = 0;

    public ArrayList<String> tileList = new ArrayList<String>();

    public TileListPane(){
        super();
        loadTiles();
        addTiles();
    }

    public void loadTiles(){
        File dir = new File(tileListDirName);
        if(dir.isDirectory()){
            File[] directoryListing = dir.listFiles();
            for (File child : directoryListing) {
                if(child.getName().endsWith(".png")){
                    tileList.add(listDirName + child.getName());
                }
            }
        }
    }

    public void addTiles(){
        for(String name: tileList){
            if(columnIndex > MAXWIDTH){
                columnIndex = 0;
                rowIndex++;
            }
            Button button = new Button();
            button.setGraphic(new ImageView(new Image(name)));
            add(button , columnIndex++, rowIndex);
        }
    }
}
