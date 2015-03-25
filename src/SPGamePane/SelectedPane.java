package SPGamePane;

import GameBoard.Tile;
import GameConcepts.Player;
import GameConcepts.Unit;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;

public class SelectedPane extends GridPane {
    //Tile Vars
    private Tile selectedTile;
    private ImageView tileImage = new ImageView();
    private Label tileL = new Label("Tile");
    private Label tileLocationL = new Label("Location: ");
    private Label tileLocation = new Label("");
    private Label tileDefModL = new Label("Defense: ");
    private Label tileDefMod = new Label("");
    private Label tileEvaModL = new Label("Evasion: ");
    private Label tileEvaMod = new Label("");
    private Label tileMovModL = new Label("Movement: ");
    private Label tileMovMod = new Label("");
    private Label tileRetModL = new Label("Retaliations: ");
    private Label tileRetMod = new Label("");

    //Unit vars
    private Unit selectedUnit;
    private ImageView unitImage = new ImageView();
    private Label unitL = new Label("Unit");
    private Label charName = new Label("Name: ");
    private Label charNameV = new Label("");
    private Label typeName = new Label("Type: ");
    private Label typeNameV = new Label("");
    private Label ACC  = new Label("ACC: ");
    private Label ACCV = new Label("");
    private Label APT = new Label("ACT: ");
    private Label APTV = new Label("");
    private Label CPA = new Label("CPA: ");
    private Label CPAV = new Label("");
    private Label DEF = new Label("DEF: ");
    private Label DEFV = new Label("");
    private Label EVA = new Label("EVA: ");
    private Label EVAV = new Label("");
    private Label HP = new Label("HP: ");
    private Label HPV = new Label("");
    private Label MOVE = new Label("MOV: ");
    private Label MOVEV = new Label("");
    private Label MP = new Label("MP: ");
    private Label MPV = new Label("");
    private Label RANGE = new Label("RNG: ");
    private Label RANGEV = new Label("");
    private Label RET = new Label("RET: ");
    private Label RETV = new Label("");
    private Label STR = new Label("STR: ");
    private Label STRV = new Label("");

    //Player Vars
    private Player owningPlayer;
    private Label ownerL = new Label("Army: ");
    private Label owner = new Label("");

    public SelectedPane(){
        //Tile Info
        this.add(tileL,0,0,4,1);
        this.add(tileImage,0,1,4,1);
        this.add(tileLocationL,0,2,2,1);
        this.add(tileLocation,2,2,2,1);
        this.add(tileDefModL,0,3,2,1);
        this.add(tileDefMod,2,3,2,1);
        this.add(tileEvaModL,0,4,2,1);
        this.add(tileEvaMod,2,4,2,1);
        this.add(tileMovModL,0,5,2,1);
        this.add(tileMovMod,2,5,2,1);
        this.add(tileRetModL,0,6,2,1);
        this.add(tileRetMod,2,6,2,1);

        //Unit Info
        this.add(unitL,0,7,2,1);
        this.add(charName,2,7);
        this.add(charNameV,3,7);
        this.add(unitImage,0,8,4,2);
        this.add(typeName,2,10);
        this.add(typeNameV,3,10);
        this.add(HP,0,11);
        this.add(HPV,1,11);
        this.add(MP,0,12);
        this.add(MPV,1,12);
        this.add(APT,0,13);
        this.add(APTV,1,13);
        this.add(MOVE,0,14);
        this.add(MOVEV,1,14);
        this.add(RANGE,0,15);
        this.add(RANGEV,1,15);
        this.add(ACC,2,11);
        this.add(ACCV,3,11);
        this.add(EVA,2,12);
        this.add(EVAV,3,12);
        this.add(STR,2,13);
        this.add(STRV,3,13);
        this.add(DEF,2,14);
        this.add(DEFV,3,14);
        this.add(RET,2,15);
        this.add(RETV,3,15);

        this.add(CPA,0,16);
        this.add(CPAV,1,16);
        this.add(ownerL,2,16);
        this.add(owner,3,16);
    }

