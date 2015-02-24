package StagingPane;

import GUIs.Navigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StagingController implements Initializable{

    @FXML private OptionsPane optionsPane;
    @FXML private SelectionPane selectionPane;
    public static Integer START = 0;
    public static Integer BACK = 1;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        optionsPane.register(this);
        selectionPane.register(this);
    }

    public void update(Integer choice){
        if(choice == START){

        } else if (choice == BACK){
            Navigator.loadScene(Navigator.MAINMENU);
        }
    }
}
