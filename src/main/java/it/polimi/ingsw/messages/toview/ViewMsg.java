package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.Msg;

import java.io.IOException;
import java.io.Serializable;

public interface ViewMsg extends Msg {
    void changeView(View view, ServerHandler server) throws IOException;
}
