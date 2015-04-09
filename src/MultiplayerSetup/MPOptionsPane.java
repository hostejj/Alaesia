package MultiplayerSetup;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MPOptionsPane extends GridPane {

    private final static int MINAPOINTS = 20;
    private final static int MAXAPOINTS = 100;
    private final static int DEFAPOINTS = 30;
    private final static int MINTPOINTS = 5;
    private final static int MAXTPOINTS = 30;
    private final static int DEFTPOINTS = 10;

    private final static int SLIDEMINTICK = 1;
    private final static int SLIDEMAJTICK = 5;

    private MPStagingController observer;
    private Label armyPoints;
    private Slider armyPointsSlide;
    private Label apSlideVal;
    private Label turnPoints;
    private Slider turnPointsSlide;
    private Label tpSlideVal;

    private HBox buttons;
    private Button start;
    private Button sel;
    private Button back;

    public MPOptionsPane() {
        super();
        initOptionNodes();
        addOptionNodes();
    }

    public void addOptionNodes() {
        add(armyPoints, 0, 1);
        add(armyPointsSlide, 1, 1);
        add(apSlideVal, 2, 1);
        add(turnPoints, 0, 2);
        add(turnPointsSlide, 1, 2);
        add(tpSlideVal, 2, 2);

        buttons.getChildren().add(back);
        buttons.getChildren().add(sel);
        buttons.getChildren().add(start);
        add(buttons, 0, 4, 3, 1);
    }

    public void initOptionNodes() {
        armyPoints = new Label("Army Points");
        turnPoints = new Label("Turn Points");
        buttons = new HBox();
        start = new Button("Start Game");
        sel = new Button("Select Map");
        back = new Button("Back");

        armyPointsSlide = new Slider(MINAPOINTS, MAXAPOINTS, DEFAPOINTS);
        armyPointsSlide.setMajorTickUnit(SLIDEMAJTICK);
        armyPointsSlide.setMinorTickCount(SLIDEMINTICK);
        armyPointsSlide.setSnapToTicks(true);
        armyPointsSlide.setMinHeight(Slider.USE_PREF_SIZE);
        turnPointsSlide = new Slider(MINTPOINTS, MAXTPOINTS, DEFTPOINTS);
        turnPointsSlide.setMajorTickUnit(SLIDEMAJTICK);
        turnPointsSlide.setMinorTickCount(SLIDEMINTICK);
        turnPointsSlide.setSnapToTicks(true);
        turnPointsSlide.setMinHeight(Slider.USE_PREF_SIZE);

        apSlideVal = new Label(((Integer) ((Double) armyPointsSlide.getValue()).intValue()).toString());
        tpSlideVal = new Label(((Integer) ((Double) turnPointsSlide.getValue()).intValue()).toString());

        armyPointsSlide.setDisable(true);
        armyPointsSlide.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                apSlideVal.textProperty().setValue(
                        String.valueOf((int) armyPointsSlide.getValue()));
            }
        });

        turnPointsSlide.setDisable(true);
        turnPointsSlide.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                tpSlideVal.textProperty().setValue(
                        String.valueOf((int) turnPointsSlide.getValue()));
            }
        });

        start.setDisable(true);
        start.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            public void handle(javafx.scene.input.MouseEvent me) {
                update(observer.START);
            }
        });
        sel.setDisable(true);
        sel.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            public void handle(javafx.scene.input.MouseEvent me) {
                update(observer.SEL);
            }
        });
        back.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            public void handle(javafx.scene.input.MouseEvent me) {
                update(observer.BACK);
            }
        });
    }

    public void register(MPStagingController mpStagingController) {
        this.observer = mpStagingController;
    }

    public void hostPrivileges() {
        armyPointsSlide.setDisable(false);
        turnPointsSlide.setDisable(false);
        start.setDisable(false);
        sel.setDisable(false);
    }

    public void update(Integer button) {
        observer.update(button);
    }

    public Slider getArmyPointsSlide() {
        return armyPointsSlide;
    }

    public Slider getTurnPointsSlide() {
        return turnPointsSlide;
    }

}