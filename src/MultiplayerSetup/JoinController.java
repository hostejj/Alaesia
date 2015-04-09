package MultiplayerSetup;

import GUIs.ErrorPopop;
import GUIs.Navigator;
import GUIs.TransferModel;
import Networking.ClientConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class JoinController implements Initializable{
    @FXML private TextField IPPortField;
    @FXML private TextField nameField;

    private TransferModel transferModel = new TransferModel();
    private ClientConnection clientConnection = null;
    private Thread clientThread = null;
    private boolean guiOpen = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transferModel.getTransferModel().setJoinController(this);
        if(transferModel.getTransferModel().getHostController() != null){
            transferModel.getTransferModel().setHostController(null);
        }
    }

    public void handleJoin() {
        try {
            String text = IPPortField.getText();
            if(text.indexOf(":") != -1) {
                String IP = text.substring(0, text.indexOf(":"));
                Integer port = Integer.parseInt(text.substring(text.indexOf(":") + 1, text.length()));
                if ((port > 2048) && (port < 65000) && (Inet4Address.getByName(IP) != null)) {
                    if (clientConnection == null) {
                        clientConnection = new ClientConnection(IP, port, this);
                        if (clientConnection.getClientSocket() != null) {
                            clientThread = new Thread(clientConnection);
                            clientThread.start();
                        } else {
                            ErrorPopop.newPopup("No games found at that IP:PORT combination");
                            clientConnection = null;
                        }
                    } else {
                        if (clientConnection.getClientSocket().isClosed()) {
                            endConnection();
                            clientConnection = null;
                            handleJoin();
                        }
                    }
                }
            }
        } catch (UnknownHostException uhe){
            ErrorPopop.newPopup("Invalid IP:PORT combination. Please check your entry.");
            System.err.println(uhe.toString());
        } catch (NumberFormatException nfe){
            ErrorPopop.newPopup("Invalid IP:PORT combination. Please check your entry.");
            System.err.println(nfe.toString());
        }
    }

    public TextField getNameField() {
        return nameField;
    }

    public void handleBack(){
        Navigator.loadScene(Navigator.MAINMENU);
    }

    public void endConnection(){
        try {
            if(clientConnection != null && clientThread != null) {
                clientConnection.terminate();
                clientThread.join();
            }
        } catch (InterruptedException ie){
            System.err.println(ie.toString());
        }
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void clientUpdate(ClientConnection.COMMANDS command, final String data){
        if((ClientConnection.COMMANDS.HBEATR == command) && !guiOpen){
            guiOpen = true;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Navigator.loadScene(Navigator.MPSTAGE);
                    //joined the connection send the current name to signify room entry
                    //expect to receive the current room data
                    clientConnection.addBufferData(ClientConnection.COMMANDS.JOINROOM, nameField.getText());
                }
            });
        } else if(ClientConnection.COMMANDS.CLOSE == command){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    endConnection();
                    if(data.equals("NAME")){
                        ErrorPopop.newPopup("Sorry, cannot join the server with the current name. It is already taken!");
                    } else if (data.equals("NOSLOTS")){
                        ErrorPopop.newPopup("Sorry, there seem to be no open slots in the room.");
                    } else if (data.equals("CONTIMEOUT")){
                        ErrorPopop.newPopup("Sorry, Connection to server lost because the connection timed out!");
                    } else if (data.equals("KICK")){
                        ErrorPopop.newPopup("Sorry, the host has kicked you from the room!");
                    } else {
                        ErrorPopop.newPopup("Sorry, Connection to server lost for an unknown reason!");
                    }

                    Navigator.loadScene(Navigator.JOIN);
                }
            });
        } else {
            transferModel.getTransferModel().getMpStagingController().clientUpdate(command, data);
        }
    }
}
