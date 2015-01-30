package StagingPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

public class OptionsPane extends GridPane {

    private final static int MINPLAYERS = 2;
    private final static int MAXPLAYERS = 4;
    private final static int DEFPLAYERS = 2;
    private final static int MINAPOINTS = 20;
    private final static int MAXAPOINTS = 100;
    private final static int DEFAPOINTS = 30;
    private final static int MINTPOINTS = 10;
    private final static int MAXTPOINTS = 50;
    private final static int DEFTPOINTS = 20;
    private final static int MINTLIMIT= 20;
    private final static int MAXTLIMIT = 200;
    private final static int DEFTLIMIT = 50;

    private final static int SLIDEMINTICK = 1;
    private final static int SLIDEMAJTICK = 5;


    private StagingController observer;
    private Label players;
    private Slider playersSlide;
    private Label pSlideVal;
    private Label armyPoints;
    private Slider armyPointsSlide;
    private Label apSlideVal;
    private Label turnPoints;
    private Slider turnPointsSlide;
    private Label tpSlideVal;
    private Label turnLimit;
    private Slider turnLimitSlide;
    private Label tlSlideVal;

    public OptionsPane(){
        super();
        initOptionNodes();
        addOptionNodes();
    }

    public void addOptionNodes(){
        add(players, 0, 0);
        add(playersSlide, 1, 0);
        add(pSlideVal, 2, 0);
        add(armyPoints, 0, 1);
        add(armyPointsSlide, 1, 1);
        add(apSlideVal, 2, 1);
        add(turnPoints, 0, 2);
        add(turnPointsSlide, 1, 2);
        add(tpSlideVal, 2, 2);
        add(turnLimit, 0, 3);
        add(turnLimitSlide, 1, 3);
        add(tlSlideVal, 2, 3);
    }

    public void initOptionNodes(){
        players = new Label("Players");
        armyPoints = new Label("Army Points");
        turnPoints = new Label("Turn Points");
        turnLimit = new Label("Turn Limit");

        playersSlide = new Slider(MINPLAYERS, MAXPLAYERS, DEFPLAYERS);
        playersSlide.setMajorTickUnit(SLIDEMINTICK);
        playersSlide.setMinorTickCount(SLIDEMINTICK);
        playersSlide.setSnapToTicks(true);
        playersSlide.setMinHeight(Slider.USE_PREF_SIZE);
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
        turnLimitSlide = new Slider(MINTLIMIT, MAXTLIMIT, DEFTLIMIT);
        turnLimitSlide.setMajorTickUnit(SLIDEMAJTICK);
        turnLimitSlide.setMinorTickCount(SLIDEMINTICK);
        turnLimitSlide.setSnapToTicks(true);
        turnLimitSlide.setMinHeight(Slider.USE_PREF_SIZE);

        pSlideVal = new Label(((Integer)((Double) playersSlide.getValue()).intValue()).toString());
        apSlideVal = new Label(((Integer)((Double) armyPointsSlide.getValue()).intValue()).toString());
        tpSlideVal = new Label(((Integer)((Double) turnPointsSlide.getValue()).intValue()).toString());
        tlSlideVal = new Label(((Integer)((Double) turnLimitSlide.getValue()).intValue()).toString());

        playersSlide.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                pSlideVal.textProperty().setValue(
                        String.valueOf((int) playersSlide.getValue()));
            }
        });

        armyPointsSlide.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                apSlideVal.textProperty().setValue(
                        String.valueOf((int) armyPointsSlide.getValue()));
            }
        });

        turnPointsSlide.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                tpSlideVal.textProperty().setValue(
                        String.valueOf((int) turnPointsSlide.getValue()));
            }
        });

        turnLimitSlide.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                tlSlideVal.textProperty().setValue(
                        String.valueOf((int) turnLimitSlide.getValue()));
            }
        });
    }

    public void register(StagingController stagingController){
        this.observer = stagingController;
    }
}
