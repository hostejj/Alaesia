package MultiplayerSetup;

import GUIs.ErrorPopop;
import GUIs.Navigator;

import GUIs.TransferModel;
import GameBoard.GameMap;
import GameConcepts.Game;
import GameConcepts.Unit;
import Networking.ClientConnection;
import Networking.ClientServer;
import StagingPane.PlayerTab;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class MPStagingController implements Initializable {

    @FXML private GridPane topGrid;
    @FXML private MPOptionsPane mpOptionsPane;
    @FXML private PlayerSelPane playerSelPane;
    @FXML private MPMapSelectionPane mpMapSelectionPane;
    @FXML private MPSelectionPane mpSelectionPane;
    @FXML private MPPlayerPane mpPlayerPane;
    @FXML private ChatPane chatPane;
    private TransferModel transferModel = new TransferModel();
    private Game game;
    private boolean firstPLYCHNG = false;
    public static Integer BACK = 0;
    public static Integer SEL = 1;
    public static Integer START = 2;
    public static Integer ADD = 3;
    public static Integer RND = 4;
    public static Integer CHAT = 5;
    public static Integer PLAYTYPE = 6;
    public static Integer REMOVEUNIT = 7;
    private HashMap<String, ClientServer> servPlayerMap = new HashMap<String, ClientServer>(); // for host only

    private boolean isHost;
    private String activeName = "";

    @Override
    public void initialize(URL location, ResourceBundle resources){
        transferModel.getTransferModel().setMpStagingController(this);
        mpOptionsPane.register(this);
        mpMapSelectionPane.register(this);
        mpSelectionPane.register(this);
        mpPlayerPane.register(this);
        chatPane.register(this);
        playerSelPane.register(this);
        if(transferModel.getTransferModel().getHostController() != null){
            isHost = true;
            hostSetup();
        } else {
            isHost = false;
            clientSetup();
        }
    }

    public void update(Integer choice){
        if(choice == BACK){
            //reset panes
            if(isHost) {
                transferModel.getTransferModel().getHostController().getHostServer().terminate();
            } else {
                transferModel.getTransferModel().getJoinController().endConnection();
            }
            Navigator.loadScene(Navigator.MAINMENU);
        } else if (choice == SEL){
            if(isHost) {
                if (mpMapSelectionPane.getCurrentMapSel() != null) {
                    Integer totalPlayers = servPlayerMap.size();
                    if(totalPlayers.equals(mpMapSelectionPane.getCurrentMapSel().getMaxPlayers())){
                        this.game = new Game(new GameMap(mpMapSelectionPane.getCurrentMapSel()),
                                ((Double) mpOptionsPane.getArmyPointsSlide().getValue()).intValue(),
                                ((Double) mpOptionsPane.getTurnPointsSlide().getValue()).intValue(), Integer.MAX_VALUE);
                        ArrayList<String> names = new ArrayList<String>();
                        for (String key: servPlayerMap.keySet()){
                            names.add(key);
                        }
                        mpPlayerPane.resetPlayerPane(totalPlayers, names);
                        ArrayList<String> privNames = new ArrayList<String>();
                        for (String key: servPlayerMap.keySet()){
                            if(servPlayerMap.get(key) == null){
                                privNames.add(key);
                            }
                        }
                        mpPlayerPane.setupPrivileges(privNames);
                        playerSelPane.setupPriviledges(true, privNames);
                    } else {
                        ErrorPopop.newPopup("You do not have the correct amount of players for that map.\nYou have: " +
                            totalPlayers + "\nNeeded: " + mpMapSelectionPane.getCurrentMapSel().getMaxPlayers());
                    }
                    String gameString = game.buildString();
                    TransferModel.getTransferModel().getHostController().getHostServer().addDataToAll(
                            ClientServer.COMMANDS.MAPSEL, gameString);
                }
            }
        } else if (choice == START){
            if(startValid()){
                Navigator.loadScene(Navigator.SPGAME);
            }
        } else if (choice == ADD){
            if(isHost){
                if(mpPlayerPane != null) {
                    for (Tab mpT : mpPlayerPane.getTabs()) {
                        if(mpT.isSelected()){
                            if (((MPPlayerTab) mpT).hasAddPrivilege()) {
                                ((MPPlayerTab) mpT).addUnit(mpSelectionPane.getSelectedUnit());
                                String addUnitString = ((MPPlayerTab) mpT).getText() + "|" + mpSelectionPane.getSelectedUnit().buildString();
                                TransferModel.getTransferModel().getHostController().getHostServer().addDataToAll(
                                        ClientServer.COMMANDS.ADDUNIT, addUnitString);
                                break; // no need to continue searching if already found
                            }
                        }
                    }
                }
            } else {
                if(mpPlayerPane != null) {
                    for (Tab mpT : mpPlayerPane.getTabs()) {
                        if(mpT.isSelected()){
                            if (((MPPlayerTab) mpT).hasAddPrivilege()) {
                                ((MPPlayerTab) mpT).addUnit(mpSelectionPane.getSelectedUnit());
                                String addUnitString = ((MPPlayerTab) mpT).getText() + "|" + mpSelectionPane.getSelectedUnit().buildString();
                                TransferModel.getTransferModel().getJoinController().getClientConnection().addBufferData(
                                        ClientConnection.COMMANDS.ADDUNIT, addUnitString);
                                break; // no need to continue searching if already found
                            }
                        }
                    }
                }
            }
        } else if (choice == RND){
            if (isHost){
                for (Tab mpT : mpPlayerPane.getTabs()) {
                    if(mpT.isSelected()){
                        if (((MPPlayerTab) mpT).hasAddPrivilege()) {
                            ((MPPlayerTab) mpT).removeAllUnits();
                            TransferModel.getTransferModel().getHostController().getHostServer().addDataToAll(
                                    ClientServer.COMMANDS.RMALL, ((MPPlayerTab) mpT).getText());
                            Random random = new Random();
                            for(int i = 0; i < ((MPPlayerTab) mpT).getMaxUnits(); i++){
                                Integer rindex = random.nextInt(mpSelectionPane.getUnits().size());
                                mpSelectionPane.setSelectedUnit(mpSelectionPane.getUnits().get(rindex).getUnit());
                                mpSelectionPane.getSelectedUnit().setCharName(mpSelectionPane.chooseRandName());
                                ((MPPlayerTab) mpT).addUnit(mpSelectionPane.getSelectedUnit());
                                String addUnitString = ((MPPlayerTab) mpT).getText() + "|" + mpSelectionPane.getSelectedUnit().buildString();
                                TransferModel.getTransferModel().getHostController().getHostServer().addDataToAll(
                                        ClientServer.COMMANDS.ADDUNIT, addUnitString);
                            }
                            break; // no need to continue searching if already found
                        }
                    }
                }
            } else {
                for (Tab mpT : mpPlayerPane.getTabs()) {
                    if(mpT.isSelected()){
                        if (((MPPlayerTab) mpT).hasAddPrivilege()) {
                            ((MPPlayerTab) mpT).removeAllUnits();
                            TransferModel.getTransferModel().getJoinController().getClientConnection().addBufferData(
                                    ClientConnection.COMMANDS.RMALL, ((MPPlayerTab) mpT).getText());
                            Random random = new Random();
                            for(int i = 0; i < ((MPPlayerTab) mpT).getMaxUnits(); i++){
                                Integer rindex = random.nextInt(mpSelectionPane.getUnits().size());
                                mpSelectionPane.setSelectedUnit(mpSelectionPane.getUnits().get(rindex).getUnit());
                                mpSelectionPane.getSelectedUnit().setCharName(mpSelectionPane.chooseRandName());
                                ((MPPlayerTab) mpT).addUnit(mpSelectionPane.getSelectedUnit());
                                String addUnitString = ((MPPlayerTab) mpT).getText() + "|" + mpSelectionPane.getSelectedUnit().buildString();
                                TransferModel.getTransferModel().getJoinController().getClientConnection().addBufferData(
                                        ClientConnection.COMMANDS.ADDUNIT, addUnitString);
                            }
                            break; // no need to continue searching if already found
                        }
                    }
                }
            }
        } else if (choice == CHAT) {
            String chatMessage = activeName + ": " + chatPane.getChatEntry().getText() + "\n";
            chatPane.getChatBox().appendText(chatMessage);
            if (isHost){
                transferModel.getTransferModel().getHostController().getHostServer().addDataToAll(ClientServer.COMMANDS.CHAT,
                        chatMessage);
            } else {
                transferModel.getTransferModel().getJoinController().getClientConnection().addBufferData(ClientConnection.COMMANDS.CHAT,
                        chatMessage);
            }
        } else if (choice == PLAYTYPE){
            if(isHost) {
                Integer amountOpen = 0;
                if (playerSelPane.getCombo2().getValue().equals("OPEN")) {
                    if(playerSelPane.getName2Label().getText().equals("CLOSED")
                            || playerSelPane.getName2Label().getText().equals("OPEN")
                            || playerSelPane.getName2Label().getText().equals("Computer 1")
                            || playerSelPane.getName2Label().getText().equals("Computer 2")
                            || playerSelPane.getName2Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey(playerSelPane.getName2Label().getText())){
                            servPlayerMap.remove(playerSelPane.getName2Label().getText());
                        }
                        amountOpen++;
                        playerSelPane.getName2Label().setText("OPEN");
                    } else {
                        playerSelPane.getCombo2().setValue("FILLED");
                    }
                }
                if (playerSelPane.getCombo3().getValue().equals("OPEN")) {
                    if(playerSelPane.getName3Label().getText().equals("CLOSED")
                            || playerSelPane.getName3Label().getText().equals("OPEN")
                            || playerSelPane.getName3Label().getText().equals("Computer 1")
                            || playerSelPane.getName3Label().getText().equals("Computer 2")
                            || playerSelPane.getName3Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey(playerSelPane.getName3Label().getText())){
                            servPlayerMap.remove(playerSelPane.getName3Label().getText());
                        }
                        amountOpen++;
                        playerSelPane.getName3Label().setText("OPEN");
                    } else {
                        playerSelPane.getCombo3().setValue("FILLED");
                    }
                }
                if (playerSelPane.getCombo4().getValue().equals("OPEN")) {
                    if(playerSelPane.getName4Label().getText().equals("CLOSED")
                            || playerSelPane.getName4Label().getText().equals("OPEN")
                            || playerSelPane.getName4Label().getText().equals("Computer 1")
                            || playerSelPane.getName4Label().getText().equals("Computer 2")
                            || playerSelPane.getName4Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey(playerSelPane.getName4Label().getText())){
                            servPlayerMap.remove(playerSelPane.getName4Label().getText());
                        }
                        playerSelPane.getName4Label().setText("OPEN");
                        amountOpen++;
                    } else {
                        playerSelPane.getCombo4().setValue("FILLED");
                    }
                }
                if (playerSelPane.getCombo2().getValue().equals("COMPUTER")) {
                    if (playerSelPane.getName2Label().getText().equals("CLOSED")
                            || playerSelPane.getName2Label().getText().equals("OPEN")
                            || playerSelPane.getName2Label().getText().equals("Computer 1")
                            || playerSelPane.getName2Label().getText().equals("Computer 2")
                            || playerSelPane.getName2Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey(playerSelPane.getName2Label().getText())){
                            servPlayerMap.remove(playerSelPane.getName2Label().getText());
                        }
                        if(servPlayerMap.containsKey("Computer 1")){
                            if(servPlayerMap.containsKey("Computer 2")){
                                servPlayerMap.put("Computer 3", null);
                                playerSelPane.getName2Label().setText("Computer 3");
                            } else {
                                servPlayerMap.put("Computer 2", null);
                                playerSelPane.getName2Label().setText("Computer 2");
                            }
                        } else {
                            servPlayerMap.put("Computer 1", null);
                            playerSelPane.getName2Label().setText("Computer 1");
                        }
                    } else {
                        playerSelPane.getCombo2().setValue("FILLED");
                    }
                }
                if (playerSelPane.getCombo3().getValue().equals("COMPUTER")) {
                    if(playerSelPane.getName3Label().getText().equals("CLOSED")
                            || playerSelPane.getName3Label().getText().equals("OPEN")
                            || playerSelPane.getName3Label().getText().equals("Computer 1")
                            || playerSelPane.getName3Label().getText().equals("Computer 2")
                            || playerSelPane.getName3Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey(playerSelPane.getName3Label().getText())){
                            servPlayerMap.remove(playerSelPane.getName3Label().getText());
                        }
                        if(servPlayerMap.containsKey("Computer 1")){
                            if(servPlayerMap.containsKey("Computer 2")){
                                servPlayerMap.put("Computer 3", null);
                                playerSelPane.getName3Label().setText("Computer 3");
                            } else {
                                servPlayerMap.put("Computer 2", null);
                                playerSelPane.getName3Label().setText("Computer 2");
                            }
                        } else {
                            servPlayerMap.put("Computer 1", null);
                            playerSelPane.getName3Label().setText("Computer 1");
                        }
                    } else {
                        playerSelPane.getCombo3().setValue("FILLED");
                    }
                }
                if (playerSelPane.getCombo4().getValue().equals("COMPUTER")) {
                    if(playerSelPane.getName4Label().getText().equals("CLOSED")
                            || playerSelPane.getName4Label().getText().equals("OPEN")
                            || playerSelPane.getName4Label().getText().equals("Computer 1")
                            || playerSelPane.getName4Label().getText().equals("Computer 2")
                            || playerSelPane.getName4Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey(playerSelPane.getName4Label().getText())){
                            servPlayerMap.remove(playerSelPane.getName4Label().getText());
                        }
                        if(servPlayerMap.containsKey("Computer 1")){
                            if(servPlayerMap.containsKey("Computer 2")){
                                servPlayerMap.put("Computer 3", null);
                                playerSelPane.getName4Label().setText("Computer 3");
                            } else {
                                servPlayerMap.put("Computer 2", null);
                                playerSelPane.getName4Label().setText("Computer 2");
                            }
                        } else {
                            servPlayerMap.put("Computer 1", null);
                            playerSelPane.getName4Label().setText("Computer 1");
                        }
                    } else {
                        playerSelPane.getCombo4().setValue("FILLED");
                    }
                }
                if (playerSelPane.getCombo2().getValue().equals("CLOSED")) {
                    if(playerSelPane.getName2Label().getText().equals("Computer 1")){
                        if(servPlayerMap.containsKey("Computer 1")){
                            servPlayerMap.remove("Computer 1");
                        }
                    } else if (playerSelPane.getName2Label().getText().equals("Computer 2")){
                        if(servPlayerMap.containsKey("Computer 2")){
                            servPlayerMap.remove("Computer 2");
                        }
                    } else if (playerSelPane.getName2Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey("Computer 3")){
                            servPlayerMap.remove("Computer 3");
                        }
                    } else if (playerSelPane.getName2Label().getText().equals("OPEN") ||
                            playerSelPane.getName2Label().getText().equals("CLOSED")){

                    } else { //kick a player
                        if(servPlayerMap.containsKey(playerSelPane.getName2Label().getText())){
                            ClientServer serverToKill = servPlayerMap.get(playerSelPane.getName2Label().getText());
                            servPlayerMap.remove(playerSelPane.getName2Label().getText());
                            transferModel.getTransferModel().getHostController().getHostServer().killConnection(serverToKill, "KICK");
                        }
                    }
                    playerSelPane.getName2Label().setText(playerSelPane.getCombo2().getValue());
                }
                if (playerSelPane.getCombo3().getValue().equals("CLOSED")) {
                    if(playerSelPane.getName3Label().getText().equals("Computer 1")){
                        if(servPlayerMap.containsKey("Computer 1")){
                            servPlayerMap.remove("Computer 1");
                        }
                    } else if (playerSelPane.getName3Label().getText().equals("Computer 2")){
                        if(servPlayerMap.containsKey("Computer 2")){
                            servPlayerMap.remove("Computer 2");
                        }
                    } else if (playerSelPane.getName3Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey("Computer 3")){
                            servPlayerMap.remove("Computer 3");
                        }
                    } else if (playerSelPane.getName3Label().getText().equals("OPEN") ||
                            playerSelPane.getName3Label().getText().equals("CLOSED")){

                    } else { //kick a player
                        if(servPlayerMap.containsKey(playerSelPane.getName3Label().getText())){
                            ClientServer serverToKill = servPlayerMap.get(playerSelPane.getName3Label().getText());
                            servPlayerMap.remove(playerSelPane.getName3Label().getText());
                            transferModel.getTransferModel().getHostController().getHostServer().killConnection(serverToKill, "KICK");
                        }
                    }
                    playerSelPane.getName3Label().setText(playerSelPane.getCombo3().getValue());
                }
                if (playerSelPane.getCombo4().getValue().equals("CLOSED")) {
                    if(playerSelPane.getName4Label().getText().equals("Computer 1")){
                        if(servPlayerMap.containsKey("Computer 1")){
                            servPlayerMap.remove("Computer 1");
                        }
                    } else if (playerSelPane.getName4Label().getText().equals("Computer 2")){
                        if(servPlayerMap.containsKey("Computer 2")){
                            servPlayerMap.remove("Computer 2");
                        }
                    } else if (playerSelPane.getName4Label().getText().equals("Computer 3")){
                        if(servPlayerMap.containsKey("Computer 3")){
                            servPlayerMap.remove("Computer 3");
                        }
                    } else if (playerSelPane.getName4Label().getText().equals("OPEN") ||
                            playerSelPane.getName4Label().getText().equals("CLOSED")){

                    } else { //kick a player
                        if(servPlayerMap.containsKey(playerSelPane.getName4Label().getText())){
                            ClientServer serverToKill = servPlayerMap.get(playerSelPane.getName4Label().getText());
                            servPlayerMap.remove(playerSelPane.getName4Label().getText());
                            transferModel.getTransferModel().getHostController().getHostServer().killConnection(serverToKill, "KICK");
                        }
                    }
                    playerSelPane.getName4Label().setText(playerSelPane.getCombo4().getValue());
                }

                if (playerSelPane.getCombo2().getValue().equals("FILLED")) {
                    if(playerSelPane.getName2Label().getText().equals("Computer 1")){
                        playerSelPane.getCombo2().setValue("COMPUTER");
                    } else if (playerSelPane.getName2Label().getText().equals("Computer 2")){
                        playerSelPane.getCombo2().setValue("COMPUTER");
                    } else if (playerSelPane.getName2Label().getText().equals("Computer 3")){
                        playerSelPane.getCombo2().setValue("COMPUTER");
                    } else if (playerSelPane.getName2Label().getText().equals("OPEN") ||
                            playerSelPane.getName2Label().getText().equals("CLOSED")){
                        playerSelPane.getCombo2().setValue(playerSelPane.getName2Label().getText());
                    } else {
                        amountOpen++;
                    }
                }
                if (playerSelPane.getCombo3().getValue().equals("FILLED")) {
                    if(playerSelPane.getName3Label().getText().equals("Computer 1")){
                        playerSelPane.getCombo3().setValue("COMPUTER");
                    } else if (playerSelPane.getName3Label().getText().equals("Computer 2")){
                        playerSelPane.getCombo3().setValue("COMPUTER");
                    } else if (playerSelPane.getName3Label().getText().equals("Computer 3")){
                        playerSelPane.getCombo3().setValue("COMPUTER");
                    } else if (playerSelPane.getName3Label().getText().equals("OPEN") ||
                            playerSelPane.getName3Label().getText().equals("CLOSED")){
                        playerSelPane.getCombo3().setValue(playerSelPane.getName3Label().getText());
                    } else {
                        amountOpen++;
                    }
                }
                if (playerSelPane.getCombo4().getValue().equals("FILLED")) {
                    if(playerSelPane.getName4Label().getText().equals("Computer 1")){
                        playerSelPane.getCombo4().setValue("COMPUTER");
                    } else if (playerSelPane.getName4Label().getText().equals("Computer 2")){
                        playerSelPane.getCombo4().setValue("COMPUTER");
                    } else if (playerSelPane.getName4Label().getText().equals("Computer 3")){
                        playerSelPane.getCombo4().setValue("COMPUTER");
                    } else if (playerSelPane.getName4Label().getText().equals("OPEN") ||
                            playerSelPane.getName4Label().getText().equals("CLOSED")){
                        playerSelPane.getCombo4().setValue(playerSelPane.getName4Label().getText());
                    } else {
                        amountOpen++;
                    }
                }

                transferModel.getTransferModel().getHostController().getHostServer().setMAXCONS(amountOpen);
                String playerString = activeName + ":" + playerSelPane.getName2Label().getText() +
                        ":" + playerSelPane.getName3Label().getText()  + ":" + playerSelPane.getName4Label().getText()  + ":";
                TransferModel.getTransferModel().getHostController().getHostServer().addDataToAll(
                        ClientServer.COMMANDS.PLAYCHNG, playerString);
                //end is host
            }
        } else if (choice == REMOVEUNIT){
            if(isHost){
                if(mpPlayerPane != null) {
                    for (Tab mpT : mpPlayerPane.getTabs()) {
                        if(mpT.isSelected()){
                            if (((MPPlayerTab) mpT).hasAddPrivilege()) {
                                String rmUnitString = ((MPPlayerTab) mpT).getText() + "|" + mpSelectionPane.getSelectedUnit().buildString();
                                TransferModel.getTransferModel().getHostController().getHostServer().addDataToAll(
                                        ClientServer.COMMANDS.RMUNIT, rmUnitString);
                                break; // no need to continue searching if already found
                            }
                        }
                    }
                }
            } else {
                if(mpPlayerPane != null) {
                    for (Tab mpT : mpPlayerPane.getTabs()) {
                        if(mpT.isSelected()){
                            if (((MPPlayerTab) mpT).hasAddPrivilege()) {
                                ((MPPlayerTab) mpT).addUnit(mpSelectionPane.getSelectedUnit());
                                String addUnitString = ((MPPlayerTab) mpT).getText() + "|" + mpSelectionPane.getSelectedUnit().buildString();
                                TransferModel.getTransferModel().getJoinController().getClientConnection().addBufferData(
                                        ClientConnection.COMMANDS.RMUNIT, addUnitString);
                                break; // no need to continue searching if already found
                            }
                        }
                    }
                }
            }
        }
    }

    private void hostSetup(){
        activeName = transferModel.getTransferModel().getHostController().getNameField().getText();
        servPlayerMap.put(activeName, null);
        playerSelPane.getName1Label().setText(activeName);
        mpMapSelectionPane.hostPrivileges();
        mpOptionsPane.hostPrivileges();

        ArrayList<String> name = new ArrayList<String>();
        name.add(activeName);
        playerSelPane.setupPriviledges(true, name);
    }

    private void clientSetup(){
        activeName = transferModel.getTransferModel().getJoinController().getNameField().getText();
    }

    public boolean startValid(){
        return true;
    }

    public void clientUpdate(ClientConnection.COMMANDS command, final String data){
        if(command == ClientConnection.COMMANDS.ADDUNIT){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (data.contains("|")) {
                        String playerName = data.substring(0, data.indexOf("|"));
                        String unitData = data.substring(data.indexOf("|")+ 1);
                        if(mpPlayerPane != null) {
                            for (Tab mpT : mpPlayerPane.getTabs()) {
                                if(mpT.getText().equals(playerName)){
                                    ((MPPlayerTab) mpT).addUnit(new Unit(unitData));
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        } else if(command == ClientConnection.COMMANDS.CHAT){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatPane.getChatBox().appendText(data);
                }
            });
        } else if(command == ClientConnection.COMMANDS.MAPSEL){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    game = new Game(data);
                    mpMapSelectionPane.setMapData(game.getGameMap());
                    if(!mpMapSelectionPane.mapExists(game.getGameMap())){
                        mpMapSelectionPane.saveMap(game.getGameMap());
                    }
                    ArrayList<String> names = new ArrayList<String>();
                    if(!playerSelPane.getName1Label().getText().equals("OPEN") &&
                            !playerSelPane.getName1Label().getText().equals("CLOSED")){
                        names.add(playerSelPane.getName1Label().getText());
                    }
                    if(!playerSelPane.getName2Label().getText().equals("OPEN") &&
                            !playerSelPane.getName2Label().getText().equals("CLOSED")){
                        names.add(playerSelPane.getName2Label().getText());
                    }
                    if(!playerSelPane.getName3Label().getText().equals("OPEN") &&
                            !playerSelPane.getName3Label().getText().equals("CLOSED")){
                        names.add(playerSelPane.getName3Label().getText());
                    }
                    if(!playerSelPane.getName4Label().getText().equals("OPEN") &&
                            !playerSelPane.getName4Label().getText().equals("CLOSED")){
                        names.add(playerSelPane.getName4Label().getText());
                    }
                    mpPlayerPane.resetPlayerPane(game.getPlayers().size(), names);
                    ArrayList<String> privNames = new ArrayList<String>();
                    privNames.add(activeName);
                    mpPlayerPane.setupPrivileges(privNames);
                    playerSelPane.setupPriviledges(true, privNames);
                }
            });
        } else if(command == ClientConnection.COMMANDS.PLAYCHNG){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String peeledData;
                    String name1;
                    String name2;
                    String name3;
                    String name4;

                    if(data.indexOf(":") != -1){
                        name1 = data.substring(0, data.indexOf(":"));
                        peeledData = data.substring(data.indexOf(":") + 1);
                        if(name1 != ""){
                            playerSelPane.getName1Label().setText(name1);
                            if (name1.equals("Computer 1") || name1.equals("Computer 2") ||
                                    name1.equals("Computer 3")) {
                                playerSelPane.getCombo1().setValue("COMPUTER");
                            } else if(name1.equals("OPEN") || name1.equals("CLOSED")){
                                playerSelPane.getCombo1().setValue(name1);
                            } else {
                                playerSelPane.getCombo1().setValue("FILLED");
                            }
                        }
                        if(peeledData.indexOf(":") != -1){
                            name2 = peeledData.substring(0, peeledData.indexOf(":"));
                            peeledData = peeledData.substring(peeledData.indexOf(":") + 1);
                            playerSelPane.getName2Label().setText(name2);
                            if (name2.equals("Computer 1") || name2.equals("Computer 2") ||
                                    name2.equals("Computer 3")) {
                                playerSelPane.getCombo2().setValue("COMPUTER");
                            } else if(name2.equals("OPEN") || name2.equals("CLOSED")){
                                playerSelPane.getCombo2().setValue(name2);
                            } else {
                                playerSelPane.getCombo2().setValue("FILLED");
                            }
                            if(peeledData.indexOf(":") != -1){
                                name3 = peeledData.substring(0, peeledData.indexOf(":"));
                                peeledData = peeledData.substring(peeledData.indexOf(":") + 1);
                                playerSelPane.getName3Label().setText(name3);
                                if (name3.equals("Computer 1") || name3.equals("Computer 2") ||
                                        name3.equals("Computer 3")) {
                                    playerSelPane.getCombo3().setValue("COMPUTER");
                                } else if(name3.equals("OPEN") || name3.equals("CLOSED")){
                                    playerSelPane.getCombo3().setValue(name3);
                                } else {
                                    playerSelPane.getCombo3().setValue("FILLED");
                                }
                                if(peeledData.indexOf(":") != -1){
                                    name4 = peeledData.substring(0, peeledData.indexOf(":"));
                                    playerSelPane.getName4Label().setText(name4);
                                    if (name4.equals("Computer 1") || name4.equals("Computer 2") ||
                                            name4.equals("Computer 3")) {
                                        playerSelPane.getCombo4().setValue("COMPUTER");
                                    } else if(name4.equals("OPEN") || name4.equals("CLOSED")){
                                        playerSelPane.getCombo4().setValue(name4);
                                    } else {
                                        playerSelPane.getCombo4().setValue("FILLED");
                                    }
                                }
                            }
                        }
                    }
                    if(!firstPLYCHNG){
                        ArrayList<String> name = new ArrayList<String>();
                        name.add(activeName);
                        playerSelPane.setupPriviledges(false, name);
                        firstPLYCHNG = true;
                    }
                }
            });
        } else if(command == ClientConnection.COMMANDS.RMALL){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (Tab mpT : mpPlayerPane.getTabs()) {
                        if (mpT.isSelected()) {
                            if(data.equals(mpT.getText())){
                                ((MPPlayerTab) mpT).removeAllUnits();
                            }
                        }
                    }
                }
            });
        } else if(command == ClientConnection.COMMANDS.RMUNIT){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (data.contains("|")) {
                        String playerName = data.substring(0, data.indexOf("|"));
                        String unitData = data.substring(data.indexOf("|") + 1);
                        for (Tab mpT : mpPlayerPane.getTabs()) {
                            if (playerName.equals(mpT.getText())) {
                                for (Unit u : ((MPPlayerTab) mpT).getUnitsList()) {
                                    if (u.equals(new Unit(unitData))) {
                                        ((MPPlayerTab) mpT).setSelectedUnit(u);
                                        ((MPPlayerTab) mpT).removeSelectedUnit();
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public void serverUpdate(ClientServer.COMMANDS command, final String data, final ClientServer sender){
        if(command == ClientServer.COMMANDS.ADDUNIT){
            for(ClientServer other: transferModel.getTransferModel().getHostController().getHostServer().getClientServers()){
                if(other != sender){
                    other.addBufferData(ClientServer.COMMANDS.ADDUNIT, data);
                }
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (data.contains("|")) {
                        String playerName = data.substring(0, data.indexOf("|"));
                        String unitData = data.substring(data.indexOf("|")+ 1);
                        if(mpPlayerPane != null) {
                            for (Tab mpT : mpPlayerPane.getTabs()) {
                                if(mpT.getText().equals(playerName)){
                                    ((MPPlayerTab) mpT).addUnit(new Unit(unitData));
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        } if(command == ClientServer.COMMANDS.CHAT){
            for(ClientServer other: transferModel.getTransferModel().getHostController().getHostServer().getClientServers()){
                if(other != sender){
                    other.addBufferData(ClientServer.COMMANDS.CHAT, data);
                }
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatPane.getChatBox().appendText(data);
                }
            });
        } else if (command == ClientServer.COMMANDS.CLOSE){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String playerName = null;
                            if(servPlayerMap.containsValue(sender)){
                                for (HashMap.Entry<String, ClientServer> entry :servPlayerMap.entrySet()){
                                    if(entry.getValue() == sender){
                                        playerName = entry.getKey();
                                        break;
                                    }
                                }
                                if(playerName != null){
                                    servPlayerMap.remove(playerName);
                                    if(playerSelPane.getName2Label().getText().equals(playerName)){
                                        playerSelPane.getName2Label().setText("CLOSED");
                                        playerSelPane.getCombo2().setValue("CLOSED");
                                    } else if(playerSelPane.getName3Label().getText().equals(playerName)){
                                        playerSelPane.getName3Label().setText("CLOSED");
                                        playerSelPane.getCombo3().setValue("CLOSED");
                                    } else if(playerSelPane.getName4Label().getText().equals(playerName)){
                                        playerSelPane.getName4Label().setText("CLOSED");
                                        playerSelPane.getCombo4().setValue("CLOSED");
                                    }
                                }
                            }

                            TransferModel.getTransferModel().getHostController().getHostServer().killConnection(sender, data);
                        }
                    });
                }
            });
        } else if (command == ClientServer.COMMANDS.JOINROOM){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(!servPlayerMap.containsKey(data) && !data.equals("") && !data.equals("Computer 1") &&
                            !data.equals("Computer 2") && !data.equals("Computer 3") &&
                            !data.equals("OPEN") && !data.equals("CLOSED")) {
                        servPlayerMap.put(data, sender);
                        if (playerSelPane.getCombo2().getValue().equals("OPEN")) {
                            playerSelPane.getName2Label().setText(data);
                            playerSelPane.getCombo2().setValue("FILLED");
                        } else if (playerSelPane.getCombo3().getValue().equals("OPEN")) {
                            playerSelPane.getName3Label().setText(data);
                            playerSelPane.getCombo3().setValue("FILLED");
                        } else if (playerSelPane.getCombo4().getValue().equals("OPEN")) {
                            playerSelPane.getName4Label().setText(data);
                            playerSelPane.getCombo4().setValue("FILLED");
                        } else {
                            transferModel.getTransferModel().getHostController().getHostServer().killConnection(sender, "NOSLOTS");
                        }
                    } else {
                        transferModel.getTransferModel().getHostController().getHostServer().killConnection(sender, "NAME");
                    }
                }
            });
        } else if (command == ClientServer.COMMANDS.RMALL){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (Tab mpT : mpPlayerPane.getTabs()) {
                        if (mpT.isSelected()) {
                            if(data.equals(mpT.getText())){
                                ((MPPlayerTab) mpT).removeAllUnits();
                            }
                        }
                    }
                    for(ClientServer other: transferModel.getTransferModel().getHostController().getHostServer().getClientServers()){
                        if(other != sender){
                            other.addBufferData(ClientServer.COMMANDS.RMALL, data);
                        }
                    }
                }
            });
        } else if (command == ClientServer.COMMANDS.RMUNIT){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (data.contains("|")) {
                        String playerName = data.substring(0, data.indexOf("|"));
                        String unitData = data.substring(data.indexOf("|")+ 1);
                        for (Tab mpT : mpPlayerPane.getTabs()) {
                            if(playerName.equals(mpT.getText())){
                                for (Unit u : ((MPPlayerTab) mpT).getUnitsList()){
                                    if(u.equals(new Unit(unitData))){
                                        ((MPPlayerTab) mpT).setSelectedUnit(u);
                                        ((MPPlayerTab) mpT).removeSelectedUnit();
                                    }
                                }
                            }
                        }
                        for(ClientServer other: transferModel.getTransferModel().getHostController().getHostServer().getClientServers()){
                            if(other != sender){
                                other.addBufferData(ClientServer.COMMANDS.RMALL, data);
                            }
                        }
                    }
                }
            });
        }
    }

    public Game getGame() {
        return game;
    }
}
