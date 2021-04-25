package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.shared.messages.server.BufferUpdateMsg;
import it.polimi.ingsw.shared.messages.view.ErrorMsg;
import it.polimi.ingsw.shared.messages.server.ServerMsg;

import java.io.IOException;

public class DiscardMsg implements CommandMsg {
    private Marble marble;

    public DiscardMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        String text;
        ServerMsg msg;
        try{
            game.discard(marble);
            msg = new BufferUpdateMsg(marble);
        }catch (ElementNotFoundException e)
        {
            text="Impossible to discard";
            msg = new ErrorMsg(text);
        }
        handler.writeObject(msg);
    }
}
