package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.server.model.shared.Marble;

public class BufferUpdateMsg implements UpdateMsg {
    private Marble marble;

    public BufferUpdateMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(SimpleModel model) {
        model.reduceBuffer(marble);
    }

    @Override
    public String toString() {
        return "BufferUpdateMsg{" +
                "marble=" + marble +
                '}';
    }
}
