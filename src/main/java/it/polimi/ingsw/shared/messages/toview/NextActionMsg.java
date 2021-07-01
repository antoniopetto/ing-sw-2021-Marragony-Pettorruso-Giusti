package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.command.CommandMsg;

/**
 * This class is the message sent to tell the player to select a move.
 */
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
