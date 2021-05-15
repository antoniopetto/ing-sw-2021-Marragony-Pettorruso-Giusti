package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.io.IOException;

public class DepotActionMsg implements ViewMsg{
    @Override
    public void changeView(View view, ServerHandler server) throws IOException {
        CommandMsg msg = view.depotMove();
        server.writeObject(msg);
    }
}
