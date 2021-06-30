package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.CommandMsg;


import java.io.IOException;

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
