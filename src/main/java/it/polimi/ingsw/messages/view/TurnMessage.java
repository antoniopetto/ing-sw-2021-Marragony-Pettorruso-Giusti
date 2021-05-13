package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.io.IOException;

public class TurnMessage implements ViewMsg{

    private boolean postTurn;
    public TurnMessage(boolean postTurn)
    {
        this.postTurn=postTurn;
    }
    @Override
    public void changeView(View view, ServerHandler server) throws IOException {
        CommandMsg msg = view.selectMove(postTurn);
        server.writeObject(msg);
    }
}
