package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.update.UpdateMsg;

import java.io.IOException;

public class DiscardMarbleMsg implements CommandMsg {
    private Marble marble;

    public DiscardMarbleMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        game.discardMarble(marble);
    }

    @Override
    public String toString() {
        return "DiscardMsg{" +
                "marble=" + marble +
                '}';
    }
}
