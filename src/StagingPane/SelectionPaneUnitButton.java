package StagingPane;

import GameConcepts.Unit;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class SelectionPaneUnitButton extends ImageView {

    private SelectionPane observer;
    private Unit unit;

    protected final String STYLE_NORMAL = "-fx-effect: null";
    protected final String STYLE_HOVER = "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.2), 60, 0, 0, 0)";

    public SelectionPaneUnitButton(Unit u, SelectionPane selectionPane){
        super();
        this.unit = u;

        try {
            this.setImage(new Image(new File(u.getImageName()).toURI().toString()));
        } catch (Exception e){
            System.err.println("Could not find the unit image file. ");
        }

        observer = selectionPane;

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                setStyle(STYLE_HOVER);
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                setStyle(STYLE_NORMAL);
            }
        });
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                observer.updateSelectedUnit(unit);
            }
        });
    }
}
