package EditorPane;

import GUIs.*;
import GameBoard.GameMap;
import GameBoard.Terrain;
import GameBoard.Tile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditorController implements Initializable, BoardPaneObserver {

    @FXML private TabPane editorTab;
    @FXML private ToolPane toolPane;
    @FXML private TileListPane tileListPane;
    private ArrayList<GameMap> openMaps = new ArrayList<GameMap>();

    //Tool buttons and information
    private static final Integer SELECTOR = 0;
    private static final Integer BRUSHWI = 1;
    private static final Integer BRUSHWIW = 0;
    private static final Integer BRUSHWII = 2;
    private static final Integer BRUSHWIIW = 1;
    private static final Integer BRUSHWIII = 3;
    private static final Integer BRUSHWIIIW = 2;

    private Integer tool = 0; //The tool state is represented by an integer. 0 - selector

    //Holds the tile being currently used as the brush paint.
    private final String DEFAULTTILEDIR = "Resources/TileImages/grass.png";
    private final int DEFAULTTILEX = -1;
    private final int DEFAULTTILEY = -1;
    private Tile brushTile = new Tile(DEFAULTTILEDIR, DEFAULTTILEX, DEFAULTTILEY);

    //Current Tile and Associated information
    @FXML private ImageView selectedTile;
    @FXML private Label tileLocation;
    @FXML private Label tileDefMod;
    @FXML private Label tileEvaMod;
    @FXML private Label tileMovMod;
    @FXML private Label tileRetMod;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tileListPane.register(this);
        toolPane.register(this);
    }

    public void handleNewMap(ActionEvent actionEvent) {
        {
            final GameMap gameMap = new GameMap();
            Stage popup = new Stage();
            GridPane popupGrid = new GridPane();
            Label mapNameL = new Label( "Map Name: ");
            TextField mapName = new TextField(gameMap.getDEFAULTNAME());
            Label maxPlayersL = new Label("Number of Players: ");

            Button newMap = new Button("New Map");
            Button back = new Button("Back");

            ComboBox<Integer> maxPlayers = new ComboBox<Integer>();
            {
                for(int i = gameMap.getMINPLAYERS(); i <= gameMap.getMAXPLAYERS(); i++){
                    maxPlayers.getItems().add(new Integer(i));
                }
                maxPlayers.setValue(gameMap.getDEFAULTPLAYERS());
            }

            Label widthL = new Label("Width: ");
            ComboBox<Integer> width = new ComboBox<Integer>();
            {
                for(int i = gameMap.getMINWIDTH(); i <= gameMap.getMAXWIDTH(); i++){
                    width.getItems().add(new Integer(i));
                }
                width.setValue(gameMap.getDEFAULTWIDTH());
            }


            Label heightL =  new Label("Height: ");
            ComboBox<Integer> height = new ComboBox<Integer>();
            {
                for(int i = gameMap.getMINHEIGHT(); i <= gameMap.getMAXHEIGHT(); i++){
                    height.getItems().add(new Integer(i));
                }
                height.setValue(gameMap.getDEFAULTHEIGHT());
            }


            popupGrid.add(mapNameL,0,0,2,1);
            popupGrid.add(mapName,2,0,2,1);
            popupGrid.add(maxPlayersL,0,1,2,1);
            popupGrid.add(maxPlayers,2,1,2,1);
            popupGrid.add(widthL,0,2);
            popupGrid.add(heightL,2,2);
            popupGrid.add(width,1,2);
            popupGrid.add(height,3,2);

            Scene popupScene = new Scene(popupGrid, 300, 300);
            popup.setScene(popupScene);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.show();

            newMap.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                }
            });
        }

        /*
        editorTab.getTabs().add(new Tab("Untitled Map.amap"));
        openMaps.add(new GameMap());
        renderMap(openMaps.size()-1);
        updateCurrentTile(openMaps.get(openMaps.size()-1).getTiles()[0][0]); */
    }

    public void handleOpenMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Map to Open");
        fileChooser.setInitialDirectory(new File("src/Resources/Maps"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Alaesia Map Files (.amap)", "*.amap"));
        try{
            File mapFile = fileChooser.showOpenDialog(new Stage());
            if (mapFile != null){
                FileInputStream fileIn = new FileInputStream(mapFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                GameMap gameMap = (GameMap) in.readObject();
                in.close();
                fileIn.close();

                editorTab.getTabs().add(new Tab(mapFile.getName()));
                openMaps.add(gameMap);
                System.err.println(openMaps.size());
                renderMap(openMaps.size() - 1);
            }
        } catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void handleCloseMap(ActionEvent actionEvent) {
        Tab tabToRemove = null;
        for (Tab t:editorTab.getTabs()){
            if(t.isSelected()){
                System.err.println(openMaps.size());
                openMaps.remove(editorTab.getTabs().indexOf(t));
                System.err.println(openMaps.size());
                tabToRemove = t;
                break;
            }
        }
        if (tabToRemove != null){
            editorTab.getTabs().remove(tabToRemove);
        }
    }

    public void handleSaveMap(ActionEvent actionEvent) {
        for (Tab t:editorTab.getTabs()){
            if(t.isSelected()){
                try{
                    File mapFile = new File(t.getText());
                    FileOutputStream fileOut = new FileOutputStream(mapFile);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);

                    out.writeObject(((BoardPane)t.getContent()).getGameMap());
                    out.close();
                    fileOut.close();

                    t.setText(mapFile.getName());
                    break; //There's no need to continue searching the remaining tabs when we've found the selected one.
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }

    public void handleSaveAs(ActionEvent actionEvent) {
        for (Tab t:editorTab.getTabs()){
            if(t.isSelected()){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Enter a Name to Save As");
                fileChooser.setInitialDirectory(new File("src/Resources/Maps"));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Alaesia Map Files (.amap)", "*.amap"));
                try{
                    File mapFile = fileChooser.showSaveDialog(new Stage());
                    if (mapFile != null){
                        FileOutputStream fileOut = new FileOutputStream(mapFile);
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        System.err.println(((BoardPane)t.getContent()).getGameMap().toString());
                        out.writeObject(((BoardPane)t.getContent()).getGameMap());
                        out.close();
                        fileOut.close();

                        t.setText(mapFile.getName());
                    }
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
                break; //There's no need to continue searching the remaining tabs when we've found the selected one.
            }
        }
    }

    public void handleExitToMain(ActionEvent actionEvent) {
        Navigator.loadScene(Navigator.MAINMENU);
    }

    public void handleExitAlaesia(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void handleAbout(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Author: Johnathon Hoste\n" +
                "Editor for the Game Alaesia\n" + "Published 2014"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void renderMap(int index){
        Tab current = editorTab.getTabs().get(index);
        current.setContent(new BoardPane(openMaps.get(index), this));
    }

    public void updateCurrentTile(Tile tile){
        selectedTile.setImage(new Image(tile.getImageName()));
        if((tile.getX() == -1) && (tile.getY() == -1)){
            tileLocation.setText("In Tile List");
        } else {
            tileLocation.setText(tile.getX().toString() + ", " + tile.getY().toString());
        }
        tileDefMod.setText(tile.getTerrain().getDefMod().toString());
        tileEvaMod.setText(tile.getTerrain().getEvaMod().toString());
        tileMovMod.setText(tile.getTerrain().getMovMod().toString());
        tileRetMod.setText(tile.getTerrain().getRetMod().toString());
    }

    public void updateBoard(Tile tile){
        if(tool.equals(SELECTOR)) {
            updateCurrentTile(tile);
        } else if (tool.equals(BRUSHWI)){
            paintTile(tile, BRUSHWIW);
        } else if (tool.equals(BRUSHWII)){
            paintTile(tile, BRUSHWIIW);
        } else if (tool.equals(BRUSHWIII)){
            paintTile(tile, BRUSHWIIIW);
        }
    }

    public void updateBrushTile(Tile tile){
        brushTile = new Tile(tile);
        updateCurrentTile(brushTile);
    }

    public void updateTool(int tool){
        this.tool = tool;
    }

    public void paintTile(Tile tile, int radius){
        for (Tab t:editorTab.getTabs()){
            if(t.isSelected()){
                BoardPane boardPane = ((BoardPane) t.getContent());
                for(int y = tile.getY() - radius; y <= tile.getY() + radius; y++){
                    for(int x = tile.getX() - radius; x <= tile.getX() + radius; x++){
                        if((x>=0) && (y>=0) && (x < boardPane.getGameMap().getWidth()) && (y < boardPane.getGameMap().getHeight())){
                            if(x%2==1){
                                for(int r=0; r<=radius; r++) {
                                    if ((Math.abs(x - tile.getX()) <= r) && (y >= (tile.getY() - (radius - r/2)))
                                            && (y <= (tile.getY() + (radius - (r+1)/2)))) {
                                        boardPane.getGameMap().getTiles()[x][y].setTerrain(new Terrain(brushTile.getTerrain()));
                                        boardPane.getGameMap().getTiles()[x][y].setImageName(brushTile.getImageName());
                                        boardPane.getBoardPaneButtons()[x][y].getTile().setTerrain(new Terrain(brushTile.getTerrain()));
                                        boardPane.getBoardPaneButtons()[x][y].setImage(new Image(brushTile.getImageName()));
                                        break;
                                    }
                                }
                            } else {
                                for(int r=0; r<=radius; r++) {
                                    if ((Math.abs(x - tile.getX()) <= r) && (y >= (tile.getY() - (radius - (r+1)/2)))
                                            && (y <= (tile.getY() + (radius - r/2)))) {
                                        boardPane.getGameMap().getTiles()[x][y].setTerrain(new Terrain(brushTile.getTerrain()));
                                        boardPane.getGameMap().getTiles()[x][y].setImageName(brushTile.getImageName());
                                        boardPane.getBoardPaneButtons()[x][y].getTile().setTerrain(new Terrain(brushTile.getTerrain()));
                                        boardPane.getBoardPaneButtons()[x][y].setImage(new Image(brushTile.getImageName()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}