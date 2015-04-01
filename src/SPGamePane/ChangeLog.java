package SPGamePane;

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class ChangeLog extends HBox{
    private TextArea log = new TextArea("Game Started: \n");

    public ChangeLog(){
        log.setPrefWidth(Integer.MAX_VALUE);
        this.getChildren().add(log);
    }

    public void setLogText(String message) {
        this.log.appendText(message);
    }
}