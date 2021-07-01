package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.Msg;

/**
 * This interface is implemented from all the messages sent from the server to the client with the aim to notify the view.
 */
public interface ToViewMsg extends Msg {

    /**
     * It is the action that need to be done in the view.
     * @param view is the view of the client
     * @param server is the server handler that handles the message.
     */
    void changeView(View view, ServerHandler server);
}