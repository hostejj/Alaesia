package StagingPane;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StagingController implements Initializable{

    @FXML private OptionsPane optionsPane;
    @FXML private SelectionPane selectionPane;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        optionsPane.register(this);
        selectionPane.register(this);
    }
}
