package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.Marble;

/**
 * Update message containing the marble to remove from the client's marblebuffer
 */
public class BufferUpdateMsg implements UpdateMsg {
    private final Marble marble;

    public BufferUpdateMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(SimpleModel model) {
        model.reduceMarbleBuffer(marble);
    }

    @Override
    public String toString() {
        return "BufferUpdateMsg{" +
                "marble=" + marble +
                '}';
    }
}
