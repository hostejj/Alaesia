package SPGamePane;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ButtonBox extends HBox {
    private Button pass;

    private SPGamePaneController observer;

    public ButtonBox(){
        pass = new Button("Pass");

        this.getChildren().add(pass);

        pass.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                observer.update(observer.PASS);
            }
        });
        pass.setPrefWidth(Integer.MAX_VALUE);
    }

    public void register(SPGamePaneController observer){
        this.observer = observer;
    }
}
