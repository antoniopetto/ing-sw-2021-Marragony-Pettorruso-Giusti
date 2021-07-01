package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;

public class BuyResourcesMsg implements CommandMsg {
    private int idLine;
    private boolean isRow;

    public BuyResourcesMsg(int idLine, boolean isRow) {
        this.idLine = idLine;
        this.isRow = isRow;
    }

    @Override
    public void execute(GameController gameController){
        gameController.buyResources(idLine, isRow);
    }

    @Override
    public String toString() {
        return "BuyResourcesMsg{" +
                "idLine=" + idLine +
                ", isRow=" + isRow +
                '}';
    }
}
