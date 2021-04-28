package it.polimi.ingsw.shared.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.views.View;

import java.io.IOException;

public interface ViewMsg {
    void changeView(View view, ServerHandler server) throws IOException;
}
