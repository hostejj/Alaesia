package MultiplayerSetup;

import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ChatPane extends GridPane {
    private TextArea chatBox = new TextArea("");
    private TextField chatEntry = new TextField("");
    private Button chatButton = new Button("Send");
    private MPStagingController observer;

    public ChatPane(){
        add(chatBox,0,0,2,1);
        add(chatEntry,0,1);
        add(chatButton,1,1);

        chatButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                observer.update(observer.CHAT);
                chatEntry.setText("");
            }
        });

        chatEntry.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    observer.update(observer.CHAT);
                    chatEntry.setText("");
                }
            }
        });
    }

    public void register(MPStagingController mpStagingController){
        this.observer = mpStagingController;
    }

    public TextField getChatEntry() {
        return chatEntry;
    }

    public TextArea getChatBox() {
        return chatBox;
    }
}
