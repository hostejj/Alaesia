package SPGamePane;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class HintBox extends VBox{
    private Label playerName;
    private Label hint;

    public HintBox(){
        playerName = new Label("Player");
        hint = new Label("Unit Placement: Click on a starting location tile to place the current unit.");

        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(playerName);
        this.getChildren().add(hint);
        hint.setWrapText(true);
    }

    public void setPlayerName(String playerName) {
        this.playerName.setText(playerName);
    }

    public void setHint(String hint) {
        this.hint.setText(hint);
    }
}
