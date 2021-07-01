package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.command.CommandMsg;
import it.polimi.ingsw.shared.messages.command.PutResourceMsg;
import it.polimi.ingsw.shared.DepotName;
import it.polimi.ingsw.shared.Resource;
import it.polimi.ingsw.shared.Marble;

/**
 * This class is the message sent to ask to put a resource in a depot.
 */
public class PutResourceRequestMsg implements ToViewMsg {

    @Override
    public void changeView(View view, ServerHandler server){

        CommandMsg msg;
        Marble selectedMarble = view.selectMarble();
        DepotName selectedDepot = view.selectDepot();

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
