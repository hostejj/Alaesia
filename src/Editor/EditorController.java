package Editor;

import GUIs.BoardPane;
import GUIs.Navigator;
import GameBoard.GameMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class EditorController {

    @FXML
    private TabPane editorTab;

    private ArrayList<GameMap> openMaps = new ArrayList<GameMap>();

    public void handleNewMap(ActionEvent actionEvent) {
        editorTab.getTabs().add(new Tab("Untitled Map.amap"));
        openMaps.add(new GameMap());
        renderMap(openMaps.size()-1);
    }

    public void handleOpenMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Map to Open");
        fileChooser.setInitialDirectory(new File("Alaesia/src/Resources/Maps"));
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
                renderMap(openMaps.size()-1);
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
                openMaps.remove(((BoardPane)t.getContent()).getGameMap());
                tabToRemove = t;
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
                fileChooser.setInitialDirectory(new File("Alaesia/src/Resources/Maps"));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Alaesia Map Files (.amap)", "*.amap"));
                try{
                    File mapFile = fileChooser.showSaveDialog(new Stage());
                    if (mapFile != null){
                        FileOutputStream fileOut = new FileOutputStream(mapFile);
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);

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
        current.setContent(new BoardPane(openMaps.get(index)));
    }
}