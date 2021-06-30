package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.io.IOException;

public class NextActionMsg implements ToViewMsg {

    private final boolean postTurn;


    public NextActionMsg(boolean postTurn) {
        this.postTurn = postTurn;

    }

    @Override
    public void changeView(View view, ServerHandler server) {
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
