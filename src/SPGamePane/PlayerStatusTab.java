package SPGamePane;

import GameConcepts.Game;
import GameConcepts.Player;
import GameConcepts.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class PlayerStatusTab extends Tab{

    private GridPane tabGrid = new GridPane();
    private Label armyValueL = new Label("Army Value: ");
    private Label armyValue = new Label();
    private Label unitsL = new Label("Units: ");
    private Label units = new Label();
    private Label turnPointsL = new Label("Turn Points: ");
    private Label turnPoints = new Label();

    private ObservableList<Unit> unitObservableList = FXCollections.observableArrayList();
    private ListView<Unit> unitListV = new ListView<Unit>(unitObservableList);
    private Game game;
    private Player player;
    private PlayerStatusPane observer;

    public PlayerStatusTab(Game game, final Player player, PlayerStatusPane observer){
        this.game = game;
        this.player = player;
        this.observer = observer;

        setText(player.getPlayerName());
        armyValue.setText(player.getArmyValue().toString() + "/" + game.getMaxArmyValue());
        units.setText(((Integer)player.getArmy().size()).toString());
        turnPoints.setText(player.getTurnPoints().toString() + "/" + game.getMaxTurnPoints());

        tabGrid.add(armyValueL, 0,0);
        tabGrid.add(armyValue, 1,0);
        tabGrid.add(unitsL, 0,1);
        tabGrid.add(units, 1,1);
        tabGrid.add(unitListV, 0,2,2,1);
        tabGrid.add(turnPointsL,0,3);
        tabGrid.add(turnPoints,1,3);

        for(Unit u: player.getArmy()){
            unitObservableList.add(u);
        }

        unitListV.setCellFactory(new Callback<ListView<Unit>, ListCell<Unit>>() {
            @Override
            public ListCell<Unit> call(ListView<Unit> param) {
                ListCell<Unit> unitListCell = new ListCell<Unit>(){
                    @Override
                    protected void updateItem(Unit item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getTypeName() + " --- " + item.getCharName());
                        }
                    }
                };
                return unitListCell;
            }
        });

        unitListV.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               notifyObs();
            }
        });

        this.setContent(tabGrid);
    }

    public void notifyObs(){
        observer.update();
    }

    public void update(){
        armyValue.setText(player.getArmyValue().toString() + "/" + game.getMaxArmyValue());
        units.setText(((Integer)player.getArmy().size()).toString());
        turnPoints.setText(player.getTurnPoints().toString() + "/" + game.getMaxTurnPoints());

        unitObservableList.removeAll();
        for(Unit u: player.getArmy()){
            unitObservableList.add(u);
        }
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerStatusPane getObserver() {
        return observer;
    }

    public ListView<Unit> getUnitListV() {
        return unitListV;
    }
}
