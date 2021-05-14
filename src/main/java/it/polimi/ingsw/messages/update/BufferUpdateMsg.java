package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.shared.Marble;

public class BufferUpdateMsg implements UpdateMsg {
    private Marble marble;

    public BufferUpdateMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(SimpleGame model) {
        model.reduceBuffer(marble);
    }
}
