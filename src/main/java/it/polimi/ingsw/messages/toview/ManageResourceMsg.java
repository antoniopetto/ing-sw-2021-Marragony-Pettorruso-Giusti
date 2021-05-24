package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.io.IOException;

public class ManageResourceMsg implements ViewMsg{
    @Override
    public void changeView(View view, ServerHandler server) throws IOException {
        CommandMsg msg = view.manageResource();
        server.writeObject(msg);
    }

    @Override
    public String toString() {
        return "ManageResourceMsg{}";
    }
}
