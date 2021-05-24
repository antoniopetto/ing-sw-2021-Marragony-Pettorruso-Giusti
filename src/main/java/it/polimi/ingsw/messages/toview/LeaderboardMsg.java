package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

public class LeaderboardMsg implements ViewMsg{

    @Override
    public void changeView(View view, ServerHandler handler) {

    }

    @Override
    public String toString() {
        return "LeaderboardMsg{}";
    }
}
