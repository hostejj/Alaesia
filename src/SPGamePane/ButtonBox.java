package SPGamePane;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ButtonBox extends HBox {
    private Button pass;
    private Button end;

    private SPGamePaneController observer;

    public ButtonBox(){
        pass = new Button("Pass");
        end = new Button("End Turn");

        this.getChildren().add(pass);
        this.getChildren().add(end);

        pass.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                observer.update(observer.PASS);
            }
        });

        end.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                observer.update(observer.END);
            }
        });
    }

    public void register(SPGamePaneController observer){
        this.observer = observer;
    }
}
