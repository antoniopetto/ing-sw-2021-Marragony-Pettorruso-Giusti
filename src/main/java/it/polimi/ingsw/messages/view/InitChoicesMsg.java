package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.DiscardLeaderCardMsg;
import it.polimi.ingsw.messages.update.UpdateMsg;
import it.polimi.ingsw.messages.view.ViewMsg;


import java.io.IOException;

public class InitChoicesMsg implements ViewMsg {



    @Override
    public void changeView(View view, ServerHandler server) throws IOException {
        int cardId = view.getDiscardedLeaderCard();
        DiscardLeaderCardMsg msg = new DiscardLeaderCardMsg(cardId);
        server.writeObject(msg);
    }
}
