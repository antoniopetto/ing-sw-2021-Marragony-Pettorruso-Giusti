package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.Marble;

import java.util.ArrayList;
import java.util.List;

public class CreateBufferMsg implements UpdateMsg {
    private List<Marble> marbleBuffer;

    public CreateBufferMsg(List<Marble> marbleBuffer) {
        this.marbleBuffer = new ArrayList<>(marbleBuffer);
    }

    @Override
    public void execute(SimpleModel model) {
        model.setMarbleBuffer(marbleBuffer);
    }

    @Override
    public String toString() {
        return "CreateBufferMsg{" +
                "marbleBuffer=" + marbleBuffer +
                '}';
    }
}
