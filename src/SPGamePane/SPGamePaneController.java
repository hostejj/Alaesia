package SPGamePane;

import GUIs.*;
import GameBoard.MapCell;
import GameBoard.Tile;
import GameConcepts.Game;
import GameConcepts.Player;
import GameConcepts.Unit;
import StagingPane.PlayerTab;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SPGamePaneController implements Initializable, BoardPaneObserver {
    private Game game;
    private BoardPane boardPane;
    private BoardPaneButton selectedBoardPaneButton;
    private final String SHADENAME = "Resources/InterfaceImages/pshade";
    private SPGamePaneController self = this;
    private Unit unitToUse;

    @FXML private ScrollPane boardWrap;
    @FXML private VBox sideBar;

    public static final int PASS = 0;
    public static final int SELECTED = 1;
    public static final int BOARDSEL = 2;
    public static final int PLACE = 3;
    public static final int USE = 4;
    public static final int MOVE = 5;
    public static final int ATTACK = 6;
    public static final int END = 7;

    private HintBox hintBox;
    private SelectedPane selectedPane;
    private PlayerStatusPane playerStatusPane;
    private ButtonBox buttonBox;

    public SPGamePaneController(){}

    @Override
    public void initialize(URL location, ResourceBundle resources){
        loadGameData(); //loads the data for the game generated in the Staging Pane
        setupComps();
        createSideBar();
        drawBoard();
        addSLShades();
        setupPhaseShift();
    }

    public void handleExitToMain(ActionEvent actionEvent) {
        Navigator.loadScene(Navigator.MAINMENU);
    }

    public void handleExitAlaesia(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void loadGameData(){
        try{
            File temp = new File("Resources/startTemp.gam");
            FileInputStream fileIn = new FileInputStream(temp);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game = (Game) in.readObject();
            in.close();
            fileIn.close();

            game.setPropertiesOnLoad();
        } catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void drawBoard(){
        boardPane = new BoardPane(game.getGameMap(),this);
        boardWrap.setContent(boardPane);
    }

    public void addSLShades(){
        for(int i = 0; i < game.getGameMap().getStartLocs().size(); i++){
            for(MapCell mc: game.getGameMap().getStartLocs().get(i)){
                try{
                    String filename = SHADENAME + (i + 1) + ".png";
                    Image image = new Image(new File(filename).toURI().toString());
                    boardPane.getBoardPaneButtons()[mc.getTile().getX()][mc.getTile().getY()].addShade(i, image);
                } catch (Exception e){
                    System.err.println(e.toString());
                }
            }
        }
    }

    public void update(int choice){
        switch (choice){
            case PASS:
                if(Game.GameState.UNITMOVE  == game.getGameState()) {
                    invalidPopup("Cannot end the turn while you still have a unit in action.");
                    break;
                }
                game.nextPlayer();
                break;
            case SELECTED:
                for(Tab tab: playerStatusPane.getTabs()){
                    if(tab.isSelected()) {
                        Unit unit = ((PlayerStatusTab) tab).getUnitListV().getSelectionModel().getSelectedItem();
                        MapCell mapCell = ((PlayerStatusTab) tab).getGame().getGameMap().locateUnit(unit);
                        if(mapCell != null){
                            selectedPane.updateSelected(mapCell.getTile(),unit,((PlayerStatusTab) tab).getPlayer());
                            highlightAvailable(unit);
                        } else {
                            selectedPane.updateSelected(null, unit, ((PlayerStatusTab) tab).getPlayer());
                        }
                        break;//action complete
                    }
                }
                break;
            case BOARDSEL:
                if(selectedBoardPaneButton.getUnit() != null){
                    selectedPane.updateSelected(selectedBoardPaneButton.getTile(), selectedBoardPaneButton.getUnit(),
                            game.findOwner(selectedBoardPaneButton.getUnit()));
                    highlightAvailable(selectedBoardPaneButton.getUnit());
                } else {
                    selectedPane.updateSelected(selectedBoardPaneButton.getTile(), selectedBoardPaneButton.getUnit(),
                            null);
                    ownerHighlights();
                }
                break;
            case PLACE:
                game.placeUnit(selectedPane.getSelectedUnit(), selectedBoardPaneButton.getTile());
                if(game.isErrorFlag()){
                    invalidPopup(game.getErrorMessage());
                    game.setErrorFlag(false);
                } else {
                    selectedBoardPaneButton.getUnitImage().setImage(new Image(
                            new File(selectedBoardPaneButton.getUnit().getImageName()).toURI().toString()));
                    game.nextPlayer();
                }
                break;
            case USE:
                if(unitToUse != null){
                    if(!game.getCurrentPlayer().getArmy().contains(unitToUse)) {
                        invalidPopup("You do not own this unit.");
                        break;
                    }
                    if(game.getCurrentPlayer().getTurnPoints() < unitToUse.getCPA()){
                        invalidPopup("You do not have enough action points.");
                        break;
                    }
                    if(unitToUse.getCurACT() <= 0) {
                        invalidPopup("The selected unit does not have enough actions.");
                        break;
                    }
                    game.getCurrentPlayer().setTurnPoints(game.getCurrentPlayer().getTurnPoints() - unitToUse.getCPA());
                    ((PlayerStatusTab) playerStatusPane.getTabs().get(game.getPlayerIndex(game.getCurrentPlayer()))).setTurnPoints(
                            game.getCurrentPlayer().getTurnPoints().toString() + "/" + game.getMaxTurnPoints()
                    );
                    game.getCurrentPlayer().setUnitInUse(unitToUse);
                    highlightAvailable(unitToUse);
                    game.getCurrentPlayer().getUnitInUse().setCurACT(game.getCurrentPlayer().getUnitInUse().getCurACT() - 1);
                    game.setGameState(Game.GameState.UNITMOVE);
                }
                break;
            case MOVE:
                MapCell sourceMC = game.getGameMap().locateUnit(game.getCurrentPlayer().getUnitInUse());
                BoardPaneButton sourceBPB = boardPane.getBoardPaneButtons()[sourceMC.getTile().getX()][sourceMC.getTile().getY()];
                game.moveUnit(selectedBoardPaneButton.getMapCell());
                if(game.isErrorFlag()){
                    invalidPopup(game.getErrorMessage());
                    game.setErrorFlag(false);
                } else {
                    sourceBPB.getUnitImage().setImage(null);
                    selectedBoardPaneButton.getUnitImage().setImage(new Image(
                            new File(selectedBoardPaneButton.getUnit().getImageName()).toURI().toString()));
                    highlightAvailable(selectedBoardPaneButton.getUnit());
                    if(selectedBoardPaneButton.getUnit().getCurMOV() == 0){
                        update(END);
                    }
                }
                break;
            case ATTACK:
                //attack the target mapcell
                game.attackUnit(selectedBoardPaneButton.getMapCell());
                if(game.isErrorFlag()){
                    invalidPopup(game.getErrorMessage());
                    game.setErrorFlag(false);
                    break;
                } else {
                    //check for two different deaths
                    if (game.getCurrentPlayer().getUnitInUse().getCurHP() <= 0) {
                        Tile unitTile = game.getGameMap().locateUnit(game.getCurrentPlayer().getUnitInUse()).getTile();
                        game.unitDeath(game.getCurrentPlayer().getUnitInUse());
                        boardPane.getBoardPaneButtons()[unitTile.getX()][unitTile.getY()].getUnitImage().setImage(null);
                    }
                    if (selectedBoardPaneButton.getMapCell().getUnit().getCurHP() <= 0) {
                        game.unitDeath(selectedBoardPaneButton.getMapCell().getUnit());
                        selectedBoardPaneButton.getUnitImage().setImage(null);
                    }
                }
            case END:
                game.getCurrentPlayer().setUnitInUse(null);
                if(game.getGameState() != Game.GameState.GAMEOVER) {
                    game.setGameState(Game.GameState.BATTLE);
                }
                break;
            default:
                break;
        }
    }

    public void createSideBar(){
        hintBox = new HintBox();
        selectedPane = new SelectedPane();
        playerStatusPane = new PlayerStatusPane(game, this);
        buttonBox = new ButtonBox();

        hintBox.setPrefHeight(178);
        selectedPane.setPrefHeight(350);
        playerStatusPane.setPrefHeight(200);
        buttonBox.setPrefHeight(40);

        buttonBox.register(this);

        sideBar.getChildren().add(hintBox);
        sideBar.getChildren().add(selectedPane);
        sideBar.getChildren().add(playerStatusPane);
        sideBar.getChildren().add(buttonBox);
    }

    @Override
    public void updateBoard(BoardPaneButton boardPaneButton, Integer click) {
        if(click == boardPaneButton.LEFTCLICK){
            selectedBoardPaneButton = boardPaneButton;
            update(BOARDSEL);
        } else if (click == boardPaneButton.RIGHTCLICK){
            selectedBoardPaneButton = boardPaneButton;
            unitToUse = selectedBoardPaneButton.getUnit();
            BoardContextMenu contextMenu = new BoardContextMenu(this);
            contextMenu.show(boardPaneButton, Side.RIGHT, 0, 20);
        }
    }

    /**
     * Displays a popup to the user with the reason that an action
     * is currently invalid.
     */
    public void invalidPopup(String message){
        final Stage popup = new Stage();
        GridPane popupGrid = new GridPane();
        Label messageL = new Label(message);
        Button okay = new Button("Ok");
        popup.getIcons().setAll(new Image(new File("Resources/InterfaceImages/IconImage.png").toURI().toString()));

        popupGrid.setPadding(new Insets(10,10,10,10));
        messageL.setWrapText(true);
        okay.setPrefWidth(Integer.MAX_VALUE);
        okay.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popup.close();
            }
        });

        popupGrid.add(messageL,0,0);
        popupGrid.add(okay,0,1);

        Scene scene = new Scene(popupGrid, 200, 100);
        popup.setScene(scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.show();
    }

    private void setupComps() {
        game.currentPlayerProperty().addListener(new ChangeListener<Object>(){
            @Override
            public void changed(ObservableValue<? extends Object> observable,
                                Object oldValue, Object newValue) {
                hintBox.setPlayerName(game.getCurrentPlayer().getPlayerName());
                if (!game.getCurrentPlayer().isHuman()) {
                    if(game.getGameState() == Game.GameState.PLACEMENTPHASE){
                        Player player = game.getCurrentPlayer();
                        if(game.getCurrentPlayer().makeMove().placeUnit(game)){
                            displayPlaceUnit(player);
                        }
                        game.nextPlayer();
                    }
                    else if(game.getGameState() == Game.GameState.BATTLE){
                        while (game.getCurrentPlayer().makeMove().moveExists()){

                        }
                        game.nextPlayer();
                    } else if(game.getGameState() == Game.GameState.UNITMOVE){
                        System.err.println("Error phase");
                    } else if(game.getGameState() == Game.GameState.GAMEOVER){
                        System.err.println("Error phase");
                    }
                    // Not really necessary to choose the move in a application-threaded task, but this is to
                    // demonstrate how to implement this for a game where choosing a move may take a long time.
                    // In reality here you could just do
                    // game.makeMove(computerPlayer, computerPlayer.chooseMove(game));

                    /*
                    final Task<Location> computerMoveTask = new Task<Location>() {
                        @Override
                        public Location call() throws Exception {
                            return computerPlayer.chooseMove(game);
                        }
                    };
                    computerMoveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            game.makeMove(computerPlayer, computerMoveTask.getValue());
                        }
                    });
                    executorService.submit(computerMoveTask);
                    */
                } else {
                    if(game.getGameState() == Game.GameState.PLACEMENTPHASE){
                        boolean unitsLeft = false;
                        for(Unit u: game.getCurrentPlayer().getArmy()){
                            if(game.getGameMap().locateUnit(u) == null){
                                unitsLeft = true;
                                break;
                            }
                        }
                        if(!unitsLeft){
                            game.nextPlayer();
                        }
                    }
                }
            }
        });
    }

    private void setupPhaseShift(){
        game.gameStateProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable,
                                Object oldValue, Object newValue) {
                if (game.getGameState() == Game.GameState.PLACEMENTPHASE) {
                } else if (game.getGameState() == Game.GameState.BATTLE) {
                    hintBox.setHint("Battle Phase: Select a unit to see its available movement. Units can be selected " +
                            "via the player tabs or the map. Right click-the map and the use button to select the unit " +
                            "for use.");
                    clearHighlights();
                } else if (game.getGameState() == Game.GameState.UNITMOVE) {
                    hintBox.setHint("Moving Unit: Right click on a highlighted tile and select a choice for the unit.");
                } else if (game.getGameState() == Game.GameState.GAMEOVER) {
                    hintBox.setHint("Game Over: " + game.getWinner().getPlayerName() + " has won.");
                }
            }
        });
    }

    public void displayPlaceUnit(Player player){
        Unit unit = player.makeMove().getNeededUnit();
        Tile tile = player.makeMove().getNeededTile();

        for(BoardPaneButton[] boardPaneButtonX: boardPane.getBoardPaneButtons()){
            for(BoardPaneButton boardPaneButtonY: boardPaneButtonX){
                if(boardPaneButtonY.getTile() == tile){
                    boardPaneButtonY.getUnitImage().setImage(new Image(
                            new File(unit.getImageName()).toURI().toString()));
                }
            }
        }
    }

    public void highlightAvailable(Unit u){
        if(u != null){
            if(game.getGameState() != Game.GameState.PLACEMENTPHASE){
                Integer pindex = game.getPlayerIndex(game.findOwner(u));
                clearHighlights();
                ArrayList<MapCell> actionableMCs = game.actionableMCs(u);
                for (MapCell mc: actionableMCs){
                    if(mc.getUnit() == null){
                        boardPane.getBoardPaneButtons()[mc.getTile().getX()][mc.getTile().getY()].addShade(pindex,
                                new Image(new File(SHADENAME + (pindex + 1) + ".png").toURI().toString()));
                    } else {
                        Integer oppIndex = game.getPlayerIndex(game.findOwner(mc.getUnit()));
                        boardPane.getBoardPaneButtons()[mc.getTile().getX()][mc.getTile().getY()].addShade(oppIndex,
                                new Image(new File(SHADENAME + (oppIndex + 1) + ".png").toURI().toString()));
                    }
                }
            }
        }
    }

    public void clearHighlights(){
        if(game.getGameState() != Game.GameState.PLACEMENTPHASE){
            for(BoardPaneButton[] bpba: boardPane.getBoardPaneButtons()){
                for(BoardPaneButton bpb: bpba){
                    bpb.removeAllShades();
                }
            }
        }
    }

    public void ownerHighlights(){
        clearHighlights();
        if(game.getGameState() == Game.GameState.BATTLE){
            for(BoardPaneButton[] bpba: boardPane.getBoardPaneButtons()){
                for(BoardPaneButton bpb: bpba){
                    if(bpb.getUnit() != null){
                        if(bpb.getUnit().getCurACT() > 0) {
                            Integer pindex = game.getPlayerIndex(game.findOwner(bpb.getUnit()));
                            bpb.addShade(pindex, new Image(new File(SHADENAME + (pindex + 1) + ".png").toURI().toString()));
                        }
                    }
                }
            }
        } else if(game.getGameState() == Game.GameState.UNITMOVE){
            MapCell mc = game.getGameMap().locateUnit(game.getCurrentPlayer().getUnitInUse());
            Integer pindex = game.getPlayerIndex(game.findOwner(game.getCurrentPlayer().getUnitInUse()));
            boardPane.getBoardPaneButtons()[mc.getTile().getX()][mc.getTile().getY()].addShade(pindex, new Image(
                    new File(SHADENAME + (pindex + 1) + ".png").toURI().toString()));
        }
    }

    public Game getGame() {
        return game;
    }
}