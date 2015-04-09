package GUIs;

import EditorPane.EditorController;
import MultiplayerSetup.HostController;
import MultiplayerSetup.JoinController;
import MultiplayerSetup.MPStagingController;
import SPGamePane.SPGamePaneController;
import StagingPane.StagingController;

/**
 * Transfers data between the different controllers of the program.
 */
public class TransferModel {
    private static final TransferModel transferModel = new TransferModel();
    private HostController hostController;
    private JoinController joinController;
    private MPStagingController mpStagingController;
    private EditorController editorController;
    private SPGamePaneController spGamePaneController;
    private StagingController stagingController;

    public TransferModel(){

    }

    public static TransferModel getTransferModel() {
        return transferModel;
    }

    public HostController getHostController() {
        return hostController;
    }

    public void setHostController(HostController hostController) {
        this.hostController = hostController;
    }

    public JoinController getJoinController() {
        return joinController;
    }

    public void setJoinController(JoinController joinController) {
        this.joinController = joinController;
    }

    public MPStagingController getMpStagingController() {
        return mpStagingController;
    }

    public void setMpStagingController(MPStagingController mpStagingController) {
        this.mpStagingController = mpStagingController;
    }

    public EditorController getEditorController() {
        return editorController;
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }

    public SPGamePaneController getSpGamePaneController() {
        return spGamePaneController;
    }

    public void setSpGamePaneController(SPGamePaneController spGamePaneController) {
        this.spGamePaneController = spGamePaneController;
    }

    public StagingController getStagingController() {
        return stagingController;
    }

    public void setStagingController(StagingController stagingController) {
        this.stagingController = stagingController;
    }
}
