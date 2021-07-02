package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;

/**
 * It is called when a player decides to buy a row or a col (in last case boolean isRow = false ) of MarketBoard
 * Marbles of selected line will make up MarbleBuffer
 *
 */
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
