package GUIs;

public interface BoardPaneObserver {

    //method to update the observer, used by subject
    public void updateBoard(BoardPaneButton boardPaneButton, Integer click);
}
