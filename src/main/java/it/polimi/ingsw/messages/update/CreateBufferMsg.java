package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.PutResourceMsg;
import it.polimi.ingsw.messages.view.ViewMsg;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.shared.Marble;

import java.io.IOException;
import java.util.List;

public class CreateBufferMsg implements ViewMsg {
    private List<Marble> marbleBuffer;

    public CreateBufferMsg(List<Marble> marbleBuffer) {
        this.marbleBuffer = marbleBuffer;
    }

    @Override
    public void changeView(View view, ServerHandler server) throws IOException {
        view.getGame().setMarbleBuffer(marbleBuffer);
    }
}
