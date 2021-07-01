package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.command.UsernameMsg;

/**
 * This class is the message sent to ask the username.
 */
public class UsernameRequestMsg implements ToViewMsg {

    @Override
    public void changeView(View view, ServerHandler handler){
        String username = view.getUsername();
        UsernameMsg message = new UsernameMsg(username);
        handler.writeObject(message);
    }

    @Override
    public String toString() {
        return "UsernameRequestMsg{}";
    }
}
