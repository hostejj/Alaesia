package GUIs;

import GUIs.TileButton;
import GameBoard.Tile;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Objects;

public class BoardPaneButton extends TileButton {

    private BoardPane observer;

    public BoardPaneButton(Tile t, BoardPane boardPane) {
        super();
        this.tile = new Tile(t);

        try {
            Image image = new Image(new File(t.getImageName()).toURI().toString());
            tileImage = new ImageView(image);
            this.getChildren().add(tileImage);
        } catch (Exception e){
            System.err.println(e.toString());
        }

        observer = boardPane;
        //initialze starting locations list to nulls for comparison
        for(int i = 0; i < observer.getGameMap().getMaxPlayers(); i++){
            shades.add(null);
        }

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                observer.update(tile);
            }
        });
    }

    //Resets the images currently on the pane and adds the correct views.
    public void resetButtonView(){
        this.getChildren().clear();
        if (tileImage != null){
            this.getChildren().add(tileImage);
        }
        for(ImageView imageView: this.shades){
            if(imageView != null){
                this.getChildren().add(imageView);
            }
        }
        if(unitImage != null){
            this.getChildren().add(unitImage);
        }
    }

    //Adds a new shade onto an already existing tile.
    public void addShade(Integer shadeIndex, ImageView imageView){
        this.shades.set(shadeIndex, imageView);
        resetButtonView();
    }

    //Removes a shade on an already existing tile.
    public void removeShade(Integer shadeIndex){
        shades.set(shadeIndex, null);
        resetButtonView();
    }
}
