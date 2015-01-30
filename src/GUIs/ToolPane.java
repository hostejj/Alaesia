package GUIs;

import Editor.EditorController;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ToolPane extends GridPane {

    private EditorController observer;

    private final String TOOL_NORMAL = "-fx-effect: null; -fx-focus-color: transparent;";
    private final String TOOL_CLICKED = "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.4), 60, 0, 0, 0); -fx-focus-color: black;";
    private static final Integer PANEHGAP = 15;
    private static final Integer BMINH = 40;
    private static final Integer BMINW = 40;
    private static final Integer BMAXH = 40;
    private static final Integer BMAXW = 40;

    private static final Integer SELECTOR = 0;
    private static final Integer BRUSHWI = 1;
    private static final Integer BRUSHWII = 2;
    private static final Integer BRUSHWIII = 3;

    private Button selector = new Button();
    private Button brushwI = new Button();
    private Button brushwII = new Button();
    private Button brushwIII = new Button();

    public ToolPane(){
        super();

        setHgap(PANEHGAP);

        selector.setGraphic(new ImageView( new Image("Resources/InterfaceImages/cursor.png")));
        selector.setMaxSize(BMAXW, BMAXH);
        selector.setMinSize(BMINH, BMINW);
        selector.setStyle(TOOL_CLICKED);
        selector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                deselectButtons();
                selector.setStyle(TOOL_CLICKED);
                observer.updateTool(SELECTOR);
            }
        });

        brushwI.setGraphic(new ImageView( new Image("Resources/InterfaceImages/brushw1.png")));
        brushwI.setMaxSize(BMAXW, BMAXH);
        brushwI.setMinSize(BMINH, BMINW);
        brushwI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                deselectButtons();
                brushwI.setStyle(TOOL_CLICKED);
                observer.updateTool(BRUSHWI);
            }
        });

        brushwII.setGraphic(new ImageView( new Image("Resources/InterfaceImages/brushw2.png")));
        brushwII.setMaxSize(BMAXW, BMAXH);
        brushwII.setMinSize(BMINH, BMINW);
        brushwII.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                deselectButtons();
                brushwII.setStyle(TOOL_CLICKED);
                observer.updateTool(BRUSHWII);
            }
        });

        brushwIII.setGraphic(new ImageView( new Image("Resources/InterfaceImages/brushw3.png")));
        brushwIII.setMaxSize(BMAXW, BMAXH);
        brushwIII.setMinSize(BMINH, BMINW);
        brushwIII.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                deselectButtons();
                brushwIII.setStyle(TOOL_CLICKED);
                observer.updateTool(BRUSHWIII);
            }
        });

        add(selector, 0, 0);
        add(brushwI, 1, 0);
        add(brushwII, 2, 0);
        add(brushwIII, 3, 0);
    }

    public void deselectButtons(){
        selector.setStyle(TOOL_NORMAL);
        brushwI.setStyle(TOOL_NORMAL);
        brushwII.setStyle(TOOL_NORMAL);
        brushwIII.setStyle(TOOL_NORMAL);
    }

    public void register(EditorController editorController){
        this.observer = editorController;
    }
}