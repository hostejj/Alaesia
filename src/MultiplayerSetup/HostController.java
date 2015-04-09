package MultiplayerSetup;

import GUIs.ErrorPopop;
import GUIs.Navigator;
import GUIs.TransferModel;
import Networking.ClientServer;
import Networking.HostServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HostController implements Initializable {
    @FXML private TextField portField;
    @FXML private TextField nameField;

    private TransferModel transferModel = new TransferModel();
    private HostServer hostServer = null;
    private Thread hostThread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transferModel.getTransferModel().setHostController(this);
        if(transferModel.getTransferModel().getJoinController() != null){
            transferModel.getTransferModel().setJoinController(null);
        }
    }

    public void handleAccept(){
        try {
            if ((Integer.parseInt(portField.getText()) > 2048) && (Integer.parseInt(portField.getText()) < 65000)) {
                if(hostServer == null) {
                    hostServer = new HostServer(Integer.parseInt(portField.getText()), this);
                    if(hostServer.isBound()) {
                        hostThread = new Thread(hostServer);
                        hostThread.start();
                        Navigator.loadScene(Navigator.MPSTAGE);
                    } else {
                        hostServer = null;
                    }
                }
            } else {
                ErrorPopop.newPopup("Port numbers must be between 2049 and 64599.");
            }
        } catch (NumberFormatException nfe){
            ErrorPopop.newPopup("Not a valid port number.");
            System.err.println(nfe.toString());
        }
    }

    public TextField getNameField() {
        return nameField;
    }

    public HostServer getHostServer() {
        return hostServer;
    }

    public void endHost(){
        if(hostServer != null){
            hostServer.terminate();
        }
        if(hostThread != null){
            try {
                hostThread.join();
            } catch (InterruptedException ie){
                System.err.println(ie.toString());
            }
        }
    }

    public void handleBack(){
        Navigator.loadScene(Navigator.MAINMENU);
    }

    public void serverUpdate(ClientServer.COMMANDS command, final String data, final ClientServer sender){
        transferModel.getTransferModel().getMpStagingController().serverUpdate(command, data, sender);
    }
}