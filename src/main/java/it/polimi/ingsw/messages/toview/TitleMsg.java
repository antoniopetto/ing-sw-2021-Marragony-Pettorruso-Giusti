package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;

public class TitleMsg implements ViewMsg{

    @Override
    public void changeView(View view, ServerHandler server) {
        view.showTitle();
    }

    @Override
    public String toString() {
        return "TitleMsg{}";
    }
}
