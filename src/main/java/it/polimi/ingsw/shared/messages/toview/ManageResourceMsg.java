package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.command.CommandMsg;

/**
 * This class is the message that is sent after a line of marbles is bought in the marketBoard until the marble buffer is empty.
 */
public class ManageResourceMsg implements ToViewMsg {
    @Override
    public void changeView(View view, ServerHandler server){
        CommandMsg msg = view.manageResource();
        server.writeObject(msg);
    }

    @Override
    public String toString() {
        return "ManageResourceMsg{}";
    }
}
