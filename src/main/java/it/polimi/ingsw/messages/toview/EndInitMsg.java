package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

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
