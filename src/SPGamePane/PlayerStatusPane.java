package SPGamePane;

import GameConcepts.Game;
import GameConcepts.Player;
import GameConcepts.Unit;
import javafx.scene.control.TabPane;

public class PlayerStatusPane extends TabPane {

    private SPGamePaneController observer;

    public PlayerStatusPane(Game game, SPGamePaneController observer){
        this.observer = observer;
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        for (Player player : game.getPlayers()){
            this.getTabs().add(new PlayerStatusTab(game, player, this));
        }
    }


    public void update(){
        observer.update(observer.SELECTED);
    }
}
