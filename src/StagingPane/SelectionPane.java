package StagingPane;

import GameConcepts.Unit;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SelectionPane extends GridPane {

    private StagingController observer;
    private ScrollPane selectionWindow = new ScrollPane();

    private final String unitsDirName = "Resources/UnitImages/";
    private final String unitsDatDirName = "Resources/UnitData/";

    public ArrayList<String> unitList = new ArrayList<String>();
    public ArrayList<SelectionPaneUnitButton> units = new ArrayList<SelectionPaneUnitButton>();
    private final int MAXWIDTH = 1;
    private static int columnIndex = 0;
    private static int rowIndex = 0;

    //DataWindow vars
    private Unit selectedUnit = new Unit();
    private ImageView unitImage = new ImageView();
    private Label charName = new Label("Name: ");
    private Label charNameV = new Label("");
    private Label typeName = new Label("Type: ");
    private Label typeNameV = new Label("");
    private Label typeVal  = new Label("Cost: ");
    private Label typeValV = new Label("");
    private Label ACC  = new Label("Accuracy: ");
    private Label ACCV = new Label("");
    private Label APT = new Label("Actions: ");
    private Label APTV = new Label("");
    private Label DEF = new Label("Defense: ");
    private Label DEFV = new Label("");
    private Label EVA = new Label("Evasion: ");
    private Label EVAV = new Label("");
    private Label HP = new Label("Hit Points: ");
    private Label HPV = new Label("");
    private Label MOVE = new Label("Movement: ");
    private Label MOVEV = new Label("");
    private Label MP = new Label("Magic Points: ");
    private Label MPV = new Label("");
    private Label RANGE = new Label("Range: ");
    private Label RANGEV = new Label("");
    private Label RET = new Label("Retaliations: ");
    private Label RETV = new Label("");
    private Label STR = new Label("Strength: ");
    private Label STRV = new Label("");

    private Button changeName = new Button("Change Name");
    private Button addUnit = new Button("Add Unit");
    private Button removeUnit = new Button("Remove Unit");

    public SelectionPane(){
        super();
        addSelectNodes();
        loadUnits();
        addUnits();
    }

    public void addSelectNodes(){
        add(selectionWindow, 0, 0, 1, 9);

        add(typeVal, 1, 0, 2, 1);
        add(typeValV, 3, 0, 2, 1);
        add(unitImage, 1, 1, 2, 2);
        add(charName, 3, 1);
        add(charNameV, 4, 1);
        add(typeName, 3, 2);
        add(typeNameV, 4, 2);

        add(HP, 1, 3);
        add(HPV, 2, 3);
        add(MP, 1, 4);
        add(MPV, 2, 4);
        add(APT, 1, 5);
        add(APTV, 2, 5);
        add(MOVE, 1, 6);
        add(MOVEV, 2, 6);
        add(RANGE, 1, 7);
        add(RANGEV, 2, 7);

        add(ACC, 3, 3);
        add(ACCV, 4, 3);
        add(EVA, 3, 4);
        add(EVAV, 4, 4);
        add(STR, 3, 5);
        add(STRV, 4, 5);
        add(DEF, 3, 6);
        add(DEFV, 4, 6);
        add(RET, 3, 7);
        add(RETV, 4, 7);

        add(changeName, 1, 8);
        add(addUnit, 2, 8);
        add(removeUnit, 3, 8);


        changeName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                selectedUnit.setCharName("John");
                charNameV.setText(selectedUnit.getCharName());
            }
        });
    }

    /**
     * Add the found units into the Pane.
     */
    public void addUnits(){
        for(SelectionPaneUnitButton button: units){
            if(columnIndex > MAXWIDTH){
                columnIndex = 0;
                rowIndex++;
            }
            add(button , columnIndex++, rowIndex);
        }
    }

    /**
     * Search through the resource directories to load the available (having a png
     * and dat file) units into the selectionPane. Also load the associated data
     * from the dat file about the unit stats.
     */
    public void loadUnits(){
        File dir = new File(unitsDirName);
        if(dir.isDirectory()){
            File[] directoryListing = dir.listFiles();
            for (File child : directoryListing) {
                if(child.getName().endsWith(".png") && (child.getName().contains("Front"))){
                    String unitData = (unitsDirName + child.getName()).replace(unitsDirName, unitsDatDirName)
                            .replace("Front", "").replace(".png", ".dat");
                    File datFile = new File(unitData);
                    if(datFile.isFile()){
                        if(loadData(unitData)){
                            unitList.add(unitsDirName + child.getName());
                        }
                    }
                }
            }
        }
    }

    /**
     * Loads the data from a file about a unit's statistics into a unit variable.
     * @param fileName The name of the file that contains a units info.
     * @return Return true if the load into a unit variable was successful.
     *         Returns false otherwise.
     */
    public boolean loadData(String fileName){
        try{
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            String fileData = new String(encoded, Charset.defaultCharset());
            String peeledData = new String(fileData);
            {
                String imageName = fileName.replace(".dat", "Front.png").replace("src/", "").replace("Data", "Images");
                String charName = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String typeName = peeledData.substring(0,peeledData.indexOf(';'));

                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String typeCostS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String ACCS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String APTS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String DEFS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String EVAS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String EXPS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String HPS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String LEVELS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String MOVES = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String MPS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String RANGES = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String RETS = peeledData.substring(0,peeledData.indexOf(';'));
                peeledData = peeledData.substring(peeledData.indexOf(';')+1);
                String STRS = peeledData.substring(0,peeledData.indexOf(';'));
                try {
                    Integer typeCost = Integer.parseInt(typeCostS);
                    Integer acc = Integer.parseInt(ACCS);
                    Integer apt = Integer.parseInt(APTS);
                    Integer def = Integer.parseInt(DEFS);
                    Integer eva = Integer.parseInt(EVAS);
                    Integer exp = Integer.parseInt(EXPS);
                    Integer hp = Integer.parseInt(HPS);
                    Integer level = Integer.parseInt(LEVELS);
                    Integer move = Integer.parseInt(MOVES);
                    Integer mp = Integer.parseInt(MPS);
                    Integer range = Integer.parseInt(RANGES);
                    Integer ret = Integer.parseInt(RETS);
                    Integer str = Integer.parseInt(STRS);

                    Unit u = new Unit(imageName, charName, typeName, typeCost, acc, apt, def, eva, exp, hp, level, move, mp, range, ret, str);
                    SelectionPaneUnitButton selectionPaneUnitButton = new SelectionPaneUnitButton(u, this);
                    units.add(selectionPaneUnitButton);
                } catch (NumberFormatException nfe){
                    System.err.println("There was an error with the numerical data in the file " + fileName);
                    return false;
                }
            }
            return true;
        } catch (IOException ioe){
            System.err.println("There was an error reading the data file " + fileName);
            return false;
        }
    }

    public void register(StagingController stagingController){
        this.observer = stagingController;
    }

    public void updateSelectedUnit(Unit u){
        selectedUnit = new Unit(u);
        unitImage.setImage(new Image(new File(selectedUnit.getImageName()).toURI().toString()));
        typeNameV.setText(u.getTypeName());
        typeValV.setText(u.getTypeVal().toString());
        charNameV.setText(u.getCharName());
        HPV.setText(u.getHP().toString());
        MPV.setText(u.getMP().toString());
        APTV.setText(u.getAPT().toString());
        MOVEV.setText(u.getMOVE().toString());
        RANGEV.setText(u.getRANGE().toString());
        ACCV.setText(u.getACC().toString());
        EVAV.setText(u.getEVA().toString());
        STRV.setText(u.getSTR().toString());
        DEFV.setText(u.getDEF().toString());
        RETV.setText(u.getRET().toString());
    }
}
