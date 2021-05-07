package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.update.UpdateMsg;

import java.io.IOException;

public class DiscardMsg implements CommandMsg {
    private Marble marble;

    public DiscardMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        String text;
        UpdateMsg msg;
        game.discardMarble(marble);
    }
}