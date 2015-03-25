package StagingPane;

import GUIs.Navigator;
import GameBoard.GameMap;
import GameConcepts.ComputerPlayerB;
import GameConcepts.Game;
import GameConcepts.HumanPlayerB;
import GameConcepts.Unit;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class StagingController implements Initializable{

    @FXML private OptionsPane optionsPane;
    @FXML private SelectionPane selectionPane;
    @FXML private MapSelectionPane mapSelPane;
    @FXML private PlayerPane playerPane;
    public static Integer BACK = 0;
    public static Integer SEL = 1;
    public static Integer START = 2;
    public static Integer ADD = 3;

    private Game game;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        optionsPane.register(this);
        selectionPane.register(this);
        mapSelPane.register(this);
        playerPane.register(this);
    }

    public void update(Integer choice){
        if(choice == BACK){
            //reset panes
            Navigator.loadScene(Navigator.MAINMENU);
        } else if (choice == SEL){
            if(mapSelPane.getCurrentMapSel() != null){
                this.game = new Game(new GameMap(mapSelPane.getCurrentMapSel()),
                        ((Double) optionsPane.getArmyPointsSlide().getValue()).intValue(),
                        ((Double) optionsPane.getTurnPointsSlide().getValue()).intValue(),
                        ((Double) optionsPane.getTurnLimitSlide().getValue()).intValue());
                playerPane.resetPlayerPane();
            }
        } else if (choice == START){
            if(startValid()){
                saveStart();
                Navigator.loadScene(Navigator.SPGAME);
            }
        } else if (choice == ADD){
            if(playerPane != null) {
                for (Tab pT : playerPane.getTabs()) {
                    if(pT.isSelected()){
                        ((PlayerTab) pT).addUnit(selectionPane.getSelectedUnit());
                        break; // no need to continue searching if already found
                    }
                }
            }
        }
    }

    public boolean startValid(){
        if(playerPane == null) return false;
        if(game == null) return false;

        for (Tab pT : playerPane.getTabs()) {
            if(((PlayerTab) pT).getArmyValue() <= 0){
                invalidPopup(pT.getText(),0);
                return false;
            }
            if(((PlayerTab) pT).getArmyValue() > game.getMaxArmyValue()){
                invalidPopup(pT.getText(),1);
                return false;
            }
            if(((PlayerTab) pT).getUnitsList().size() > ((PlayerTab) pT).getMaxUnits()){
                invalidPopup(pT.getText(),2);
                return false;
            }
        }

        return true;
    }

    /**
     * Displays a popup to the user with the reason that the game start
     * is currently invalid.
     */
    public void invalidPopup(String name, int choice){
        String message = "";
        switch(choice){
            case 0:
                message = "The game is unable to start because " + name + " does not have any units.";
                break;
            case 1:
                message = "The game is unable to start because " + name + " has too valuable of an army.";
                break;
            case 2:
                message = "The game is unable to start because " + name + " has too many units.";
                break;
            default:
                break;
        }


        final Stage popup = new Stage();
        GridPane popupGrid = new GridPane();
        Label messageL = new Label(message);
        Button okay = new Button("Ok");

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

    /**
     * Saves the data for the setup game into a temp file. This
     * file is loaded into the game scene upon initialization.
     */
    public void saveStart(){
        populateGameData();
        try{
            File temp = new File("startTemp.gam");
            FileOutputStream fileOut = new FileOutputStream("Resources/" + temp);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(game);
            out.close();
            fileOut.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Stores the data chosen by the user about the different
     * players into the game data.
     */
    public void populateGameData(){
        for (int i = 0; i < game.getPlayers().size(); i++) {
            game.getPlayers().get(i).setPlayerName(playerPane.getTabs().get(i).getText());
            for (Unit unit : ((PlayerTab) playerPane.getTabs().get(i)).getUnitsList()) {
                game.getPlayers().get(i).getArmy().add(unit);
            }
            //for single player only the first player is human
            if(i == 0){
                game.getPlayers().get(i).setPlayerBehavior(new HumanPlayerB());
            } else {
                game.getPlayers().get(i).setPlayerBehavior(new ComputerPlayerB());
            }
        }
    }

    public Game getGame() {
        return game;
    }
}
