package SPGamePane;

import GUIs.BoardPane;
import GUIs.BoardPaneObserver;
import GUIs.Navigator;
import GameBoard.Tile;
import GameConcepts.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class SPGamePaneController implements Initializable, BoardPaneObserver {
    private Game game;
    private BoardPane boardPane;

    @FXML private ScrollPane boardWrap;
    @FXML private VBox sideBar;

    public static final int PASS = 0;
    public static final int END = 1;
    public static final int SELECTED = 2;

    private HintBox hintBox;
    private SelectedPane selectedPane;
    private PlayerStatusPane playerStatusPane;
    private ButtonBox buttonBox;

    public SPGamePaneController(){}

    @Override
    public void initialize(URL location, ResourceBundle resources){
        loadGameData(); //loads the data for the game generated in the Staging Pane
        createSideBar();
        drawBoard();
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

    public void update(int choice){
        switch (choice){
            case PASS:
                break;
            case END:
                break;
            case SELECTED:
                for(Tab tab: playerStatusPane.getTabs()){
                    if(tab.isSelected()) {
                        tab = (PlayerStatusTab) tab;
                       // selectedPane.updateSelected(tab.);
                        break;//action complete
                    }
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

        hintBox.setPrefHeight(100);
        selectedPane.setPrefHeight(400);
        playerStatusPane.setPrefHeight(228);
        buttonBox.setPrefHeight(40);

        buttonBox.register(this);

        sideBar.getChildren().add(hintBox);
        sideBar.getChildren().add(selectedPane);
        sideBar.getChildren().add(playerStatusPane);
        sideBar.getChildren().add(buttonBox);
    }

    @Override
    public void updateBoard(Tile tile) {

    }
}