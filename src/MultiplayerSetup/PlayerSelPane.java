package MultiplayerSetup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

public class PlayerSelPane extends GridPane{
    MPStagingController observer;

    private Label nameLabel = new Label("Name");
    private Label name1Label = new Label("CLOSED");
    private Label name2Label = new Label("CLOSED");
    private Label name3Label = new Label("CLOSED");
    private Label name4Label = new Label("CLOSED");
    private Label playerLabel = new Label("Player");
    private Label player1Label = new Label("1");
    private Label player2Label = new Label("2");
    private Label player3Label = new Label("3");
    private Label player4Label = new Label("4");
    private Label comboLabel = new Label("Type");
    private ObservableList<String> comboItems =  FXCollections.observableArrayList("OPEN", "CLOSED", "COMPUTER", "FILLED");
    private ComboBox<String> combo1 = new ComboBox<String>(comboItems);
    private ComboBox<String> combo2 = new ComboBox<String>(comboItems);
    private ComboBox<String> combo3 = new ComboBox<String>(comboItems);
    private ComboBox<String> combo4 = new ComboBox<String>(comboItems);


    public PlayerSelPane(){
        combo2.getSelectionModel().select(1);
        combo3.getSelectionModel().select(1);
        combo4.getSelectionModel().select(1);

        combo2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                observer.update(observer.PLAYTYPE);
            }
        });

        combo3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                observer.update(observer.PLAYTYPE);
            }
        });

        combo4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                observer.update(observer.PLAYTYPE);
            }
        });

        add(nameLabel, 0, 0);
        add(name1Label,0,1);
        add(name2Label,0,2);
        add(name3Label,0,3);
        add(name4Label,0,4);
        add(playerLabel,1,0);
        add(player1Label,1,1);
        add(player2Label,1,2);
        add(player3Label,1,3);
        add(player4Label,1,4);
        add(comboLabel,2,0);
        add(combo1,2,1);
        add(combo2,2,2);
        add(combo3,2,3);
        add(combo4,2,4);
    }

    public void register(MPStagingController mpStagingController){
        this.observer = mpStagingController;
    }

    public Label getName1Label() {
        return name1Label;
    }

    public Label getName2Label() {
        return name2Label;
    }

    public Label getName3Label() {
        return name3Label;
    }

    public Label getName4Label() {
        return name4Label;
    }

    public ComboBox<String> getCombo1() {
        return combo1;
    }

    public ComboBox<String> getCombo2() {
        return combo2;
    }

    public ComboBox<String> getCombo3() {
        return combo3;
    }

    public ComboBox<String> getCombo4() {
        return combo4;
    }
}
