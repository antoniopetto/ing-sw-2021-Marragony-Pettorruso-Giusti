package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.command.CommandMsg;

public class DiscardLeaderRequestMsg implements ToViewMsg {

    @Override
    public void changeView(View view, ServerHandler server){
        CommandMsg msg = view.discardLeaderCard();
        server.writeObject(msg);
    }

    @Override
    public String toString() {
        return "DiscardLeaderRequestMsg{}";
    }
}
