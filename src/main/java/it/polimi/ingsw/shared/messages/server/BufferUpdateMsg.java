package it.polimi.ingsw.shared.messages.server;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.shared.Marble;

public class BufferUpdateMsg implements ServerMsg {
    private Marble marble;

    public BufferUpdateMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(SimpleGame model) {
        model.reduceBuffer(marble);
        model.getPlayers().get(0).getView().bufferUpdate(marble);

    }
}
