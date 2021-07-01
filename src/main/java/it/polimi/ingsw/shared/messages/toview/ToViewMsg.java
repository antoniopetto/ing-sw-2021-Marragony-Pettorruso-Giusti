package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.Msg;

public interface ToViewMsg extends Msg {
    void changeView(View view, ServerHandler server);
}