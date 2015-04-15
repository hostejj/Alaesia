package MultiplayerSetup;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.ArrayList;

public class MPPlayerPane extends TabPane {
    private MPStagingController observer;

    public MPPlayerPane(){}

    public void resetPlayerPane(Integer numPlayers, ArrayList<String> names){
        //remove old tabs
        for(int i = this.getTabs().size() - 1; i >= 0; i--){
            this.getTabs().remove(i);
        }

        for(int i = 0; i < numPlayers; i++){
            if(i < names.size()) {
                this.getTabs().add(new MPPlayerTab(names.get(i), this, observer.getGame().getGameMap().getStartLocs().get(i).size()));
            }
        }
    }

    public void register(MPStagingController mpStagingController){
        this.observer = mpStagingController;
    }

    public void setupPrivileges(ArrayList<String> names){
        for(String name: names){
            for (Tab mppt: getTabs()){
                if (((MPPlayerTab) mppt).getText().equals(name)){
                    ((MPPlayerTab) mppt).setupPrivileges();
                    break;
                }
            }
        }
    }

    public MPStagingController getObserver() {
        return observer;
    }
}
