package StagingPane;

import GameConcepts.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import java.io.File;

public class PlayerTab extends Tab {
    private PlayerPane observer;
    private Integer maxUnits;
    private GridPane gridPane = new GridPane();
    private ObservableList<Unit> unitObserv = FXCollections.observableArrayList();
    private ListView<Unit> unitListV = new ListView<Unit>(unitObserv);
    private Label armyValueL = new Label("Army Value: ");
    private Label armyValue = new Label();
    private Label unitsTotalL = new Label("Units: ");
    private Label unitsTotal = new Label();

    //DataWindow vars
    private Unit selectedUnit;
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
    private Label CPA = new Label("Cost per Action:");
    private Label CPAV = new Label("");
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
    private Label DESCV = new Label("");

    private Button removeUnit = new Button("Remove Unit");
    private Button generateRND = new Button("Generate Army");

    public PlayerTab(String playerName, PlayerPane playerPane, Integer maxUnits){
        register(playerPane);
        this.setText(playerName);
        this.setClosable(false);
        this.maxUnits = maxUnits;

        unitListV.setPrefHeight(Integer.MAX_VALUE);
        gridPane.setPadding(new Insets(10, 0, 10, 0));
        DESCV.setWrapText(true);
        armyValue.setText("0/" + observer.getObserver().getGame().getMaxArmyValue().toString());
        unitsTotal.setText("0/" + maxUnits);
        gridPane.add(armyValueL,0,0);
        gridPane.add(armyValue,1,0);
        gridPane.add(unitsTotalL,0,1);
        gridPane.add(unitsTotal,1,1);
        gridPane.add(unitListV,0,2,2,18);

        gridPane.add(unitImage, 2, 0, 2, 2);
        gridPane.add(typeVal, 2, 2);
        gridPane.add(typeValV, 3, 2);
        gridPane.add(charName, 2, 3);
        gridPane.add(charNameV, 3, 3);
        gridPane.add(typeName, 2, 4);
        gridPane.add(typeNameV, 3, 4);
        gridPane.add(CPA,2,5);
        gridPane.add(CPAV,3,5);

        gridPane.add(HP, 2, 6);
        gridPane.add(HPV, 3, 6);
        gridPane.add(MP, 2, 7);
        gridPane.add(MPV, 3, 7);
        gridPane.add(APT, 2, 8);
        gridPane.add(APTV, 3, 8);
        gridPane.add(MOVE, 2, 9);
        gridPane.add(MOVEV, 3, 9);
        gridPane.add(RANGE, 2, 10);
        gridPane.add(RANGEV, 3, 10);

        gridPane.add(ACC, 2, 11);
        gridPane.add(ACCV, 3, 11);
        gridPane.add(EVA, 2, 12);
        gridPane.add(EVAV, 3, 12);
        gridPane.add(STR, 2, 13);
        gridPane.add(STRV, 3, 13);
        gridPane.add(DEF, 2, 14);
        gridPane.add(DEFV, 3, 14);
        gridPane.add(RET, 2, 15);
        gridPane.add(RETV, 3, 15);
        gridPane.add(DESCV, 2, 16, 2, 1);

        gridPane.add(removeUnit, 2,17,2,1);
        gridPane.add(generateRND, 2,18,2,1);

        unitListV.setCellFactory(new Callback<ListView<Unit>, ListCell<Unit>>() {
            @Override
            public ListCell<Unit> call(ListView<Unit> param) {
                ListCell<Unit> unitListCell = new ListCell<Unit>(){
                    @Override
                    protected void updateItem(Unit item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getTypeName() + " --- " + item.getCharName());
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
                return unitListCell;
            }
        });

        unitListV.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedUnit = unitListV.getSelectionModel().getSelectedItem();
                if(selectedUnit != null) {
                    setUnitData();
                }
            }
        });

        removeUnit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeSelectedUnit();
            }
        });

        generateRND.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                observer.getObserver().update(observer.getObserver().RND);
            }
        });
        this.setContent(gridPane);
    }

    public void addUnit(Unit u){
        Unit unit = new Unit(u);
        unitObserv.add(unit);

        armyValue.setText(getArmyValue().toString() + "/" + observer.getObserver().getGame().getMaxArmyValue().toString() );
        unitsTotal.setText(unitObserv.size() + "/" + maxUnits);
        FXCollections.sort(unitObserv);
    }

    public void removeSelectedUnit(){
        if(selectedUnit == null){
            return;
        }

        unitObserv.remove(selectedUnit);
        armyValue.setText(getArmyValue().toString() + "/" + observer.getObserver().getGame().getMaxArmyValue().toString());
        unitsTotal.setText(unitObserv.size() + "/" + maxUnits);
        return;
    }

    public void removeAllUnits(){
        int length = unitObserv.size();
        for(int i = 0; i < length; i++ ){
            unitObserv.remove(0);
        }
        armyValue.setText(getArmyValue().toString() + "/" + observer.getObserver().getGame().getMaxArmyValue().toString());
        unitsTotal.setText(unitObserv.size() + "/" + maxUnits);
    }

    public void setUnitData(){
        unitImage.setImage(new Image(new File(selectedUnit.getImageName()).toURI().toString()));
        typeNameV.setText(selectedUnit.getTypeName());
        typeValV.setText(selectedUnit.getTypeVal().toString());
        charNameV.setText(selectedUnit.getCharName());
        CPA.setText(selectedUnit.getCPA().toString());
        HPV.setText(selectedUnit.getHP().toString());
        MPV.setText(selectedUnit.getMP().toString());
        APTV.setText(selectedUnit.getAPT().toString());
        MOVEV.setText(selectedUnit.getMOVE().toString());
        RANGEV.setText(selectedUnit.getMinRANGE() + "-" + selectedUnit.getMaxRANGE());
        ACCV.setText(selectedUnit.getACC().toString());
        EVAV.setText(selectedUnit.getEVA().toString());
        STRV.setText(selectedUnit.getSTR().toString());
        DEFV.setText(selectedUnit.getDEF().toString());
        RETV.setText(selectedUnit.getRET().toString());
        DESCV.setText(selectedUnit.getDESC());
    }

    public Integer getArmyValue(){
        Integer value = 0;
        for(Unit u: unitObserv){
            value += u.getTypeVal();
        }
        return value;
    }

    public ObservableList<Unit> getUnitsList() {
        return unitObserv;
    }

    public Integer getMaxUnits() {
        return maxUnits;
    }

    public void register(PlayerPane playerPane){
        this.observer = playerPane;
    }
}
