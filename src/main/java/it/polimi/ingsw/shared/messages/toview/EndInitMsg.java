package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

/**
 * This class is the message that notify the end of the initialization for all the players.
 */
public class EndInitMsg implements ToViewMsg {

    @Override
    public void changeView(View view, ServerHandler handler){
        view.endInit();
    }

    @Override
    public String toString() {
        return "EndInit{}";
    }
}
