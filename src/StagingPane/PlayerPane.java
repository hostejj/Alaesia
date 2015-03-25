package StagingPane;

import javafx.scene.control.TabPane;

public class PlayerPane extends TabPane{
    private StagingController observer;

    public PlayerPane(){

    }

    public void resetPlayerPane(){
        //remove old tabs
        for(int i = this.getTabs().size() - 1; i >= 0; i--){
            this.getTabs().remove(i);
        }

        for(int i = 0; i < observer.getGame().getGameMap().getMaxPlayers(); i++){
            if(i==0) {
                this.getTabs().add(new PlayerTab("Player", this, observer.getGame().getGameMap().getStartLocs().get(i).size()));
            } else {
                this.getTabs().add(new PlayerTab("Computer " + i, this, observer.getGame().getGameMap().getStartLocs().get(i).size()));
            }
        }
    }

    public void register(StagingController stagingController){
        this.observer = stagingController;
    }

    public StagingController getObserver() {
        return observer;
    }
}
