package GUIs;

import GameBoard.MapCell;
import GameBoard.Tile;
import GameConcepts.Unit;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ArrayList;

public class BoardPaneButton extends StackPane {
    public final Integer LEFTCLICK = 0;
    public final Integer RIGHTCLICK = 1;

    private BoardPane observer;
    private MapCell mapCell;
    private ImageView tileImage;
    private ArrayList<ImageView> shades = new ArrayList<ImageView>();
    private ImageView unitImage;
    private BoardPaneButton self;

    public BoardPaneButton(final MapCell mapCell, BoardPane boardPane) {
        super();
        this.mapCell = mapCell;
        this.self = this;
        observer = boardPane;

        try {
            Image timage = new Image(new File(mapCell.getTile().getImageName()).toURI().toString());
            tileImage = new ImageView(timage);
            this.getChildren().add(tileImage);

            //initialze starting locations list to nulls for comparison
            for(int i = 0; i < observer.getGameMap().getMaxPlayers(); i++){
                shades.add(new ImageView());
            }

            if(mapCell.getUnit() != null){
                Image uimage = new Image(new File(mapCell.getUnit().getImageName()).toURI().toString());
                unitImage = new ImageView(uimage);
                this.getChildren().add(unitImage);
            } else {
                unitImage = new ImageView();
                this.getChildren().add(unitImage);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if(me.getButton().equals(MouseButton.PRIMARY)){
                    observer.update(self, LEFTCLICK);
                } else if (me.getButton().equals(MouseButton.SECONDARY)){
                    observer.update(self, RIGHTCLICK);
                }
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
    public void addShade(Integer shadeIndex, Image image){
        this.shades.get(shadeIndex).setImage(image);
        resetButtonView();
    }

    //Removes a shade on an already existing tile.
    public void removeShade(Integer shadeIndex){
        this.shades.get(shadeIndex).setImage(null);
        resetButtonView();
    }

    //Removes all shades on an already existing tile
    public void removeAllShades(){
        for(int i = 0; i < shades.size() ; i++){
            removeShade(i);
        }
    }

    public MapCell getMapCell() { return mapCell; }

    public Tile getTile() {
        return mapCell.getTile();
    }

    public Unit getUnit() { return mapCell.getUnit(); }

    public ImageView getTileImage() { return tileImage; }

    public ImageView getUnitImage() {
        return unitImage;
    }

}
