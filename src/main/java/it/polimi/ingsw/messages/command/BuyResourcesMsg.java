package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;

public class BuyResourcesMsg implements CommandMsg {
    private int idLine;
    private boolean isRow;

    public BuyResourcesMsg(int idLine, boolean isRow) {
        this.idLine = idLine;
        this.isRow = isRow;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        game.buyResources(idLine, isRow);
    }

    @Override
    public String toString() {
        return "BuyResourcesMsg{" +
                "idLine=" + idLine +
                ", isRow=" + isRow +
                '}';
    }
}
