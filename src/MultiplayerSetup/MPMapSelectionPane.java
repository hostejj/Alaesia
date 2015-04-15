package MultiplayerSetup;

import GUIs.BoardPane;
import GameBoard.GameMap;
import GameConcepts.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.ArrayList;

public class MPMapSelectionPane extends GridPane {
    private static String MAPDIRNAME = "Resources/Maps/";
    private MPStagingController observer;
    private final Label mapLabel = new Label("Map:   ");
    private final ComboBox<String> mapSelector = new ComboBox<String>();

    private final Label numPlayersL = new Label("Number of Players: ");
    private final Label numPlayers = new Label("-");
    private final Label sizeL = new Label("Size: ");
    private final Label size = new Label("-,-");
    private final ArrayList<Label> playerSLL = new ArrayList<Label>();
    private final ArrayList<Label> playerSL = new ArrayList<Label>();

    private GameMap currentMapSel;

    public MPMapSelectionPane(){
        loadMapNames();

        add(mapLabel,0,0);
        add(mapSelector,1,0);
        add(numPlayersL,0,1);
        add(numPlayers,1,1);
        add(sizeL,0,2);
        add(size,1,2);

        mapSelector.setDisable(true);
        mapSelector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentMapSel != null){
                    removeCurrentMapData();
                }
                setMapData(mapSelector.getValue() + ".amap");
            }
        });
    }

    public void loadMapNames(){
        File dir = new File(MAPDIRNAME);
        if(dir.isDirectory()){
            File[] directoryListing = dir.listFiles();
            for (File child : directoryListing) {
                if(child.getName().endsWith(".amap")){
                    mapSelector.getItems().addAll(child.getName().replace(".amap", ""));
                }
            }
        }
    }

    public void setMapData(String mapName){
        try{
            FileInputStream fileIn = new FileInputStream(MAPDIRNAME + mapName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GameMap gameMap = (GameMap) in.readObject();
            in.close();
            fileIn.close();

            currentMapSel = gameMap;

            numPlayers.setText(((Integer) gameMap.getMaxPlayers()).toString());
            for (int i = 0; i < gameMap.getMaxPlayers(); i++){
                Integer playerNum = i + 1;
                playerSLL.add(new Label("Player " + playerNum.toString() + " Starting Locations: "));
                playerSL.add(new Label(((Integer) gameMap.getStartLocs().get(i).size()).toString()));


                add(playerSLL.get(i), 0, 3 + i);
                add(playerSL.get(i), 1, 3+i);
            }
            size.setText(((Integer) gameMap.getWidth()).toString() + ", " + ((Integer) gameMap.getHeight()).toString());
        } catch (Exception e){
            System.err.println(e.toString());
        }

    }

    public void setMapData(GameMap gameMap){
        currentMapSel = gameMap;
        removeCurrentMapData();

        numPlayers.setText(((Integer) gameMap.getMaxPlayers()).toString());
        for (int i = 0; i < gameMap.getMaxPlayers(); i++){
            Integer playerNum = i + 1;
            playerSLL.add(new Label("Player " + playerNum.toString() + " Starting Locations: "));
            playerSL.add(new Label(((Integer) gameMap.getStartLocs().get(i).size()).toString()));


            add(playerSLL.get(i), 0, 3 + i);
            add(playerSL.get(i), 1, 3+i);
        }
        size.setText(((Integer) gameMap.getWidth()).toString() + ", " + ((Integer) gameMap.getHeight()).toString());
        mapSelector.setValue(gameMap.getMapName());
    }

    public boolean mapExists(GameMap gameMap){
        String fileName = MAPDIRNAME + gameMap.getMapName() + ".amap";
        File dir = new File(MAPDIRNAME);
        if(dir.isDirectory()){
            File[] directoryListing = dir.listFiles();
            for (File child : directoryListing) {
                if(child.getName().equals(fileName)){
                    return true;
                }
            }
        }
        return false;
    }

    public void saveMap(GameMap gameMap){
        String fileName = gameMap.getMapName() + ".amap";
        try{
            File mapFile = new File(fileName);
            FileOutputStream fileOut = new FileOutputStream(MAPDIRNAME + mapFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(gameMap);
            out.close();
            fileOut.close();

        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void removeCurrentMapData(){
        numPlayers.setText("-");
        size.setText("-,-");

        for(Label l: playerSLL){
            this.getChildren().remove(l);
        }
        for(Label l: playerSL) {
            this.getChildren().remove(l);
        }

        playerSL.clear();
        playerSLL.clear();
    }

    public void register(MPStagingController mpStagingController){
        this.observer = mpStagingController;
    }

    public void hostPrivileges(){
        mapSelector.setDisable(false);
    }

    public GameMap getCurrentMapSel() {
        return currentMapSel;
    }
}
