package GUIs;

import GameConcepts.Game;
import SPGamePane.SPGamePaneController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class BoardContextMenu extends ContextMenu {
    private MenuItem use = new MenuItem("Use");
    private MenuItem move = new MenuItem("Move");
    private MenuItem attack = new MenuItem("Attack");
    private MenuItem end = new MenuItem("End Action");
    private MenuItem place = new MenuItem("Place");

    private SPGamePaneController observer;

    public BoardContextMenu(final SPGamePaneController observer) {
        super();
        this.observer = observer;

        use.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //select the unit for use
                //deduct points equal to the cost per action from players turn total
                observer.update(observer.USE);
            }
        });

        move.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                observer.update(observer.MOVE);
            }
        });

        attack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                observer.update(observer.ATTACK);
            }
        });

        end.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                observer.update(observer.END);
            }
        });

        place.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                observer.update(observer.PLACE);
            }
        });

        if(observer.getGame().getGameState() == Game.GameState.PLACEMENTPHASE){
            placeDis();
        } else if(observer.getGame().getGameState() == Game.GameState.BATTLE) {
            battleDis();
        } else if(observer.getGame().getGameState() == Game.GameState.UNITMOVE){
            unitDis();
        } else if(observer.getGame().getGameState() == Game.GameState.GAMEOVER){
            goDis();
        }

        this.getItems().addAll(use, move, attack, end, place);
    }

    public void placeDis(){
        use.setDisable(true);
        move.setDisable(true);
        attack.setDisable(true);
        end.setDisable(true);
        place.setDisable(false);
    }

    public void battleDis(){
        use.setDisable(false);
        move.setDisable(true);
        attack.setDisable(true);
        end.setDisable(true);
        place.setDisable(true);
    }

    public void unitDis(){
        use.setDisable(true);
        move.setDisable(false);
        attack.setDisable(false);
        end.setDisable(false);
        place.setDisable(true);
    }

    public void goDis(){
        use.setDisable(true);
        move.setDisable(true);
        attack.setDisable(true);
        end.setDisable(true);
        place.setDisable(true);
    }
}
