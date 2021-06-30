package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;

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
