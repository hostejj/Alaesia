package StagingPane;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class SelectionPane extends GridPane {

    private StagingController observer;
    private ScrollPane selectionWindow = new ScrollPane();

    //DataWindow vars
    private ImageView unitImage = new ImageView();
    private Label charName = new Label("Name: ");
    private Label charNameV = new Label("");
    private Label typeName = new Label("Type: ");
    private Label typeNameV = new Label("");
    private Label typeVal  = new Label("Cost: ");
    private Label typeValV = new Label("");
    private Label ACC  = new Label("Accuracy: ");
    private Label ACCV = new Label("");
    private Label APT = new Label("Actions: ");
    private Label APTV = new Label("");
    private Label DEF = new Label("Defense: ");
    private Label DEFV = new Label("");
    private Label EVA = new Label("Evasion: ");
    private Label EVAV = new Label("");
    private Label HP = new Label("Hit Points: ");
    private Label HPV = new Label("");
    private Label MOVE = new Label("Movement: ");
    private Label MOVEV = new Label("");
    private Label MP = new Label("Magic Points: ");
    private Label MPV = new Label("");
    private Label RANGE = new Label("Range: ");
    private Label RANGEV = new Label("");
    private Label RET = new Label("Retaliations: ");
    private Label RETV = new Label("");
    private Label STR = new Label("Strength: ");
    private Label STRV = new Label("");

    private Button changeName = new Button("Change Name");
    private Button addUnit = new Button("Add Unit");
    private Button removeUnit = new Button("Remove Unit");

    public SelectionPane(){
        super();
        addSelectNodes();
    }

    public void addSelectNodes(){
        add(selectionWindow, 0, 0, 1, 9);

        add(typeVal, 1, 0, 2, 1);
        add(typeValV, 3, 0, 2, 1);
        add(unitImage, 1, 1, 2, 2);
        add(charName, 3, 1);
        add(charNameV, 4, 1);
        add(typeName, 3, 2);
        add(typeNameV, 4, 2);

        add(HP, 1, 3);
        add(HPV, 2, 3);
        add(MP, 1, 4);
        add(MPV, 2, 4);
        add(APT, 1, 5);
        add(APTV, 2, 5);
        add(MOVE, 1, 6);
        add(MOVEV, 2, 6);
        add(RANGE, 1, 7);
        add(RANGEV, 2, 7);

        add(ACC, 3, 3);
        add(ACCV, 4, 3);
        add(EVA, 3, 4);
        add(EVAV, 4, 4);
        add(STR, 3, 5);
        add(STRV, 4, 5);
        add(DEF, 3, 6);
        add(DEFV, 4, 6);
        add(RET, 3, 7);
        add(RETV, 4, 7);

        add(changeName, 1, 8);
        add(addUnit, 2, 8);
        add(removeUnit, 3, 8);
    }

    public void register(StagingController stagingController){
        this.observer = stagingController;
    }
}
