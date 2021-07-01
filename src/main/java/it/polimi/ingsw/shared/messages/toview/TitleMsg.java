package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

public class TitleMsg implements ToViewMsg {

    @Override
    public void changeView(View view, ServerHandler server) {
        view.showTitle();
    }

    @Override
    public String toString() {
        return "TitleMsg{}";
    }
}
