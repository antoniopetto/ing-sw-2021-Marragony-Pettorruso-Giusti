package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.List;

public class CreateBufferMsg implements UpdateMsg {
    private List<Marble> marbleBuffer;

    public CreateBufferMsg(List<Marble> marbleBuffer) {
        this.marbleBuffer = marbleBuffer;
    }

    @Override
    public void execute(SimpleGame model) {
        model.setMarbleBuffer(marbleBuffer);
    }
}
