package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.io.IOException;

public class TurnMsg implements ViewMsg{

    private final boolean postTurn;


    public TurnMsg(boolean postTurn) {
        this.postTurn = postTurn;

    }


    @Override
    public void changeView(View view, ServerHandler server) throws IOException {
        CommandMsg msg = view.selectMove(postTurn);
        server.writeObject(msg);
    }

    @Override
    public String toString() {
        return "TurnMsg{" +
                "postTurn=" + postTurn +
                '}';
    }
}