    public void updateSelected(Tile tile, Unit unit, Player player){
        selectedTile = tile;
        selectedUnit = unit;
        owningPlayer = player;

        if((selectedTile != null) && (selectedUnit != null) && (owningPlayer != null)){//a tile was selected with a unit on it
            try {
                tileImage.setImage(new Image(new File(selectedTile.getImageName()).toURI().toString()));
            } catch (Exception e){
                System.err.println(e.toString());
            }
            tileLocation.setText(selectedTile.getX() + ", " + selectedTile.getY());
            tileDefMod.setText(selectedTile.getTerrain().getDefMod().toString());
            tileEvaMod.setText(selectedTile.getTerrain().getEvaMod().toString());
            tileMovMod.setText(selectedTile.getTerrain().getMovMod().toString());
            tileRetMod.setText(selectedTile.getTerrain().getRetMod().toString());

            try {
                unitImage.setImage(new Image(new File(selectedUnit.getImageName()).toURI().toString()));
            } catch (Exception e){
                System.err.println(e.toString());
            }
            charNameV.setText(selectedUnit.getCharName());
            typeNameV.setText(selectedUnit.getTypeName());
            ACCV.setText(selectedUnit.getACC().toString());
            APTV.setText(selectedUnit.getCurACT() + "/" + selectedUnit.getAPT());
            CPAV.setText(selectedUnit.getCPA().toString());
            DEFV.setText(selectedUnit.getDEF() + " (" + selectedUnit.getDEF() + selectedTile.getTerrain().getDefMod() + ")");
            EVAV.setText(selectedUnit.getEVA() + " (" + selectedUnit.getEVA() + selectedTile.getTerrain().getEvaMod() + ")");
            HPV.setText(selectedUnit.getCurHP() + "/" + selectedUnit.getHP());
            MOVEV.setText(selectedUnit.getCurMOV() + "/" + selectedUnit.getMOVE());
            MPV.setText(selectedUnit.getCurMP() + "/" + selectedUnit.getMP());
            RANGEV.setText(selectedUnit.getRANGE().toString());
            RETV.setText(selectedUnit.getCurRET() + "/" + selectedUnit.getRET() + " (" + selectedUnit.getRET() + selectedTile.getTerrain().getRetMod() + ")");
            STRV.setText(selectedUnit.getSTR().toString());

            owner.setText(owningPlayer.getPlayerName());
        }
        else if ((selectedTile != null) && (selectedUnit == null)){
            try {
                tileImage.setImage(new Image(new File(selectedTile.getImageName()).toURI().toString()));
            } catch (Exception e){
                System.err.println(e.toString());
            }
            tileLocation.setText(selectedTile.getX() + ", " + selectedTile.getY());
            tileDefMod.setText(selectedTile.getTerrain().getDefMod().toString());
            tileEvaMod.setText(selectedTile.getTerrain().getEvaMod().toString());
            tileMovMod.setText(selectedTile.getTerrain().getMovMod().toString());
            tileRetMod.setText(selectedTile.getTerrain().getRetMod().toString());

            try {
                unitImage.setImage(null);
            } catch (Exception e){
                System.err.println(e.toString());
            }
            charNameV.setText("");
            typeNameV.setText("");
            ACCV.setText("");
            APTV.setText("");
            CPAV.setText("");
            DEFV.setText("");
            EVAV.setText("");
            HPV.setText("");
            MOVEV.setText("");
            MPV.setText("");
            RANGEV.setText("");
            RETV.setText("");
            STRV.setText("");

            owner.setText("");
        }
        else if((selectedTile == null) && (selectedUnit != null) && (owningPlayer != null)){//a tile was selected with a unit on it
            try {
                tileImage.setImage(null);
            } catch (Exception e){
                System.err.println(e.toString());
            }
            tileLocation.setText("Waiting to be placed");
            tileDefMod.setText("");
            tileEvaMod.setText("");
            tileMovMod.setText("");
            tileRetMod.setText("");

            try {
                unitImage.setImage(new Image(new File(selectedUnit.getImageName()).toURI().toString()));
            } catch (Exception e){
                System.err.println(e.toString());
            }
            charNameV.setText(selectedUnit.getCharName());
            typeNameV.setText(selectedUnit.getTypeName());
            ACCV.setText(selectedUnit.getACC().toString());
            APTV.setText(selectedUnit.getCurACT() + "/" + selectedUnit.getAPT());
            CPAV.setText(selectedUnit.getCPA().toString());
            DEFV.setText(selectedUnit.getDEF() + " (" + selectedUnit.getDEF() + selectedTile.getTerrain().getDefMod() + ")");
            EVAV.setText(selectedUnit.getEVA() + " (" + selectedUnit.getEVA() + selectedTile.getTerrain().getEvaMod() + ")");
            HPV.setText(selectedUnit.getCurHP() + "/" + selectedUnit.getHP());
            MOVEV.setText(selectedUnit.getCurMOV() + "/" + selectedUnit.getMOVE());
            MPV.setText(selectedUnit.getCurMP() + "/" + selectedUnit.getMP());
            RANGEV.setText(selectedUnit.getRANGE().toString());
            RETV.setText(selectedUnit.getCurRET() + "/" + selectedUnit.getRET() + " (" + selectedUnit.getRET() + selectedTile.getTerrain().getRetMod() + ")");
            STRV.setText(selectedUnit.getSTR().toString());

            owner.setText(owningPlayer.getPlayerName());
        }
    }
}
