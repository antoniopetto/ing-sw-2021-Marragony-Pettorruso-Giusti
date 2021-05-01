package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.shared.messages.update.CreateBufferMsg;

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
        CreateBufferMsg msg = new CreateBufferMsg(game.getMarbleBuffer());
        handler.writeObject(msg);
    }
}
