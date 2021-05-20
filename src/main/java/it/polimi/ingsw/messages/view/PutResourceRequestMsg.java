package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.CommandMsg;
import it.polimi.ingsw.messages.command.DiscardLeaderCardMsg;
import it.polimi.ingsw.messages.command.GoBackMsg;
import it.polimi.ingsw.messages.command.PutResourceMsg;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;

import java.io.IOException;

public class PutResourceRequestMsg implements ViewMsg {

    @Override
    public void changeView(View view, ServerHandler server) throws IOException {

        CommandMsg msg;
        Marble selectedMarble = view.selectMarble();
        DepotName selectedDepot = view.selectDepot();
        if (selectedDepot == null) {
            msg = new GoBackMsg();
            server.writeObject(msg);
            return;
        }
        if (selectedMarble.equals(Marble.WHITE)) {
            Resource selectedResource = view.selectResource();
            msg = new PutResourceMsg(selectedMarble, selectedDepot, selectedResource);
        } else
            msg = new PutResourceMsg(selectedMarble, selectedDepot);
        server.writeObject(msg);

    }

    @Override
    public String toString() {
        return "PutResourceRequestMsg{}";
    }
}
