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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;

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
    private Integer tool = 0; //The tool state is represented by an integer.
    private static final Integer SELECTOR = 0;
    private static final Integer BRUSHWI = 1;
    private static final Integer BRUSHWIW = 0;
    private static final Integer BRUSHWII = 2;
    private static final Integer BRUSHWIIW = 1;
    private static final Integer BRUSHWIII = 3;
    private static final Integer BRUSHWIIIW = 2;
    private static final Integer SLBRUSH = 4;

    //Player Starting Location tool color chooser.
    @FXML private Button playerSLC = new Button();
    private static Integer playerSLCState = 1;
    private String SLSTRING = "Resources/InterfaceImages/pslshade";
    private String pslStyleOne = "-fx-text-fill: blue; -fx-font-size: 18px; -fx-font-weight: bold;";
    private String pslStyleTwo = "-fx-text-fill: red; -fx-font-size: 18px; -fx-font-weight: bold;";
    private String pslStyleThree = "-fx-text-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;";
    private String pslStyleFour = "-fx-text-fill: orange; -fx-font-size: 18px; -fx-font-weight: bold;";


    private static final int COMPMINW = 40;
    private static final int COMPMAXW = 40;
    private static final int COMPMINH = 40;
    private static final int COMPMAXH = 40;

    //Holds the tile being currently used as the brush paint.
    private final String DEFAULTTILEDIR = "/TileImages/grass.png";
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
        //register as and observer
        tileListPane.register(this);
        toolPane.register(this);


        // component styles
        playerSLC.setMinSize(COMPMINW, COMPMINH);
        playerSLC.setMaxSize(COMPMAXW, COMPMAXH);
        playerSLC.setText(((Integer)playerSLCState).toString());
        playerSLC.setStyle(pslStyleOne);

        //component actions
        playerSLC.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Integer openIndex = null;
                for (Tab t:editorTab.getTabs()){
                    if(t.isSelected()){
                        openIndex = editorTab.getTabs().indexOf(t);
                        break;
                    }
                }
                if(openIndex != null) {
                    if (playerSLCState < ((BoardPane) ((ScrollPane) editorTab.getTabs().get(openIndex).getContent()).getContent()).getGameMap().getMaxPlayers()){
                        playerSLCState++;
                    } else {
                        playerSLCState = 1;
                    }
                    playerSLC.setText(((Integer) playerSLCState).toString());

                    if(playerSLCState == 1){
                        playerSLC.setStyle(pslStyleOne);
                    } else if(playerSLCState == 2){
                        playerSLC.setStyle(pslStyleTwo);
                    } else if(playerSLCState == 3){
                        playerSLC.setStyle(pslStyleThree);
                    } else if(playerSLCState == 4){
                        playerSLC.setStyle(pslStyleFour);
                    }
                }
            }
        });
    }

    public void handleNewMap(ActionEvent actionEvent) {
        EditorPopup editorPopup = new EditorPopup();
        editorPopup.register(this);
        editorPopup.show();
    }

    public void handleOpenMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Map to Open");
        fileChooser.setInitialDirectory(new File("Resources/Maps"));
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
                openMaps.remove(editorTab.getTabs().indexOf(t));
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
                    FileOutputStream fileOut = new FileOutputStream("Resources/Maps/" + mapFile);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);

                    out.writeObject(((BoardPane) ((ScrollPane) t.getContent()).getContent()).getGameMap());
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
                fileChooser.setInitialDirectory(new File("Resources/Maps"));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Alaesia Map Files (.amap)", "*.amap"));
                try{
                    File mapFile = fileChooser.showSaveDialog(new Stage());
                    if (mapFile != null){
                        FileOutputStream fileOut = new FileOutputStream(mapFile);
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(((BoardPane)((ScrollPane)t.getContent()).getContent()).getGameMap());
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
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        BoardPane boardPane = new BoardPane(openMaps.get(index), this);
        scrollPane.setContent(boardPane);
        current.setContent(scrollPane);
        for(int i = 0; i < boardPane.getGameMap().getMaxPlayers(); i++) {
            for (Tile sltile : boardPane.getGameMap().getStartLocs().get(i)){
                //add color the tile on the editor
                try {
                    String shadeName = SLSTRING + ((Integer) (i + 1)).toString() + ".png";
                    ImageView shadeImage = new ImageView(new Image(new File(shadeName).toURI().toString()));
                    boardPane.getBoardPaneButtons()[sltile.getX()][sltile.getY()].addShade(i, shadeImage);
                } catch (IllegalArgumentException iae){
                    System.err.println(iae.toString());
                }
            }
        }
    }

    public void updateCurrentTile(Tile tile){
        try {
            selectedTile.setImage(new Image(new File(tile.getImageName()).toURI().toString()));
            if ((tile.getX() == -1) && (tile.getY() == -1)) {
                tileLocation.setText("In Tile List");
            } else {
                tileLocation.setText(tile.getX().toString() + ", " + tile.getY().toString());
            }
            tileDefMod.setText(tile.getTerrain().getDefMod().toString());
            tileEvaMod.setText(tile.getTerrain().getEvaMod().toString());
            tileMovMod.setText(tile.getTerrain().getMovMod().toString());
            tileRetMod.setText(tile.getTerrain().getRetMod().toString());
        } catch (IllegalArgumentException iae){
            System.err.println(iae.toString());
        }

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
        } else if (tool.equals(SLBRUSH)){
            changeStartingLocation(tile);
        }
    }

    public void updateBrushTile(Tile tile){
        brushTile = new Tile(tile);
        updateCurrentTile(brushTile);
    }

    public void updateTool(int tool){
        this.tool = tool;
    }

    public void createNewMap(String mapName, Integer width, Integer height, Integer maxPlayers){
        editorTab.getTabs().add(new Tab(mapName + ".amap"));
        openMaps.add(new GameMap(mapName, width, height, maxPlayers));
        renderMap(openMaps.size()-1);
        updateCurrentTile(openMaps.get(openMaps.size()-1).getTiles()[0][0]);
    }

    public void paintTile(Tile tile, int radius){
        for (Tab t:editorTab.getTabs()){
            if(t.isSelected()){
                BoardPane boardPane = ((BoardPane) (((ScrollPane) t.getContent())).getContent());
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
                                        boardPane.getBoardPaneButtons()[x][y].getTile().setImageName(brushTile.getImageName());
                                        boardPane.getBoardPaneButtons()[x][y].getTileImage().setImage(
                                                new Image(new File( brushTile.getImageName()).toURI().toString()));
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
                                        boardPane.getBoardPaneButtons()[x][y].getTile().setImageName(brushTile.getImageName());
                                        boardPane.getBoardPaneButtons()[x][y].getTileImage().setImage(
                                                new Image(new File( brushTile.getImageName()).toURI().toString()));
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

    //adds or removes a starting location from the map
    public void changeStartingLocation(Tile tile){
        for (Tab t:editorTab.getTabs()) {
            if (t.isSelected()) {
                BoardPane boardPane = ((BoardPane) (((ScrollPane) t.getContent())).getContent());
                if(playerSLCState <= boardPane.getGameMap().getMaxPlayers()){
                    //check if the tile is already in the list
                    for(Tile cltile: boardPane.getGameMap().getStartLocs().get(playerSLCState - 1)){
                        if(cltile.equals(tile)){
                            //remove the tile
                            boardPane.getGameMap().getStartLocs().get(playerSLCState - 1).remove(cltile);

                            //remove the coloring on the editor
                            boardPane.getBoardPaneButtons()[tile.getX()][tile.getY()].removeShade(playerSLCState - 1);

                            return;
                        }
                    }

                    //the tile was not found in the list already, so add the tile
                    boardPane.getGameMap().getStartLocs().get(playerSLCState - 1).add(tile);

                    //add color the tile on the editor
                    try {
                        String shadeName = SLSTRING + playerSLCState.toString() + ".png";
                        ImageView shadeImage = new ImageView(new Image(new File(shadeName).toURI().toString()));
                        boardPane.getBoardPaneButtons()[tile.getX()][tile.getY()].addShade(playerSLCState - 1, shadeImage);
                    } catch (IllegalArgumentException iae){
                        System.err.println(iae.toString());
                    }
                }
                break;
            }
        }
    }
}