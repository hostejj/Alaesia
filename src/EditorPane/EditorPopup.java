package EditorPane;

import GameBoard.GameMap;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditorPopup extends Stage {
    private EditorController observer;

    public EditorPopup(){
        super();

        final GameMap gameMap = new GameMap();
        GridPane popupGrid = new GridPane();
        Label mapNameL = new Label( "Map Name: ");
        final TextField mapName = new TextField(gameMap.getDEFAULTNAME());
        Label maxPlayersL = new Label("Number of Players: ");

        Button newMap = new Button("New Map");
        Button back = new Button("Back");

        final ComboBox<Integer> maxPlayers = new ComboBox<Integer>();
        {
            for(int i = gameMap.getMINPLAYERS(); i <= gameMap.getMAXPLAYERS(); i++){
                maxPlayers.getItems().add(new Integer(i));
            }
            maxPlayers.setValue(gameMap.getDEFAULTPLAYERS());
        }

        Label widthL = new Label("Width: ");
        final ComboBox<Integer> width = new ComboBox<Integer>();
        {
            for(int i = gameMap.getMINWIDTH(); i <= gameMap.getMAXWIDTH(); i++){
                width.getItems().add(new Integer(i));
            }
            width.setValue(gameMap.getDEFAULTWIDTH());
        }


        Label heightL =  new Label("Height: ");
        final ComboBox<Integer> height = new ComboBox<Integer>();
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
        popupGrid.add(newMap,0,3,2,1);
        popupGrid.add(back,2,3,2,1);


        Scene scene = new Scene(popupGrid, 300, 200);
        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);

        newMap.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                observer.createNewMap(mapName.getText(), width.getValue(), height.getValue(), maxPlayers.getValue());
                close();
            }
        });

        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close();
            }
        });
    }

    public void register(EditorController editorController){
        this.observer = editorController;
    }
}
